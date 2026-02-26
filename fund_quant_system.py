#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
基金量化分析系统（增强版 - 支持QDII基金 + 份额管理 + 实时收益预估）

功能：
1. 使用天天基金真实API获取数据
2. 提供买入/卖出建议  
3. 支持多基金监控
4. 增强QDII基金支持（通过历史净值页面）
5. tushare备用数据源
6. 智能份额管理（基于市值和日期自动计算份额）
7. 实时收益预估
8. 修复基金名称获取问题

注意：此系统使用天天基金公开接口，非官方API，请合理使用
"""

import json
import time
import requests
import re
import pandas as pd
from datetime import datetime, timedelta
from typing import Dict, List, Tuple, Optional
import logging
import os

# 导入份额管理器
from fund_valuation import FundShareManager

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class FundQuantSystem:
    def __init__(self):
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        })
        self.fund_cache = {}
        self.last_update = {}
        self.tushare_available = False
        self._check_tushare()
        self.share_manager = FundShareManager()
        
    def _check_tushare(self):
        """检查tushare是否可用"""
        try:
            import tushare as ts
            token = os.environ.get('TUSHARE_TOKEN')
            if token:
                ts.set_token(token)
                self.tushare_available = True
                logger.info("Tushare已配置，可使用tushare数据源")
            else:
                logger.warning("Tushare已安装但未提供token，部分功能可能受限")
        except ImportError:
            logger.info("Tushare未安装，跳过tushare数据源")
        except Exception as e:
            logger.warning(f"Tushare初始化失败: {e}")
    
    def _parse_jsonp(self, text: str) -> Dict:
        """解析JSONP格式的数据"""
        try:
            # 提取JSON部分
            match = re.match(r".*?({.*}).*", text, re.S)
            if match:
                return json.loads(match.group(1))
            return {}
        except Exception as e:
            logger.error(f"JSONP解析失败: {e}")
            return {}
    
    def _get_fund_name_from_eastmoney(self, fund_code: str) -> str:
        """从天天基金详情页面获取基金名称"""
        try:
            # 尝试从基金档案页面获取名称
            url = f"http://fundf10.eastmoney.com/jbgk_{fund_code}.html"
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                # 查找基金名称的HTML模式
                # 通常在 <h4 class="title">基金名称</h4> 或类似结构中
                name_patterns = [
                    r'<h4[^>]*class="title"[^>]*>([^<]+)</h4>',
                    r'<div[^>]*class="fundDetail-tit"[^>]*>\s*<div[^>]*>\s*([^<]+)',
                    r'<span[^>]*class="name"[^>]*>([^<]+)</span>',
                    r'基金简称：</td>\s*<td[^>]*>([^<]+)</td>'
                ]
                
                for pattern in name_patterns:
                    match = re.search(pattern, response.text)
                    if match:
                        name = match.group(1).strip()
                        if name and "暂无数据" not in name:
                            return name
                
                # 尝试从页面标题提取
                title_match = re.search(r'<title>([^_]+)_', response.text)
                if title_match:
                    name = title_match.group(1).strip()
                    if name and len(name) > 2:
                        return name
                        
        except Exception as e:
            logger.warning(f"从天天基金详情页获取{fund_code}名称失败: {e}")
        
        # 备用方案：尝试从概要页面获取
        try:
            url = f"http://fund.eastmoney.com/{fund_code}.html"
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                # 查找页面中的基金名称
                name_match = re.search(r'<div[^>]*class="fundName"[^>]*>.*?<a[^>]*>([^<]+)</a>', response.text)
                if name_match:
                    name = name_match.group(1).strip()
                    if name and "暂无数据" not in name:
                        return name
                        
                # 尝试其他模式
                name_match2 = re.search(r'var fS_name\s*=\s*"([^"]+)"', response.text)
                if name_match2:
                    name = name_match2.group(1).strip()
                    if name and len(name) > 2:
                        return name
                        
        except Exception as e:
            logger.warning(f"从天天基金概要页获取{fund_code}名称失败: {e}")
        
        return ""
    
    def _get_from_fundgz(self, fund_code: str) -> Dict:
        """从天天基金实时估值API获取数据"""
        try:
            url = f"https://fundgz.1234567.com.cn/js/{fund_code}.js"
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                data = self._parse_jsonp(response.text)
                if data and 'fundcode' in data:
                    return {
                        'fund_code': data.get('fundcode', fund_code),
                        'name': data.get('name', ''),
                        'nav_date': data.get('jzrq', ''),  # 净值日期
                        'nav': float(data.get('dwjz', 0)),  # 单位净值
                        'estimate_value': float(data.get('gsz', 0)),  # 估算净值
                        'estimate_growth': float(data.get('gszzl', 0)),  # 估算增长率
                        'estimate_time': data.get('gztime', ''),  # 估算时间
                        'last_update': datetime.now().isoformat(),
                        'data_source': 'fundgz'
                    }
            else:
                logger.warning(f"基金{fund_code}实时数据获取失败，状态码: {response.status_code}")
                
        except Exception as e:
            logger.error(f"获取基金{fund_code}实时数据失败: {e}")
        
        return None
    
    def _get_from_eastmoney_history(self, fund_code: str) -> Dict:
        """从天天基金历史净值页面获取最新数据"""
        try:
            url = f"http://fundf10.eastmoney.com/jjjz_{fund_code}.html"
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                # 先尝试获取基金名称
                name = ""
                try:
                    # 从页面中提取基金名称
                    name_match = re.search(r'<div[^>]*class="fundDetail-tit"[^>]*>.*?<div[^>]*>([^<]+)', response.text)
                    if name_match:
                        name = name_match.group(1).strip()
                except:
                    pass
                
                if not name:
                    name = self._get_fund_name_from_eastmoney(fund_code)
                
                # 解析HTML获取最新净值信息
                # 查找 "单位净值（日期）： <b>数值 ( 涨幅% )</b>"
                nav_pattern = r'单位净值（(\d{2}-\d{2})）：\s*<b[^>]*>\s*([\d.]+)\s*\(\s*([+-]?[\d.]+)%\s*\)'
                match = re.search(nav_pattern, response.text)
                
                if match:
                    date_str = match.group(1)
                    nav_value = float(match.group(2))
                    growth_rate = float(match.group(3))
                    
                    # 转换日期格式 (假设是今年)
                    current_year = datetime.now().year
                    full_date = f"{current_year}-{date_str}"
                    
                    return {
                        'fund_code': fund_code,
                        'name': name,
                        'nav_date': full_date,
                        'nav': nav_value,
                        'estimate_value': nav_value,  # 使用最新净值作为估算值
                        'estimate_growth': growth_rate,
                        'estimate_time': '',
                        'last_update': datetime.now().isoformat(),
                        'data_source': 'eastmoney_history'
                    }
                else:
                    # 尝试其他模式
                    nav_simple = r'单位净值[^\\n]*?\\n\\n\s*([\d.]+)'
                    match2 = re.search(nav_simple, response.text)
                    if match2:
                        nav_value = float(match2.group(1))
                        return {
                            'fund_code': fund_code,
                            'name': name,
                            'nav_date': datetime.now().strftime('%Y-%m-%d'),
                            'nav': nav_value,
                            'estimate_value': nav_value,
                            'estimate_growth': 0.0,
                            'estimate_time': '',
                            'last_update': datetime.now().isoformat(),
                            'data_source': 'eastmoney_history_simple'
                        }
                    
        except Exception as e:
            logger.error(f"从历史净值获取{fund_code}失败: {e}")
        
        return None
    
    def _get_from_tushare(self, fund_code: str) -> Dict:
        """使用tushare获取基金数据"""
        if not self.tushare_available:
            return None
            
        try:
            import tushare as ts
            # 获取基金基本信息
            info_df = ts.fund.get_fund_info(fund_code)
            if info_df is not None and not info_df.empty:
                name = info_df['jjqc'].iloc[0] if 'jjqc' in info_df.columns else ''
                
                # 获取最近的净值数据
                end_date = datetime.now().strftime('%Y-%m-%d')
                start_date = (datetime.now() - timedelta(days=7)).strftime('%Y-%m-%d')
                nav_df = ts.fund.get_nav_history(fund_code, start=start_date, end=end_date)
                
                if nav_df is not None and not nav_df.empty:
                    latest = nav_df.iloc[0]
                    return {
                        'fund_code': fund_code,
                        'name': name,
                        'nav_date': latest['date'],
                        'nav': float(latest['value']),
                        'estimate_value': float(latest['value']),
                        'estimate_growth': 0.0,  # tushare不提供当日涨幅
                        'estimate_time': '',
                        'last_update': datetime.now().isoformat(),
                        'data_source': 'tushare'
                    }
        except Exception as e:
            logger.error(f"tushare获取{fund_code}失败: {e}")
        
        return None
    
    def get_fund_realtime_info(self, fund_code: str) -> Dict:
        """获取基金实时估值信息，多数据源智能切换"""
        logger.info(f"尝试获取基金{fund_code}数据...")
        
        # 数据源优先级：天天基金实时估值 -> 天天基金历史净值 -> tushare
        data_sources = [
            (self._get_from_fundgz, "天天基金实时API"),
            (self._get_from_eastmoney_history, "天天基金历史净值"),
            (self._get_from_tushare, "tushare")
        ]
        
        final_data = None
        for source_func, source_name in data_sources:
            try:
                data = source_func(fund_code)
                if data and data.get('nav', 0) > 0:
                    logger.info(f"基金{fund_code}数据获取成功，来源: {source_name}")
                    final_data = data
                    break
            except Exception as e:
                logger.warning(f"{source_name}获取{fund_code}失败: {e}")
                continue
        
        if final_data is None:
            logger.error(f"所有数据源都无法获取基金{fund_code}数据")
            final_data = {
                'fund_code': fund_code,
                'name': '',
                'nav_date': '',
                'nav': 0,
                'estimate_value': 0,
                'estimate_growth': 0,
                'estimate_time': '',
                'last_update': datetime.now().isoformat(),
                'data_source': 'none'
            }
        
        # 如果没有获取到名称，尝试专门获取名称
        if not final_data.get('name', '').strip():
            name = self._get_fund_name_from_eastmoney(fund_code)
            if name:
                final_data['name'] = name
                logger.info(f"基金{fund_code}名称补充成功: {name}")
        
        # 如果还是没有名称，使用基金代码作为默认名称
        if not final_data.get('name', '').strip():
            final_data['name'] = f"基金{fund_code}"
            logger.info(f"基金{fund_code}使用默认名称")
        
        return final_data
    
    def get_fund_history_nav(self, fund_code: str, days: int = 365) -> List[Dict]:
        """获取基金历史净值数据"""
        try:
            url = f"https://fund.eastmoney.com/pingzhongdata/{fund_code}.js"
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                text = response.text
                
                # 提取累计净值走势数据
                nav_match = re.search(r'Data_ACWorthTrend\s*=\s*(\[.*?\]);', text, re.S)
                if nav_match:
                    nav_data_str = nav_match.group(1)
                    try:
                        nav_data = json.loads(nav_data_str)
                        
                        # 转换为标准格式
                        history_data = []
                        for item in nav_data:
                            if isinstance(item, list) and len(item) >= 2:
                                timestamp = item[0]
                                nav_value = item[1]
                                
                                # 转换时间戳为日期
                                date_obj = datetime.fromtimestamp(timestamp / 1000)
                                date_str = date_obj.strftime('%Y-%m-%d')
                                
                                history_data.append({
                                    'date': date_str,
                                    'nav': nav_value
                                })
                        
                        # 按日期排序并返回最近N天的数据
                        history_data.sort(key=lambda x: x['date'], reverse=True)
                        end_date = datetime.now()
                        start_date = end_date - timedelta(days=days)
                        
                        filtered_data = []
                        for item in history_data:
                            item_date = datetime.strptime(item['date'], '%Y-%m-%d')
                            if item_date >= start_date:
                                filtered_data.append(item)
                            else:
                                break
                        
                        return filtered_data
                        
                    except json.JSONDecodeError:
                        logger.error(f"基金{fund_code}历史数据JSON解析失败")
                
        except Exception as e:
            logger.error(f"获取基金{fund_code}历史数据失败: {e}")
        
        return []
    
    def calculate_technical_indicators(self, history_data: List[Dict]) -> Dict:
        """计算技术指标"""
        if len(history_data) < 5:
            return {}
        
        # 提取净值列表
        nav_values = [item['nav'] for item in history_data]
        nav_values.reverse()  # 按时间顺序排列
        
        # 计算移动平均线
        def calculate_ma(values, period):
            if len(values) < period:
                return []
            return [sum(values[i:i+period]) / period for i in range(len(values) - period + 1)]
        
        ma5 = calculate_ma(nav_values, 5)
        ma10 = calculate_ma(nav_values, 10)
        ma20 = calculate_ma(nav_values, 20)
        
        # 计算RSI
        def calculate_rsi(values, period=14):
            if len(values) < period + 1:
                return 50
            
            gains = []
            losses = []
            
            for i in range(1, len(values)):
                change = values[i] - values[i-1]
                if change > 0:
                    gains.append(change)
                    losses.append(0)
                else:
                    gains.append(0)
                    losses.append(abs(change))
            
            if len(gains) < period:
                return 50
            
            avg_gain = sum(gains[-period:]) / period
            avg_loss = sum(losses[-period:]) / period
            
            if avg_loss == 0:
                return 100 if avg_gain > 0 else 50
            
            rs = avg_gain / avg_loss
            rsi = 100 - (100 / (1 + rs))
            return rsi
        
        rsi = calculate_rsi(nav_values)
        
        return {
            'ma5': ma5[-1] if ma5 else None,
            'ma10': ma10[-1] if ma10 else None,
            'ma20': ma20[-1] if ma20 else None,
            'rsi': rsi,
            'current_nav': nav_values[-1] if nav_values else 0
        }
    
    def get_trading_advice(self, fund_code: str, realtime_info: Dict, indicators: Dict) -> Dict:
        """生成交易建议"""
        current_nav = indicators.get('current_nav', 0)
        ma5 = indicators.get('ma5')
        ma10 = indicators.get('ma10')
        rsi = indicators.get('rsi', 50)
        
        advice = "hold"
        reason = "趋势不明确"
        
        # 基于移动平均线的策略
        if ma5 is not None and ma10 is not None:
            if current_nav > ma5 > ma10:
                advice = "buy"
                reason = "价格在短期和长期均线上方，呈上升趋势"
            elif current_nav < ma5 < ma10:
                advice = "sell"
                reason = "价格在短期和长期均线下方，呈下降趋势"
        
        # RSI超买超卖信号
        if rsi > 70:
            if advice == "buy":
                advice = "hold"
                reason += "，但RSI超买，建议观望"
            elif advice == "hold":
                advice = "sell"
                reason = "RSI超买，可能存在回调风险"
        elif rsi < 30:
            if advice == "sell":
                advice = "hold"
                reason += "，但RSI超卖，建议观望"
            elif advice == "hold":
                advice = "buy"
                reason = "RSI超卖，可能存在反弹机会"
        
        return {
            'advice': advice,
            'reason': reason,
            'confidence': self._calculate_confidence(realtime_info, indicators)
        }
    
    def _calculate_confidence(self, realtime_info: Dict, indicators: Dict) -> float:
        """计算建议置信度"""
        confidence = 0.5  # 基础置信度
        
        # 根据估算增长率调整置信度
        estimate_growth = realtime_info.get('estimate_growth', 0)
        if abs(estimate_growth) > 2:
            confidence += 0.2
        
        # 根据RSI位置调整
        rsi = indicators.get('rsi', 50)
        if rsi < 20 or rsi > 80:
            confidence += 0.15
        
        return min(confidence, 0.95)  # 最大置信度95%
    
    def analyze_fund(self, fund_code: str) -> Dict:
        """分析单只基金并返回完整报告"""
        try:
            logger.info(f"开始分析基金 {fund_code}")
            
            # 1. 获取实时信息
            realtime_info = self.get_fund_realtime_info(fund_code)
            if realtime_info['nav'] == 0:
                return {
                    'status': 'error',
                    'error': f'基金代码 {fund_code} 不存在或无法获取数据',
                    'analysis_time': datetime.now().isoformat()
                }
            
            # 2. 获取历史数据
            history_data = self.get_fund_history_nav(fund_code, days=180)
            
            # 3. 计算技术指标
            indicators = self.calculate_technical_indicators(history_data)
            
            # 4. 生成交易建议
            trading_advice = self.get_trading_advice(fund_code, realtime_info, indicators)
            
            # 5. 构建完整报告
            report = {
                'fund_info': realtime_info,
                'history_data_count': len(history_data),
                'technical_indicators': indicators,
                'trading_advice': trading_advice,
                'analysis_time': datetime.now().isoformat(),
                'status': 'success'
            }
            
            logger.info(f"基金 {fund_code} 分析完成")
            return report
            
        except Exception as e:
            logger.error(f"分析基金 {fund_code} 失败: {e}")
            return {
                'status': 'error',
                'error': str(e),
                'analysis_time': datetime.now().isoformat()
            }
    
    def analyze_multiple_funds(self, fund_codes: List[str]) -> Dict[str, Dict]:
        """批量分析多个基金"""
        results = {}
        for fund_code in fund_codes:
            results[fund_code] = self.analyze_fund(fund_code)
            # 添加延迟避免请求过快
            time.sleep(0.5)
        return results
    
    def set_holding(self, fund_code: str, current_value: float, value_date: str):
        """设置基金持仓（用于实时收益计算）"""
        self.share_manager.set_holding(fund_code, current_value, value_date)
    
    def get_realtime_profit(self, fund_code: str) -> Dict:
        """获取基金实时收益预估"""
        return self.share_manager.get_realtime_profit(fund_code)
    
    def get_all_profits(self) -> Dict[str, Dict]:
        """获取所有持仓基金的实时收益"""
        return self.share_manager.get_all_profits()


def main():
    """命令行测试入口"""
    system = FundQuantSystem()
    
    # 测试示例（包含QDII基金）
    test_funds = ["018147", "519771", "018957", "400015", "017811", "007280", "018543", "015790", "015916", "024203", "025942", "002207", "017436","018463"]
    
    print("=== 基金量化分析系统（增强版 - 支持QDII基金 + 份额管理 + 实时收益预估）===")
    
    if system.tushare_available:
        print("Tushare可用性: ✅ 已配置")
    else:
        print("Tushare可用性: ❌ 未配置")
        print("如需启用tushare，请设置环境变量: export TUSHARE_TOKEN=your_token")
    print("实时收益预估: ✅ 可用")
    print()
    
    # 测试实时收益功能
    print("=== 实时收益预估测试 ===")
    # 设置一个测试持仓（假设今天持有10000元）
    system.set_holding("000001", current_value=10000, value_date="2026-02-24")
    profit_result = system.get_realtime_profit("000001")
    
    if profit_result['success']:
        print(f"基金: {profit_result['fund_name']} ({profit_result['fund_code']})")
        print(f"持有份额: {profit_result['shares']:.4f}份")
        print(f"昨日净值: {profit_result['yesterday_nav']:.4f}")
        print(f"今日估算: {profit_result['today_estimate_nav']:.4f} ({profit_result['estimate_growth_pct']:+.2f}%)")
        print(f"实时收益: ¥{profit_result['profit_amount']:+.2f} ({profit_result['profit_rate']:+.2%})")
        print(f"当前市值: ¥{profit_result['current_value']:,.2f}")
    else:
        print(f"实时收益计算失败: {profit_result['error']}")
    print("-" * 50)
    print()
    
    for fund in test_funds:
        print(f"分析基金 {fund}...")
        result = system.analyze_fund(fund)
        
        if result["status"] == "success":
            fund_info = result['fund_info']
            advice = result['trading_advice']
            
            print(f"基金名称: {fund_info['name']}")
            print(f"当前净值: {fund_info['nav']:.4f}")
            print(f"估算净值: {fund_info['estimate_value']:.4f} ({fund_info['estimate_growth']:+.2f}%)")
            print(f"数据来源: {fund_info['data_source']}")
            print(f"交易建议: {advice['advice'].upper()}")
            print(f"建议理由: {advice['reason']}")
            print(f"置信度: {advice['confidence']:.2%}")
            print(f"历史数据: {result['history_data_count']} 天")
        else:
            print(f"分析失败: {result['error']}")
        
        print("-" * 50)


if __name__ == "__main__":
    main()