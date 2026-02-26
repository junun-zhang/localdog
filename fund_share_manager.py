#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Fund Share Manager

Features:
1. Automatically calculates and saves fund shares based on user-provided market value and date
2. Uses saved shares for real-time profit estimation
3. Recalculates shares only when market value or date is updated
4. Persists share information to local file

Usage:
from fund_share_manager import FundShareManager

manager = FundShareManager()

# Set holding for the first time (calculates and saves shares)
manager.set_holding("000001", current_value=10000, value_date="2026-02-24")

# Get real-time profit estimation (uses saved shares)
result = manager.get_realtime_profit("000001")

# Get profits for multiple funds at once
results = manager.get_all_profits()
"""

import os
import json
import re
import requests
import logging
from datetime import datetime
from typing import Dict, Optional, List

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class FundShareManager:
    def __init__(self, data_file: str = "fund_shares.json"):
        self.data_file = data_file
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        })
        self.holdings = self._load_holdings()
    
    def _load_holdings(self) -> Dict:
        """Load holding data from file"""
        if os.path.exists(self.data_file):
            try:
                with open(self.data_file, 'r', encoding='utf-8') as f:
                    return json.load(f)
            except Exception as e:
                logger.warning(f"Failed to load holding data: {e}")
                return {}
        return {}
    
    def _save_holdings(self):
        """Save holding data to file"""
        try:
            with open(self.data_file, 'w', encoding='utf-8') as f:
                json.dump(self.holdings, f, ensure_ascii=False, indent=2)
        except Exception as e:
            logger.error(f"Failed to save holding data: {e}")
    
    def _parse_jsonp(self, text: str) -> Dict:
        """Parse JSONP formatted data"""
        try:
            start = text.find('{')
            end = text.rfind('}') + 1
            if start != -1 and end != -1:
                json_str = text[start:end]
                return json.loads(json_str)
            return {}
        except Exception as e:
            logger.error(f"JSONP parsing failed: {e}")
            return {}
    
    def _get_fund_nav_by_date(self, fund_code: str, date: str) -> Optional[float]:
        """
        Get fund NAV on a specific date
        
        Args:
            fund_code: Fund code
            date: Date in YYYY-MM-DD format
            
        Returns:
            NAV value or None
        """
        try:
            # Try to get historical NAV from TianTian Fund
            url = f"https://fund.eastmoney.com/pingzhongdata/{fund_code}.js"
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                text = response.text
                
                # Extract cumulative NAV trend data
                nav_match = re.search(r'Data_ACWorthTrend\s*=\s*(\[.*?\]);', text, re.S)
                if nav_match:
                    nav_data_str = nav_match.group(1)
                    try:
                        nav_data = json.loads(nav_data_str)
                        
                        # Convert target date to timestamp
                        target_date = datetime.strptime(date, '%Y-%m-%d')
                        target_timestamp = int(target_date.timestamp() * 1000)
                        
                        # Find matching NAV
                        for item in nav_data:
                            if isinstance(item, list) and len(item) >= 2:
                                timestamp = item[0]
                                if abs(timestamp - target_timestamp) < 86400000:  # Within 24 hours
                                    return float(item[1])
                        
                    except (json.JSONDecodeError, ValueError, IndexError) as e:
                        logger.warning(f"Failed to parse historical NAV data: {e}")
            
            # If historical data retrieval fails, try using current NAV as approximation
            realtime_info = self._get_fund_realtime_info(fund_code)
            if realtime_info and realtime_info['nav'] > 0:
                logger.warning(f"Cannot get NAV for {date}, using current NAV {realtime_info['nav']} as approximation")
                return realtime_info['nav']
                
        except Exception as e:
            logger.error(f"Failed to get NAV for fund {fund_code} on {date}: {e}")
        
        return None
    
    def _get_fund_realtime_info(self, fund_code: str) -> Dict:
        """Get fund real-time valuation info"""
        try:
            url = f"https://fundgz.1234567.com.cn/js/{fund_code}.js"
            response = self.session.get(url, timeout=10)
            
            if response.status_code == 200:
                data = self._parse_jsonp(response.text)
                if data and 'fundcode' in data:
                    return {
                        'fund_code': data.get('fundcode', fund_code),
                        'name': data.get('name', ''),
                        'nav_date': data.get('jzrq', ''),  # NAV date
                        'nav': float(data.get('dwjz', 0)),  # Yesterday's unit NAV
                        'estimate_value': float(data.get('gsz', 0)),  # Today's estimated NAV
                        'estimate_growth': float(data.get('gszzl', 0)),  # Today's estimated growth rate
                        'estimate_time': data.get('gztime', ''),  # Estimate time
                        'last_update': datetime.now().isoformat(),
                        'data_source': 'fundgz'
                    }
        except Exception as e:
            logger.error(f"Failed to get real-time data for fund {fund_code}: {e}")
        
        return None
    
    def set_holding(self, fund_code: str, current_value: float, value_date: str):
        """
        Set fund holding
        
        Args:
            fund_code: Fund code
            current_value: Current market value
            value_date: Date corresponding to the market value (YYYY-MM-DD)
        """
        # Check if shares need recalculation
        need_recalculate = True
        if fund_code in self.holdings:
            existing = self.holdings[fund_code]
            if (abs(existing['current_value'] - current_value) < 0.01 and 
                existing['value_date'] == value_date):
                need_recalculate = False
        
        if need_recalculate:
            # Get NAV on specified date
            nav_on_date = self._get_fund_nav_by_date(fund_code, value_date)
            if nav_on_date is None or nav_on_date <= 0:
                logger.error(f"Cannot get NAV for fund {fund_code} on {value_date}, cannot calculate shares")
                return
            
            # Calculate shares
            shares = current_value / nav_on_date
            
            # Save holding info
            self.holdings[fund_code] = {
                'fund_code': fund_code,
                'current_value': current_value,
                'value_date': value_date,
                'nav_on_date': nav_on_date,
                'shares': shares,
                'last_updated': datetime.now().isoformat()
            }
            self._save_holdings()
            logger.info(f"Shares for fund {fund_code} updated: {shares:.4f} shares")
        else:
            logger.info(f"Holding for fund {fund_code} unchanged, no need to recalculate shares")
    
    def set_multiple_holdings(self, holdings: List[Dict[str, any]]):
        """
        Set multiple fund holdings at once
        
        Args:
            holdings: List of holding dictionaries with keys:
                     - fund_code: Fund code
                     - current_value: Current market value
                     - value_date: Date corresponding to the market value (YYYY-MM-DD)
        """
        for holding in holdings:
            self.set_holding(
                holding['fund_code'], 
                holding['current_value'], 
                holding['value_date']
            )
    
    def get_realtime_profit(self, fund_code: str) -> Dict:
        """
        Get real-time profit estimation for a fund
        
        Args:
            fund_code: Fund code
            
        Returns:
            Dictionary with profit information
        """
        if fund_code not in self.holdings:
            return {
                'success': False,
                'error': f'Holding for fund {fund_code} not set, please call set_holding() first',
                'fund_code': fund_code
            }
        
        holding = self.holdings[fund_code]
        shares = holding['shares']
        
        # Get real-time info
        realtime_info = self._get_fund_realtime_info(fund_code)
        if not realtime_info or realtime_info['estimate_value'] <= 0:
            return {
                'success': False,
                'error': f'Cannot get real-time data for fund {fund_code}',
                'fund_code': fund_code
            }
        
        # Calculate current market value
        current_value = shares * realtime_info['estimate_value']
        
        # Calculate profit (compared to yesterday's NAV)
        yesterday_value = shares * realtime_info['nav']
        profit_amount = current_value - yesterday_value
        profit_rate = (realtime_info['estimate_value'] - realtime_info['nav']) / realtime_info['nav']
        
        return {
            'success': True,
            'fund_code': fund_code,
            'fund_name': realtime_info['name'],
            'shares': shares,
            'yesterday_nav': realtime_info['nav'],
            'today_estimate_nav': realtime_info['estimate_value'],
            'estimate_growth_pct': realtime_info['estimate_growth'],
            'profit_amount': profit_amount,
            'profit_rate': profit_rate,
            'current_value': current_value,
            'yesterday_value': yesterday_value,
            'nav_date': realtime_info['nav_date'],
            'estimate_time': realtime_info['estimate_time'],
            'original_holding': {
                'value': holding['current_value'],
                'date': holding['value_date'],
                'nav': holding['nav_on_date']
            }
        }
    
    def get_multiple_profits(self, fund_codes: List[str]) -> Dict[str, Dict]:
        """
        Get real-time profit estimations for multiple funds
        
        Args:
            fund_codes: List of fund codes
            
        Returns:
            Dictionary mapping fund codes to their profit information
        """
        results = {}
        for fund_code in fund_codes:
            results[fund_code] = self.get_realtime_profit(fund_code)
        return results
    
    def get_all_profits(self) -> Dict[str, Dict]:
        """Get real-time profit for all held funds"""
        results = {}
        for fund_code in self.holdings:
            results[fund_code] = self.get_realtime_profit(fund_code)
        return results


def test_share_manager():
    """Test the share manager"""
    manager = FundShareManager()
    
    # Set holding (assuming today is 2026-02-24, holding ¥10,000)
    manager.set_holding("000001", current_value=10000, value_date="2026-02-24")
    
    # Get real-time profit
    result = manager.get_realtime_profit("000001")
    
    print("=== Fund Share Manager Test ===")
    if result['success']:
        print(f"Fund: {result['fund_name']} ({result['fund_code']})")
        print(f"Holding shares: {result['shares']:.4f}")
        print(f"Yesterday's NAV: {result['yesterday_nav']:.4f}")
        print(f"Today's estimate: {result['today_estimate_nav']:.4f} ({result['estimate_growth_pct']:+.2f}%)")
        print(f"Real-time profit: ¥{result['profit_amount']:+.2f} ({result['profit_rate']:+.2%})")
        print(f"Current market value: ¥{result['current_value']:,.2f}")
        print(f"Original holding: ¥{result['original_holding']['value']:.2f} ({result['original_holding']['date']})")
    else:
        print(f"Calculation failed: {result['error']}")


def test_multiple_holdings():
    """Test multiple holdings functionality"""
    manager = FundShareManager()
    
    # Set multiple holdings at once
    holdings = [
        {"fund_code": "000001", "current_value": 10000, "value_date": "2026-02-24"},
        {"fund_code": "110022", "current_value": 5000, "value_date": "2026-02-24"},
        {"fund_code": "400015", "current_value": 8000, "value_date": "2026-02-24"}
    ]
    
    manager.set_multiple_holdings(holdings)
    
    # Get profits for multiple funds
    fund_codes = ["000001", "110022", "400015"]
    results = manager.get_multiple_profits(fund_codes)
    
    print("\n=== Multiple Holdings Test ===")
    total_profit = 0
    total_value = 0
    
    for fund_code, result in results.items():
        if result['success']:
            print(f"{result['fund_name']}: ¥{result['profit_amount']:+.2f} ({result['estimate_growth_pct']:+.2f}%)")
            total_profit += result['profit_amount']
            total_value += result['current_value']
        else:
            print(f"{fund_code}: Calculation failed - {result['error']}")
    
    print(f"\nTotal profit: ¥{total_profit:+.2f}")
    print(f"Total market value: ¥{total_value:,.2f}")


if __name__ == "__main__":
    test_share_manager()
    test_multiple_holdings()