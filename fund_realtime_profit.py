#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
基金实时收益预估器

功能：
1. 根据用户持有的基金金额，预估当前实时收益
2. 基于昨日净值 vs 今日实时估算净值进行计算
3. 提供简单的配置接口

使用方法：
from fund_realtime_profit import FundRealTimeProfit

# 方式1: 单个基金查询
profit_calculator = FundRealTimeProfit()
result = profit_calculator.calculate_profit("000001", 10000)
print(result)

# 方式2: 批量基金查询
holdings = [
    {"fund_code": "000001", "amount": 10000},
    {"fund_code": "110022", "amount": 5000}
]
results = profit_calculator.calculate_profits(holdings)
"""

import requests
import json
import logging
from datetime import datetime
from typing import Dict, List, Union

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class FundRealTimeProfit:
    def __init__(self):
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        })
    
    def _parse_jsonp(self, text: str) -> Dict:
        """解析JSONP格式的数据"""
        try:
            # 提取JSON部分
            start = text.find('{')
            end = text.rfind('}') + 1
            if start != -1 and end != -1:
                json_str = text[start:end]
                return json.loads(json_str)
            return {}
        except Exception as e:
            logger.error(f"JSONP解析失败: {e}")
            return {}
    
    def get_fund_realtime_info(self, fund_code: str) -> Dict:
        """获取基金实时估值信息"""
        try:
            # 获取实时估值数据
            url = f"https://fundgz.1234567.com.cn/js/{fund_code}.js"
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                data = self._parse_jsonp(response.text)
                if data and 'fundcode' in data:
                    return {
                        'fund_code': data.get('fundcode', fund_code),
                        'name': data.get('name', ''),
                        'nav_date': data.get('jzrq', ''),  # 净值日期（昨日）
                        'nav': float(data.get('dwjz', 0)),  # 昨日单位净值
                        'estimate_value': float(data.get('gsz', 0)),  # 今日估算净值
                        'estimate_growth': float(data.get('gszzl', 0)),  # 今日估算增长率
                        'estimate_time': data.get('gztime', ''),  # 估算时间
                        'last_update': datetime.now().isoformat(),
                        'data_source': 'fundgz'
                    }
            else:
                logger.warning(f"基金{fund_code}实时数据获取失败，状态码: {response.status_code}")
                
        except Exception as e:
            logger.error(f"获取基金{fund_code}实时数据失败: {e}")
        
        return None
    
    def calculate_profit(self, fund_code: str, holding_amount: float) -> Dict:
        """
        计算单个基金的实时收益
        
        Args:
            fund_code: 基金代码
            holding_amount: 持有金额（元）
            
        Returns:
            包含收益信息的字典
        """
        try:
            # 获取基金实时信息
            fund_info = self.get_fund_realtime_info(fund_code)
            if not fund_info or fund_info['nav'] <= 0:
                return {
                    'success': False,
                    'error': f'无法获取基金{fund_code}数据',
                    'fund_code': fund_code,
                    'holding_amount': holding_amount
                }
            
            # 计算份额
            shares = holding_amount / fund_info['nav']
            
            # 计算当前市值
            current_value = shares * fund_info['estimate_value']
            
            # 计算收益
            profit_amount = current_value - holding_amount
            profit_rate = fund_info['estimate_growth'] / 100.0
            
            return {
                'success': True,
                'fund_code': fund_code,
                'fund_name': fund_info['name'],
                'holding_amount': holding_amount,
                'shares': shares,
                'yesterday_nav': fund_info['nav'],
                'today_estimate_nav': fund_info['estimate_value'],
                'estimate_growth_pct': fund_info['estimate_growth'],
                'profit_amount': profit_amount,
                'profit_rate': profit_rate,
                'current_value': current_value,
                'nav_date': fund_info['nav_date'],
                'estimate_time': fund_info['estimate_time'],
                'data_source': fund_info['data_source']
            }
            
        except Exception as e:
            logger.error(f"计算基金{fund_code}收益失败: {e}")
            return {
                'success': False,
                'error': str(e),
                'fund_code': fund_code,
                'holding_amount': holding_amount
            }
    
    def calculate_profits(self, holdings: List[Dict[str, Union[str, float]]]) -> List[Dict]:
        """
        批量计算多个基金的实时收益
        
        Args:
            holdings: 持仓列表，格式 [{"fund_code": "000001", "amount": 10000}, ...]
            
        Returns:
            收益结果列表
        """
        results = []
        for holding in holdings:
            fund_code = holding['fund_code']
            amount = holding['amount']
            result = self.calculate_profit(fund_code, amount)
            results.append(result)
        return results


def test_single_fund():
    """测试单个基金收益计算"""
    calculator = FundRealTimeProfit()
    result = calculator.calculate_profit("000001", 10000)
    
    print("=== 单个基金实时收益测试 ===")
    if result['success']:
        print(f"基金: {result['fund_name']} ({result['fund_code']})")
        print(f"持有金额: ¥{result['holding_amount']:,.2f}")
        print(f"昨日净值: {result['yesterday_nav']:.4f}")
        print(f"今日估算: {result['today_estimate_nav']:.4f} ({result['estimate_growth_pct']:+.2f}%)")
        print(f"实时收益: ¥{result['profit_amount']:+.2f} ({result['profit_rate']:+.2%})")
        print(f"当前市值: ¥{result['current_value']:,.2f}")
    else:
        print(f"计算失败: {result['error']}")


def test_multiple_funds():
    """测试多个基金收益计算"""
    calculator = FundRealTimeProfit()
    holdings = [
        {"fund_code": "000001", "amount": 10000},
        {"fund_code": "110022", "amount": 5000},
        {"fund_code": "400015", "amount": 8000}
    ]
    
    results = calculator.calculate_profits(holdings)
    
    print("\n=== 多个基金实时收益测试 ===")
    total_profit = 0
    total_value = 0
    
    for result in results:
        if result['success']:
            print(f"{result['fund_name']}: ¥{result['profit_amount']:+.2f} ({result['estimate_growth_pct']:+.2f}%)")
            total_profit += result['profit_amount']
            total_value += result['current_value']
        else:
            print(f"{result['fund_code']}: 计算失败 - {result['error']}")
    
    print(f"\n总收益: ¥{total_profit:+.2f}")
    print(f"总市值: ¥{total_value:,.2f}")


if __name__ == "__main__":
    test_single_fund()
    test_multiple_funds()