# 基金量化系统使用说明

## 系统特点

- **轻量级**: 仅依赖Python标准库，无需额外安装包
- **真实数据**: 直接调用天天基金API获取实时数据
- **简单策略**: 基于当日涨跌幅的简单交易建议
- **易于扩展**: 模块化设计，方便添加新功能

## 核心功能

### 1. 单基金分析
```python
from fund_quant_system_simple import FundQuantSystem

system = FundQuantSystem()
result = system.analyze_fund("000001")
print(f"建议: {result['advice']}")
print(f"理由: {result['reason']}")
```

### 2. 多基金批量分析
```python
fund_codes = ["000001", "110022", "519697"]
report = system.generate_batch_report(fund_codes)
print(report)
```

### 3. 自定义监控列表
修改 `config.py` 中的 `WATCHLIST` 列表：
```python
WATCHLIST = [
    {"code": "000001", "name": "华夏成长混合"},
    {"code": "110022", "name": "易方达消费行业股票"},
    {"code": "519697", "name": "交银优势行业混合"}
]
```

## API接口说明

### analyze_fund(fund_code)
**参数**: 
- `fund_code` (str): 6位基金代码

**返回**: 
```json
{
    "fund_code": "000001",
    "fund_name": "华夏成长混合",
    "current_nav": 1.1484,
    "growth_rate": -0.14,
    "update_time": "2026-02-13 15:00",
    "advice": "HOLD",
    "reason": "波动较小(-0.14%)，维持现状"
}
```

### generate_batch_report(fund_list)
**参数**: 
- `fund_list` (list): 基金代码列表

**返回**: 完整的分析报告字符串

## 策略逻辑

当前使用简单的涨跌幅策略：
- **买入**: 当日涨幅 > 1%
- **卖出**: 当日跌幅 > 1%  
- **持有**: 涨跌幅在 [-1%, 1%] 区间内

## 扩展建议

### 1. 添加更多技术指标
- 移动平均线
- RSI相对强弱指数
- MACD指标

### 2. 风险控制
- 最大回撤监控
- 波动率分析
- 相关性分析

### 3. 通知系统
- QQ消息提醒（已支持）
- 邮件通知
- 微信推送

### 4. 定时任务
集成cron实现自动监控：
```bash
# 每天15:30检查基金状态
30 15 * * 1-5 python /path/to/fund_quant_system_simple.py
```

## 注意事项

⚠️ **重要提醒**:
- 此系统提供的是**参考建议**，不构成投资建议
- 实际投资决策请结合个人风险承受能力和投资目标
- 基金数据来源于天天基金，可能存在延迟
- 交易日15:00后才能获取当日完整净值数据

## 故障排除

### 1. 获取数据失败
- 检查网络连接
- 确认基金代码正确（6位数字）
- 天天基金API可能临时不可用

### 2. 返回空数据
- 基金可能已清盘或暂停交易
- 非交易日可能无数据更新

### 3. 系统运行缓慢
- 减少同时分析的基金数量
- 增加请求间隔时间

## 贡献和反馈

如果您有改进建议或发现问题，请：
1. 检查 `TO_IMPLEMENT.md` 文件
2. 提交具体的改进需求
3. 测试新功能并提供反馈