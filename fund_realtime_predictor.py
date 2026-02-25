#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
基金实时估值预测器

功能：
1. 获取基金最新持仓数据（前十大重仓股）
2. 获取实时股票价格
3. 基于持仓权重计算基金实时估值预测
4. 提供预测置信度评估

注意：此模块依赖tushare获取A股实时数据，需要配置TUSHARE_TOKEN环境变量
"""

import os
import json
import time
import requests
import logging
from datetime import datetime, timedelta
from typing import Dict, List, Tuple, Optional
import re

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class FundRealTimePredictor:
    def __init__(self, tushare_token: str = None):
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        })
        self.tushare_available = False
        self.tushare_token = tushare_token or os.environ.get('TUSHARE_TOKEN')
        self._init_tushare()
        self.holdings_cache = {}
        self.cache_expiry = 3600  # 持仓缓存1小时
        
    def _init_tushare(self):
        """初始化tushare"""
        if not self.tushare_token:
            logger.warning("Tushare token未配置，实时股票数据功能将受限")
            return
            
        try:
            import tushare as ts
            ts.set_token(self.tushare_token)
            self.pro = ts.pro_api()
            self.tushare_available = True
            logger.info("Tushare已成功初始化")
        except ImportError:
            logger.warning("tushare未安装，无法获取实时股票数据")
        except Exception as e:
            logger.warning(f"Tushare初始化失败: {e}")
    
    def _get_fund_holdings_from_eastmoney(self, fund_code: str) -> Dict:
        """从天天基金获取基金持仓数据"""
        try:
            url = f"http://fundf10.eastmoney.com/FundArchivesDatas.aspx?type=jjcc&code={fund_code}&topline=10&year=&month="
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                content = response.text
                
                # 提取JSONP中的HTML内容
                match = re.search(r'var apidata=\{.*?content:"([^"]*)"', content, re.DOTALL)
                if not match:
                    logger.warning(f"基金{fund_code}持仓数据格式不匹配")
                    return None
                
                html_content = match.group(1)
                # 处理转义字符
                html_content = html_content.replace('\\/', '/')
                html_content = html_content.replace('\\"', '"')
                html_content = html_content.replace('\\n', '\n')
                html_content = html_content.replace('\\t', '\t')
                
                # 解析HTML表格
                holdings = []
                # 查找表格行
                rows = re.findall(r'<tr>.*?</tr>', html_content, re.DOTALL)
                
                for row in rows:
                    # 跳过表头
                    if '<th>' in row:
                        continue
                    
                    # 提取股票代码
                    code_match = re.search(r'<a href=[^>]*>(\d{6})</a>', row)
                    if not code_match:
                        continue
                    stock_code = code_match.group(1)
                    
                    # 提取股票名称
                    name_match = re.search(r"<td class='tol'><a[^>]*>([^<]+)</a>", row)
                    stock_name = name_match.group(1) if name_match else "未知"
                    
                    # 提取持仓比例
                    ratio_match = re.search(r"<td class='tor'>([\d.]+)%</td>", row)
                    if not ratio_match:
                        continue
                    ratio = float(ratio_match.group(1)) / 100.0
                    
                    holdings.append({
                        'stock_code': stock_code,
                        'stock_name': stock_name,
                        'ratio': ratio
                    })
                
                if holdings:
                    # 只取前10个
                    holdings = holdings[:10]
                    total_ratio = sum(h['ratio'] for h in holdings)
                    return {
                        'fund_code': fund_code,
                        'holdings': holdings,
                        'total_disclosed_ratio': total_ratio,
                        'data_source': 'eastmoney',
                        'last_update': datetime.now().isoformat()
                    }
                    
        except Exception as e:
            logger.error(f"获取基金{fund_code}持仓数据失败: {e}")
        
        return None
    
    def _get_stock_real_time_price(self, stock_codes: List[str]) -> Dict[str, float]:
        """获取股票实时价格变化率"""
        if not self.tushare_available:
            logger.warning("tushare不可用，无法获取实时股票价格")
            return {}
            
        try:
            import tushare as ts
            # 格式化股票代码（tushare格式）
            ts_codes = []
            for code in stock_codes:
                if code.startswith('6'):
                    ts_codes.append(f"{code}.SH")
                elif code.startswith(('0', '3')):
                    ts_codes.append(f"{code}.SZ")
                else:
                    # 其他代码暂时跳过
                    continue
            
            if not ts_codes:
                return {}
                
            # 获取实时行情
            df = ts.get_realtime_quotes(ts_codes[:50])  # tushare限制50个
            
            price_changes = {}
            for _, row in df.iterrows():
                if 'changepercent' in row and pd.notna(row['changepercent']):
                    # 转换为小数形式（如 2.5 表示 +2.5%）
                    change_pct = float(row['changepercent']) / 100.0
                    # 提取原始代码
                    orig_code = row['code']
                    price_changes[orig_code] = change_pct
                    
            return price_changes
            
        except Exception as e:
            logger.error(f"获取实时股票价格失败: {e}")
            return {}
    
    def _get_index_change_for_fund(self, fund_code: str) -> float:
        """根据基金类型获取相关指数涨跌幅作为补充"""
        # 简单映射，实际可以更复杂
        fund_type_mapping = {
            '000001': '000300.SH',  # 沪深300
            '110022': '399975.SZ',  # 中证消费
            '400015': '399976.SZ',  # 新能源车
            # 可以扩展更多映射
        }
        
        # 默认使用沪深300
        index_code = fund_type_mapping.get(fund_code, '000300.SH')
        
        if self.tushare_available:
            try:
                import tushare as ts
                today = datetime.now().strftime('%Y%m%d')
                df = self.pro.index_daily(ts_code=index_code, start_date=today, end_date=today)
                if not df.empty:
                    change_pct = (df.iloc[0]['close'] - df.iloc[0]['pre_close']) / df.iloc[0]['pre_close']
                    return change_pct
            except Exception as e:
                logger.warning(f"获取指数{index_code}数据失败: {e}")
        
        # 如果无法获取，返回0
        return 0.0
    
    def get_fund_holdings(self, fund_code: str) -> Optional[Dict]:
        """获取基金持仓数据（带缓存）"""
        # 检查缓存
        if fund_code in self.holdings_cache:
            cache_data = self.holdings_cache[fund_code]
            cache_time = datetime.fromisoformat(cache_data['last_update'])
            if (datetime.now() - cache_time).total_seconds() < self.cache_expiry:
                return cache_data
        
        # 获取新数据
        holdings_data = self._get_fund_holdings_from_eastmoney(fund_code)
        if holdings_data:
            self.holdings_cache[fund_code] = holdings_data
            
        return holdings_data
    
    def predict_fund_value_change(self, fund_code: str) -> Dict:
        """预测基金净值变化"""
        try:
            # 1. 获取基金持仓
            holdings_data = self.get_fund_holdings(fund_code)
            if not holdings_data:
                return {
                    'success': False,
                    'error': '无法获取基金持仓数据',
                    'predicted_change': 0.0,
                    'confidence': 0.0,
                    'data_source': 'none'
                }
            
            holdings = holdings_data['holdings']
            total_disclosed_ratio = holdings_data['total_disclosed_ratio']
            
            if not holdings:
                return {
                    'success': False,
                    'error': '持仓数据为空',
                    'predicted_change': 0.0,
                    'confidence': 0.0,
                    'data_source': 'none'
                }
            
            # 2. 获取实时股票价格变化
            stock_codes = [h['stock_code'] for h in holdings]
            stock_changes = self._get_stock_real_time_price(stock_codes)
            
            # 3. 计算预测净值变化
            predicted_change = 0.0
            weighted_stocks_count = 0
            
            for holding in holdings:
                stock_code = holding['stock_code']
                ratio = holding['ratio']
                
                if stock_code in stock_changes:
                    stock_change = stock_changes[stock_code]
                    predicted_change += stock_change * ratio
                    weighted_stocks_count += 1
            
            # 4. 处理未披露持仓部分
            undisclosed_ratio = 1.0 - total_disclosed_ratio
            if undisclosed_ratio > 0 and weighted_stocks_count > 0:
                # 使用相关指数替代未披露部分
                index_change = self._get_index_change_for_fund(fund_code)
                predicted_change += index_change * undisclosed_ratio
            
            # 5. 计算置信度
            confidence = self._calculate_prediction_confidence(
                holdings_data, stock_changes, weighted_stocks_count
            )
            
            return {
                'success': True,
                'predicted_change': predicted_change,
                'predicted_change_pct': predicted_change * 100,
                'confidence': confidence,
                'data_source': 'realtime_prediction',
                'holdings_coverage': total_disclosed_ratio,
                'stocks_with_realtime_data': weighted_stocks_count,
                'total_stocks': len(holdings),
                'last_update': datetime.now().isoformat()
            }
            
        except Exception as e:
            logger.error(f"预测基金{fund_code}净值变化失败: {e}")
            return {
                'success': False,
                'error': str(e),
                'predicted_change': 0.0,
                'confidence': 0.0,
                'data_source': 'none'
            }
    
    def _calculate_prediction_confidence(self, holdings_data: Dict, 
                                       stock_changes: Dict, 
                                       weighted_stocks_count: int) -> float:
        """计算预测置信度"""
        confidence = 0.3  # 基础置信度
        
        # 持仓覆盖率
        coverage = holdings_data['total_disclosed_ratio']
        if coverage >= 0.5:
            confidence += 0.3
        elif coverage >= 0.3:
            confidence += 0.2
        elif coverage >= 0.1:
            confidence += 0.1
        
        # 实时数据覆盖率
        total_stocks = len(holdings_data['holdings'])
        if total_stocks > 0:
            realtime_coverage = weighted_stocks_count / total_stocks
            if realtime_coverage >= 0.8:
                confidence += 0.2
            elif realtime_coverage >= 0.5:
                confidence += 0.1
        
        # tushare可用性
        if self.tushare_available:
            confidence += 0.1
        
        return min(confidence, 0.95)


def test_predictor():
    """测试函数"""
    predictor = FundRealTimePredictor()
    
    # 测试几个基金
    test_funds = ["000001", "110022", "400015"]
    
    print("=== 基金实时估值预测器测试 ===")
    
    for fund in test_funds:
        print(f"\n预测基金 {fund}...")
        result = predictor.predict_fund_value_change(fund)
        
        if result['success']:
            print(f"预测净值变化: {result['predicted_change_pct']:+.2f}%")
            print(f"置信度: {result['confidence']:.2%}")
            print(f"持仓覆盖率: {result['holdings_coverage']:.2%}")
            print(f"实时数据股票数: {result['stocks_with_realtime_data']}/{result['total_stocks']}")
        else:
            print(f"预测失败: {result['error']}")


if __name__ == "__main__":
    test_predictor()