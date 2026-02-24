#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
基金量化系统 - 最终版本（支持天天基金 + tushare双数据源）

功能：
1. 使用天天基金真实API获取数据（主数据源）
2. 使用tushare作为备用数据源
3. 提供买入/卖出建议
4. 支持多基金监控

注意：此系统使用天天基金公开接口和tushare API，请合理使用
"""

import json
import time
import requests
import re
import pandas as pd
from datetime import datetime, timedelta
from typing import Dict, List, Tuple, Optional
import logging

# 尝试导入tushare
try:
    import tushare as ts
    TUSHARE_AVAILABLE = True
except ImportError:
    TUSHARE_AVAILABLE = False
    ts = None

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class FundQuantSystem:
    def __init__(self, tushare_token: str = None):
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        })
        self.fund_cache = {}
        self.last_update = {}
        
        # 初始化tushare
        self.tushare_initialized = False
        if TUSHARE_AVAILABLE and tushare_token:
            try:
                ts.set_token(tushare_token)
                self.pro = ts.pro_api()
                self.tushare_initialized = True
                logger.info("Tushare初始化成功")
            except Exception as e:
                logger.warning(f"Tushare初始化失败: {e}")
                self.tushare_initialized = False
        elif TUSHARE_AVAILABLE and not tushare_token:
            logger.warning("Tushare已安装但未提供token，部分功能可能受限")
    
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
    
    def get_fund_realtime_info_tushare(self, fund_code: str) -> Dict:
        """使用tushare获取基金实时信息（备用方案）"""
        if not self.tushare_initialized:
            return {}
        
        try:
            # 获取开放型基金净值数据
            df = ts.fund.nav.get_nav_open(fund_type='all')
            if df is not None and not df.empty:
                # 查找指定基金
                fund_data = df[df['symbol'] == fund_code]
                if not fund_data.empty:
                    row = fund_data.iloc[0]
                    return {
                        'fund_code': row['symbol'],
                        'name': row['sname'],
                        'nav_date': row['nav_date'],
                        'nav': float(row['per_nav']) if pd.notna(row['per_nav']) else 0,
                        'estimate_value': float(row['per_nav']) if pd.notna(row['per_nav']) else 0,
                        'estimate_growth': float(row['nav_rate']) if pd.notna(row['nav_rate']) else 0,
                        'estimate_time': row['nav_date'],
                        'last_update': datetime.now().isoformat()
                    }
        except Exception as e:
            logger.error(f"使用tushare获取基金{fund_code}实时数据失败: {e}")
        
        return {}
    
    def get_fund_history_nav_tushare(self, fund_code: str, days: int = 365) -> List[Dict]:
        """使用tushare获取基金历史净值数据（备用方案）"""
        if not self.tushare_initialized:
            return []
        
        try:
            end_date = datetime.now().strftime('%Y-%m-%d')
            start_date = (datetime.now() - timedelta(days=days)).strftime('%Y-%m-%d')
            
            df = ts.fund.nav.get_nav_history(code=fund_code, start=start_date, end=end_date)
            if df is not None and not df.empty:
                history_data = []
                for idx, row in df.iterrows():
                    date_str = idx.strftime('%Y-%m-%d') if hasattr(idx, 'strftime') else str(idx)
                    nav_value = float(row['value']) if pd.notna(row['value']) else 0
                    history_data.append({
                        'date': date_str,
                        'nav': nav_value
                    })
                return history_data
        except Exception as e:
            logger.error(f"使用tushare获取基金{fund_code}历史数据失败: {e}")
        
        return []
    
    def get_fund_realtime_info(self, fund_code: str) -> Dict:
        """获取基金实时估值信息（来自天天基金，失败时使用tushare备用）"""
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
                        'last_update': datetime.now().isoformat()
                    }
            else:
                logger.warning(f"基金{fund_code}实时数据获取失败，状态码: {response.status_code}")
                
        except Exception as e:
            logger.error(f"获取基金{fund_code}实时数据失败: {e}")
        
        # 天天基金失败，尝试使用tushare
        logger.info(f"天天基金数据获取失败，尝试使用tushare获取基金{fund_code}数据")
        tushare_data = self.get_fund_realtime_info_tushare(fund_code)
        if tushare_data:
            return tushare_data
        
        return {
            'fund_code': fund_code,
            'name': '',
            'nav_date': '',
            'nav': 0,
            'estimate_value': 0,
            'estimate_growth': 0,
            'estimate_time': '',
            'last_update': datetime.now().isoformat()
        }
    
    def get_fund_history_nav(self, fund_code: str, days: int = 365) -> List[Dict]:
        """获取基金历史净值数据（天天基金为主，tushare为备用）"""
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
        
        # 天天基金失败，尝试使用tushare
        logger.info(f"天天基金历史数据获取失败，尝试使用tushare获取基金{fund_code}历史数据")
        tushare_data = self.get_fund_history_nav_tushare(fund_code, days)
        if tushare_data:
            return tushare_data
        
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
            if not realtime_info['name']:
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


def main():
    """命令行测试入口"""
    # 如果你有tushare token，可以在这里设置
    # tushare_token = "your_tushare_token_here"
    tushare_token = None
    
    system = FundQuantSystem(tushare_token=tushare_token)
    
    # 测试示例
    test_funds = ["000001", "110022", "519697"]
    
    print("=== 基金量化分析系统（天天基金 + tushare双数据源）===")
    print(f"Tushare可用性: {TUSHARE_AVAILABLE}")
    if TUSHARE_AVAILABLE and tushare_token:
        print("Tushare已配置")
    elif TUSHARE_AVAILABLE:
        print("Tushare已安装但未配置token")
    else:
        print("Tushare未安装")
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
            print(f"交易建议: {advice['advice'].upper()}")
            print(f"建议理由: {advice['reason']}")
            print(f"置信度: {advice['confidence']:.2%}")
            print(f"历史数据: {result['history_data_count']} 天")
        else:
            print(f"分析失败: {result['error']}")
        
        print("-" * 50)


if __name__ == "__main__":
    main()