#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Fund Real-time Profit Estimator

Features:
1. Estimates real-time profit based on user's fund holdings
2. Calculates profit using yesterday's NAV vs today's real-time estimated NAV
3. Provides simple configuration interface

Usage:
from fund_realtime_profit import FundRealTimeProfit

# Method 1: Single fund query
profit_calculator = FundRealTimeProfit()
result = profit_calculator.calculate_profit("000001", 10000)
print(result)

# Method 2: Batch fund query
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

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class FundRealTimeProfit:
    def __init__(self):
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        })
    
    def _parse_jsonp(self, text: str) -> Dict:
        """Parse JSONP formatted data"""
        try:
            # Extract JSON part
            start = text.find('{')
            end = text.rfind('}') + 1
            if start != -1 and end != -1:
                json_str = text[start:end]
                return json.loads(json_str)
            return {}
        except Exception as e:
            logger.error(f"JSONP parsing failed: {e}")
            return {}
    
    def get_fund_realtime_info(self, fund_code: str) -> Dict:
        """Get fund real-time valuation information"""
        try:
            # Get real-time valuation data
            url = f"https://fundgz.1234567.com.cn/js/{fund_code}.js"
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                data = self._parse_jsonp(response.text)
                if data and 'fundcode' in data:
                    return {
                        'fund_code': data.get('fundcode', fund_code),
                        'name': data.get('name', ''),
                        'nav_date': data.get('jzrq', ''),  # NAV date (yesterday)
                        'nav': float(data.get('dwjz', 0)),  # Yesterday's unit NAV
                        'estimate_value': float(data.get('gsz', 0)),  # Today's estimated NAV
                        'estimate_growth': float(data.get('gszzl', 0)),  # Today's estimated growth rate
                        'estimate_time': data.get('gztime', ''),  # Estimate time
                        'last_update': datetime.now().isoformat(),
                        'data_source': 'fundgz'
                    }
            else:
                logger.warning(f"Failed to get real-time data for fund {fund_code}, status code: {response.status_code}")
                
        except Exception as e:
            logger.error(f"Failed to get real-time data for fund {fund_code}: {e}")
        
        return None
    
    def calculate_profit(self, fund_code: str, holding_amount: float) -> Dict:
        """
        Calculate real-time profit for a single fund
        
        Args:
            fund_code: Fund code
            holding_amount: Holding amount (in CNY)
            
        Returns:
            Dictionary containing profit information
        """
        try:
            # Get fund real-time information
            fund_info = self.get_fund_realtime_info(fund_code)
            if not fund_info or fund_info['nav'] <= 0:
                return {
                    'success': False,
                    'error': f'Unable to retrieve data for fund {fund_code}',
                    'fund_code': fund_code,
                    'holding_amount': holding_amount
                }
            
            # Calculate shares
            shares = holding_amount / fund_info['nav']
            
            # Calculate current market value
            current_value = shares * fund_info['estimate_value']
            
            # Calculate profit
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
            logger.error(f"Failed to calculate profit for fund {fund_code}: {e}")
            return {
                'success': False,
                'error': str(e),
                'fund_code': fund_code,
                'holding_amount': holding_amount
            }
    
    def calculate_profits(self, holdings: List[Dict[str, Union[str, float]]]) -> List[Dict]:
        """
        Batch calculate real-time profits for multiple funds
        
        Args:
            holdings: List of holdings, format [{"fund_code": "000001", "amount": 10000}, ...]
            
        Returns:
            List of profit results
        """
        results = []
        for holding in holdings:
            fund_code = holding['fund_code']
            amount = holding['amount']
            result = self.calculate_profit(fund_code, amount)
            results.append(result)
        return results


def test_single_fund():
    """Test single fund profit calculation"""
    calculator = FundRealTimeProfit()
    result = calculator.calculate_profit("000001", 10000)
    
    print("=== Single Fund Real-time Profit Test ===")
    if result['success']:
        print(f"Fund: {result['fund_name']} ({result['fund_code']})")
        print(f"Holding Amount: ¥{result['holding_amount']:,.2f}")
        print(f"Yesterday's NAV: {result['yesterday_nav']:.4f}")
        print(f"Today's Estimate: {result['today_estimate_nav']:.4f} ({result['estimate_growth_pct']:+.2f}%)")
        print(f"Real-time Profit: ¥{result['profit_amount']:+.2f} ({result['profit_rate']:+.2%})")
        print(f"Current Market Value: ¥{result['current_value']:,.2f}")
    else:
        print(f"Calculation Failed: {result['error']}")


def test_multiple_funds():
    """Test multiple funds profit calculation"""
    calculator = FundRealTimeProfit()
    holdings = [
        {"fund_code": "000001", "amount": 10000},
        {"fund_code": "110022", "amount": 5000},
        {"fund_code": "400015", "amount": 8000}
    ]
    
    results = calculator.calculate_profits(holdings)
    
    print("\n=== Multiple Funds Real-time Profit Test ===")
    total_profit = 0
    total_value = 0
    
    for result in results:
        if result['success']:
            print(f"{result['fund_name']}: ¥{result['profit_amount']:+.2f} ({result['estimate_growth_pct']:+.2f}%)")
            total_profit += result['profit_amount']
            total_value += result['current_value']
        else:
            print(f"{result['fund_code']}: Calculation Failed - {result['error']}")
    
    print(f"\nTotal Profit: ¥{total_profit:+.2f}")
    print(f"Total Market Value: ¥{total_value:,.2f}")


if __name__ == "__main__":
    test_single_fund()
    test_multiple_funds()