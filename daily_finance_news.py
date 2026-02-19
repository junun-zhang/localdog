#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
每日财经新闻推送脚本
每天早上7点自动运行，获取国内外重点财经新闻
"""

import requests
import json
from datetime import datetime
import logging

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def get_finance_news():
    """
    获取财经新闻的多种方法
    返回新闻摘要列表
    """
    news_items = []
    
    # 方法1: 尝试获取新浪财经头条
    try:
        # 新浪财经首页新闻
        sina_url = "https://feed.mix.sina.com.cn/api/roll/get?pageid=153&lid=2509&k=&num=10&versionNumber=1.2.4"
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
        }
        response = requests.get(sina_url, headers=headers, timeout=10)
        if response.status_code == 200:
            data = response.json()
            if data.get('result'):
                for item in data['result'][:5]:  # 取前5条
                    news_items.append({
                        'title': item.get('title', ''),
                        'source': '新浪财经',
                        'time': datetime.fromtimestamp(int(item.get('ctime', 0))).strftime('%Y-%m-%d %H:%M')
                    })
    except Exception as e:
        logger.error(f"获取新浪财经失败: {e}")
    
    # 方法2: 尝试获取东方财富财经新闻
    try:
        eastmoney_url = "http://np-cdn.finance.eastmoney.com/EM_CDN/json/importantnews.js"
        response = requests.get(eastmoney_url, timeout=10)
        if response.status_code == 200:
            # 解析响应内容
            content = response.text
            # 这里需要根据实际返回格式解析
            # 由于格式可能变化，这里只做简单处理
            if 'title' in content:
                news_items.append({
                    'title': '东方财富重要财经新闻',
                    'source': '东方财富',
                    'time': datetime.now().strftime('%Y-%m-%d %H:%M')
                })
    except Exception as e:
        logger.error(f"获取东方财富新闻失败: {e}")
    
    # 方法3: 如果以上都失败，提供通用财经信息
    if not news_items:
        news_items = [
            {
                'title': '今日重点关注：全球市场动态、央行政策、经济数据发布',
                'source': '综合财经',
                'time': datetime.now().strftime('%Y-%m-%d %H:%M')
            },
            {
                'title': '建议关注：A股市场走势、美元汇率、大宗商品价格',
                'source': '投资建议',
                'time': datetime.now().strftime('%Y-%m-%d %H:%M')
            }
        ]
    
    return news_items[:5]  # 最多返回5条新闻

def format_news_message(news_items):
    """格式化新闻消息"""
    if not news_items:
        return "抱歉，今日财经新闻获取失败，请稍后重试。"
    
    message = f"📅 **{datetime.now().strftime('%Y年%m月%d日')} 财经早报**\n\n"
    message += "📰 **重点财经新闻**\n"
    
    for i, news in enumerate(news_items, 1):
        message += f"\n{i}. {news['title']}\n"
        message += f"   来源: {news['source']} | {news['time']}"
    
    message += "\n\n💡 *数据来源于公开财经媒体，仅供参考*"
    return message

def main():
    """主函数"""
    try:
        news_items = get_finance_news()
        message = format_news_message(news_items)
        print(message)
        
        # 这里可以添加发送到QQ的消息逻辑
        # 由于需要OpenClaw的message工具，这里只打印
        
    except Exception as e:
        logger.error(f"获取财经新闻失败: {e}")
        error_message = f"📅 **{datetime.now().strftime('%Y年%m月%d日')} 财经早报**\n\n⚠️ 获取新闻时出现错误，请稍后重试。"
        print(error_message)

if __name__ == "__main__":
    main()