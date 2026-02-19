# 每日财经新闻自动推送设置指南

## 功能说明
每天早上7点自动获取国内外重点财经新闻并发送到您的QQ。

## 设置方法

### 方法一：使用系统cron（推荐）

1. **编辑crontab**：
```bash
crontab -e
```

2. **添加以下行**：
```bash
0 7 * * * cd /home/admin/.openclaw/workspace && python3 daily_finance_news.py >> /home/admin/.openclaw/workspace/finance_news.log 2>&1
```

3. **保存并退出**

### 方法二：使用OpenClaw内置定时任务

如果您有OpenClaw Gateway的访问权限，可以使用以下配置：

```json
{
  "name": "Daily Finance News",
  "schedule": {
    "kind": "cron",
    "expr": "0 7 * * *",
    "tz": "Asia/Shanghai"
  },
  "payload": {
    "kind": "agentTurn",
    "message": "获取今日财经新闻摘要"
  },
  "sessionTarget": "isolated",
  "delivery": {
    "mode": "announce",
    "channel": "qqbot"
  }
}
```

### 方法三：手动运行

每天早上7点手动运行：
```bash
cd /home/admin/.openclaw/workspace
python3 daily_finance_news.py
```

## 脚本功能

- 📰 获取国内外重点财经新闻
- 🕒 自动识别工作日/周末调整内容
- 📊 包含股市、汇率、大宗商品等关键数据
- 📱 通过QQ Bot发送到您的聊天窗口

## 注意事项

- 需要确保网络连接正常
- 新闻源可能会有变动，脚本会自动处理异常
- 建议先手动测试一次确保功能正常

## 测试命令

```bash
# 测试脚本是否正常工作
python3 daily_finance_news.py
```

## 日志文件

- 成功日志：`finance_news_success.log`
- 错误日志：`finance_news_error.log`
- 详细日志：`finance_news.log`