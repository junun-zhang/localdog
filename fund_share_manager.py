#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
基金份额管理器

功能：
1. 根据用户提供的市值和日期，自动计算并保存基金份额
2. 使用保存的份额进行实时收益预估
3. 只在市值或日期更新时重新计算份额
4. 持久化存储份额信息到本地文件

使用方法：
from fund_share_manager import FundShareManager

manager = FundShareManager()

# 首次设置持仓（会计算并保存份额）
manager.set_holding("000001", current_value=10000, value_date="2026-02-24")

# 获取实时收益预估（使用保存的份额）
result = manager.get_realtime_profit("000001")
"""

import os
import json
import re
import requests
import logging
from datetime import datetime
from typing import Dict, Optional

# 配置日志
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
        """加载持仓数据"""
        if os.path.exists(self.data_file):
            try:
                with open(self.data_file, 'r', encoding='utf-8') as f:
                    return json.load(f)
            except Exception as e:
                logger.warning(f"加载持仓数据失败: {e}")
                return {}
        return {}
    
    def _save_holdings(self):
        """保存持仓数据"""
        try:
            with open(self.data_file, 'w', encoding='utf-8') as f:
                json.dump(self.holdings, f, ensure_ascii=False, indent=2)
        except Exception as e:
            logger.error(f"保存持仓数据失败: {e}")
    
    def _parse_jsonp(self, text: str) -> Dict:
        """解析JSONP格式的数据"""
        try:
            start = text.find('{')
            end = text.rfind('}') + 1
            if start != -1 and end != -1:
                json_str = text[start:end]
                return json.loads(json_str)
            return {}
        except Exception as e:
            logger.error(f"JSONP解析失败: {e}")
            return {}
    
    def _get_fund_nav_by_date(self, fund_code: str, date: str) -> Optional[float]:
        """
        获取基金在指定日期的净值
        
        Args:
            fund_code: 基金代码
            date: 日期，格式 YYYY-MM-DD
            
        Returns:
            净值 or None
        """
        try:
            # 尝试从天天基金历史净值获取
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
                        
                        # 转换目标日期为时间戳
                        target_date = datetime.strptime(date, '%Y-%m-%d')
                        target_timestamp = int(target_date.timestamp() * 1000)
                        
                        # 查找匹配的净值
                        for item in nav_data:
                            if isinstance(item, list) and len(item) >= 2:
                                timestamp = item[0]
                                if abs(timestamp - target_timestamp) < 86400000:  # 24小时内
                                    return float(item[1])
                        
                    except (json.JSONDecodeError, ValueError, IndexError) as e:
                        logger.warning(f"解析历史净值数据失败: {e}")
            
            # 如果历史数据获取失败，尝试使用当前净值作为近似
            realtime_info = self._get_fund_realtime_info(fund_code)
            if realtime_info and realtime_info['nav'] > 0:
                logger.warning(f"无法获取{date}的净值，使用当前净值{realtime_info['nav']}作为近似")
                return realtime_info['nav']
                
        except Exception as e:
            logger.error(f"获取基金{fund_code}在{date}的净值失败: {e}")
        
        return None
    
    def _get_fund_realtime_info(self, fund_code: str) -> Dict:
        """获取基金实时估值信息"""
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
                        'nav': float(data.get('dwjz', 0)),  # 昨日单位净值
                        'estimate_value': float(data.get('gsz', 0)),  # 今日估算净值
                        'estimate_growth': float(data.get('gszzl', 0)),  # 今日估算增长率
                        'estimate_time': data.get('gztime', ''),  # 估算时间
                        'last_update': datetime.now().isoformat(),
                        'data_source': 'fundgz'
                    }
        except Exception as e:
            logger.error(f"获取基金{fund_code}实时数据失败: {e}")
        
        return None
    
    def set_holding(self, fund_code: str, current_value: float, value_date: str):
        """
        设置基金持仓
        
        Args:
            fund_code: 基金代码
            current_value: 当前市值
            value_date: 市值对应的日期 (YYYY-MM-DD)
        """
        # 检查是否需要重新计算份额
        need_recalculate = True
        if fund_code in self.holdings:
            existing = self.holdings[fund_code]
            if (abs(existing['current_value'] - current_value) < 0.01 and 
                existing['value_date'] == value_date):
                need_recalculate = False
        
        if need_recalculate:
            # 获取指定日期的净值
            nav_on_date = self._get_fund_nav_by_date(fund_code, value_date)
            if nav_on_date is None or nav_on_date <= 0:
                logger.error(f"无法获取基金{fund_code}在{value_date}的净值，无法计算份额")
                return
            
            # 计算份额
            shares = current_value / nav_on_date
            
            # 保存持仓信息
            self.holdings[fund_code] = {
                'fund_code': fund_code,
                'current_value': current_value,
                'value_date': value_date,
                'nav_on_date': nav_on_date,
                'shares': shares,
                'last_updated': datetime.now().isoformat()
            }
            self._save_holdings()
            logger.info(f"基金{fund_code}份额已更新: {shares:.4f}份")
        else:
            logger.info(f"基金{fund_code}持仓未变化，无需重新计算份额")
    
    def get_realtime_profit(self, fund_code: str) -> Dict:
        """
        获取基金实时收益预估
        
        Args:
            fund_code: 基金代码
            
        Returns:
            收益信息字典
        """
        if fund_code not in self.holdings:
            return {
                'success': False,
                'error': f'基金{fund_code}未设置持仓，请先调用set_holding()',
                'fund_code': fund_code
            }
        
        holding = self.holdings[fund_code]
        shares = holding['shares']
        
        # 获取实时信息
        realtime_info = self._get_fund_realtime_info(fund_code)
        if not realtime_info or realtime_info['estimate_value'] <= 0:
            return {
                'success': False,
                'error': f'无法获取基金{fund_code}实时数据',
                'fund_code': fund_code
            }
        
        # 计算当前市值
        current_value = shares * realtime_info['estimate_value']
        
        # 计算收益（相对于昨日净值）
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
    
    def get_all_profits(self) -> Dict[str, Dict]:
        """获取所有持仓基金的实时收益"""
        results = {}
        for fund_code in self.holdings:
            results[fund_code] = self.get_realtime_profit(fund_code)
        return results


def test_share_manager():
    """测试份额管理器"""
    manager = FundShareManager()
    
    # 设置持仓（假设今天是2026-02-24，持有10000元）
    manager.set_holding("000001", current_value=10000, value_date="2026-02-24")
    
    # 获取实时收益
    result = manager.get_realtime_profit("000001")
    
    print("=== 基金份额管理器测试 ===")
    if result['success']:
        print(f"基金: {result['fund_name']} ({result['fund_code']})")
        print(f"持有份额: {result['shares']:.4f}份")
        print(f"昨日净值: {result['yesterday_nav']:.4f}")
        print(f"今日估算: {result['today_estimate_nav']:.4f} ({result['estimate_growth_pct']:+.2f}%)")
        print(f"实时收益: ¥{result['profit_amount']:+.2f} ({result['profit_rate']:+.2%})")
        print(f"当前市值: ¥{result['current_value']:,.2f}")
        print(f"原始持仓: ¥{result['original_holding']['value']:.2f} ({result['original_holding']['date']})")
    else:
        print(f"计算失败: {result['error']}")


if __name__ == "__main__":
    test_share_manager()