# 基金量化系统

这是一个轻量级的基金量化分析系统，直接调用天天基金API获取实时数据。

## 功能特性

1. **实时估值查询** - 根据基金编号获取实时估值和涨跌幅
2. **交易建议** - 基于简单策略提供买入/卖出/持有建议  
3. **多基金监控** - 支持同时分析多个基金
4. **轻量级设计** - 仅依赖Python标准库，无需额外安装包

## 系统架构

```
fund_quant_system_simple.py
├── FundQuantSystem 类
│   ├── get_fund_realtime_data() - 获取基金实时数据（天天基金API）
│   ├── calculate_trading_advice() - 生成交易建议
│   ├── analyze_single_fund() - 分析单只基金
│   └── analyze_multiple_funds() - 批量分析多只基金
└── main() - 测试入口
```

## 使用说明

### 1. 直接运行测试
```bash
python fund_quant_system_simple.py
```

### 2. 集成到您的项目
```python
from fund_quant_system_simple import FundQuantSystem

system = FundQuantSystem()
result = system.analyze_single_fund("000001")
print(f"建议: {result['advice']}")
print(f"理由: {result['reason']}")
```

### 3. 自定义基金列表
修改 `main()` 函数中的 `fund_list` 变量：
```python
fund_list = ["000001", "110022", "519697"]  # 替换为您关注的基金代码
```

## 数据源

- **天天基金API**: `https://fundgz.1234567.com.cn/js/{基金代码}.js`
- **数据字段**: 基金代码、基金名称、单位净值、估算净值、估算增长率、更新时间

## 交易策略

当前使用简单的波动率策略：
- **买入条件**: 估算增长率 > 1.5%
- **卖出条件**: 估算增长率 < -1.5%  
- **持有条件**: 波动在 -1.5% 到 +1.5% 之间

## 注意事项

⚠️ **重要提醒**：
- 此系统使用非官方API，稳定性无法保证
- 估算数据仅供参考，不构成投资建议
- 实际交易前请核实最新净值和市场情况
- 投资有风险，决策需谨慎

## 扩展方向

1. **添加更多策略**: RSI、MACD等技术指标
2. **历史数据分析**: 获取历史净值进行回测
3. **通知系统**: 集成QQ/邮件提醒
4. **Web界面**: 提供可视化界面
5. **定时任务**: 自动监控和提醒

## 文件结构

- `fund_quant_system_simple.py` - 核心逻辑（仅依赖标准库）
- `requirements.txt` - 无依赖（空文件）
- `README.md` - 说明文档
- `config.py` - 配置文件（可选）