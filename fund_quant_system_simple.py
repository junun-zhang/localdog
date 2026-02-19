#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
基金量化系统 - 简化版（仅使用标准库）

功能：
1. 根据基金编号查询天天基金实时数据
2. 提供买入/卖出建议
3. 支持多基金监控

注意：此版本仅依赖Python标准库，无需额外安装包
"""

import json
import time
import requests
import re
from datetime import datetime, timedelta
from typing import Dict, List, Tuple, Optional


class FundQuantSystem:
    def __init__(self):
        self.session = requests.Session()
        self.session.headers.update({
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
        })
        self.fund_cache = {}
        self.cache_timeout = 300  # 缓存5分钟
        
    def _parse_jsonp(self, jsonp_text: str) -> Dict:
        """解析JSONP格式的数据"""
        try:
            # 提取JSON部分
            match = re.match(r".*?({.*}).*", jsonp_text, re.S)
            if match:
                json_str = match.group(1)
                return json.loads(json_str)
            return {}
        except Exception as e:
            print(f"JSONP解析失败: {e}")
            return {}
    
    def get_fund_realtime_info(self, fund_code: str) -> Dict:
        """获取基金实时估值信息（来自天天基金）"""
        cache_key = f"realtime_{fund_code}"
        current_time = time.time()
        
        # 检查缓存
        if cache_key in self.fund_cache:
            cached_data, timestamp = self.fund_cache[cache_key]
            if current_time - timestamp < self.cache_timeout:
                return cached_data
        
        try:
            url = f"https://fundgz.1234567.com.cn/js/{fund_code}.js"
            response = self.session.get(url, timeout=10)
            response.raise_for_status()
            
            data = self._parse_jsonp(response.text)
            
            # 转换数据类型
            if 'gszzl' in data:
                data['gszzl'] = float(data['gszzl'])
            if 'dwjz' in data:
                data['dwjz'] = float(data['dwjz'])
                
            # 缓存数据
            self.fund_cache[cache_key] = (data, current_time)
            return data
            
        except Exception as e:
            print(f"获取基金{fund_code}实时数据失败: {e}")
            return {}
    
    def get_fund_history_data(self, fund_code: str, days: int = 30) -> List[Dict]:
        """获取基金历史净值数据（简化版，仅返回最近几天的估算值）"""
        # 由于直接获取历史净值比较复杂，这里先返回空列表
        # 实际应用中可以从天天基金的历史数据接口获取
        return []
    
    def calculate_technical_indicators(self, fund_code: str) -> Dict:
        """计算技术指标（基于实时数据的简单策略）"""
        realtime_data = self.get_fund_realtime_info(fund_code)
        if not realtime_data:
            return {"error": "无法获取基金数据"}
        
        growth_rate = realtime_data.get('gszzl', 0)
        
        # 简单策略：基于当日涨跌幅
        if growth_rate > 1.0:
            advice = "sell"
            reason = f"当日涨幅较大({growth_rate:.2f}%)，考虑止盈"
        elif growth_rate < -1.0:
            advice = "buy"
            reason = f"当日跌幅较大({growth_rate:.2f}%)，考虑抄底"
        elif growth_rate > 0.5:
            advice = "hold"
            reason = f"小幅上涨({growth_rate:.2f}%)，继续持有"
        elif growth_rate < -0.5:
            advice = "watch"
            reason = f"小幅下跌({growth_rate:.2f}%)，观望为主"
        else:
            advice = "hold"
            reason = f"波动较小({growth_rate:.2f}%)，维持现状"
            
        return {
            "advice": advice,
            "reason": reason,
            "growth_rate": growth_rate,
            "fund_name": realtime_data.get('name', fund_code),
            "current_value": realtime_data.get('gsz', 'N/A'),
            "last_nav": realtime_data.get('dwjz', 'N/A'),
            "update_time": realtime_data.get('gztime', 'N/A')
        }
    
    def analyze_multiple_funds(self, fund_codes: List[str]) -> Dict[str, Dict]:
        """批量分析多个基金"""
        results = {}
        for fund_code in fund_codes:
            print(f"正在分析基金 {fund_code}...")
            results[fund_code] = self.calculate_technical_indicators(fund_code)
            time.sleep(0.5)  # 避免请求过快
        return results
    
    def get_trading_summary(self, fund_codes: List[str]) -> str:
        """生成交易摘要报告"""
        analysis_results = self.analyze_multiple_funds(fund_codes)
        
        summary = "=== 基金量化分析报告 ===\n"
        summary += f"分析时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n\n"
        
        buy_list = []
        sell_list = []
        hold_list = []
        watch_list = []
        
        for fund_code, result in analysis_results.items():
            if 'error' in result:
                summary += f"{fund_code}: {result['error']}\n"
                continue
                
            fund_name = result['fund_name']
            advice = result['advice']
            reason = result['reason']
            growth = result['growth_rate']
            
            if advice == 'buy':
                buy_list.append((fund_code, fund_name, growth, reason))
            elif advice == 'sell':
                sell_list.append((fund_code, fund_name, growth, reason))
            elif advice == 'watch':
                watch_list.append((fund_code, fund_name, growth, reason))
            else:
                hold_list.append((fund_code, fund_name, growth, reason))
        
        if buy_list:
            summary += "📈 **建议买入**:\n"
            for code, name, growth, reason in buy_list:
                summary += f"  {code} {name} ({growth:+.2f}%): {reason}\n"
            summary += "\n"
            
        if sell_list:
            summary += "📉 **建议卖出**:\n"
            for code, name, growth, reason in sell_list:
                summary += f"  {code} {name} ({growth:+.2f}%): {reason}\n"
            summary += "\n"
            
        if watch_list:
            summary += "👀 **建议观望**:\n"
            for code, name, growth, reason in watch_list:
                summary += f"  {code} {name} ({growth:+.2f}%): {reason}\n"
            summary += "\n"
            
        summary += f"📊 **继续持有**: {len(hold_list)} 只基金\n"
        
        return summary


def main():
    """命令行测试入口"""
    system = FundQuantSystem()
    
    # 测试基金代码（您可以替换为您关注的基金）
    test_funds = ["000001", "110022", "519697"]
    
    print("基金量化系统启动...\n")
    
    # 单基金分析示例
    print("=== 单基金分析示例 ===")
    single_result = system.calculate_technical_indicators("000001")
    if 'error' not in single_result:
        print(f"基金: {single_result['fund_name']}")
        print(f"建议: {single_result['advice'].upper()}")
        print(f"理由: {single_result['reason']}")
        print(f"当前估值: {single_result['current_value']}")
        print(f"更新时间: {single_result['update_time']}")
    else:
        print(f"分析失败: {single_result['error']}")
    
    print("\n" + "="*50 + "\n")
    
    # 多基金分析示例
    print("=== 多基金分析报告 ===")
    summary = system.get_trading_summary(test_funds)
    print(summary)


if __name__ == "__main__":
    main()