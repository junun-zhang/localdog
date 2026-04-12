# 📚 我的笔记 Dashboard

**最后更新**: 2026-04-09

---

## 🔢 快速统计

| 类型 | 数量 | 路径 |
|------|------|------|
| 📅 每日笔记 | `find daily -name "*.md" | wc -l` 篇 | `notes/daily/` |
| 📁 项目笔记 | `find projects -name "*.md" | wc -l` 篇 | `notes/projects/` |
| 🏷️ 主题笔记 | `find topics -name "*.md" | wc -l` 篇 | `notes/topics/` |
| 📝 模板 | `find templates -name "*.md" | wc -l` 个 | `notes/templates/` |

---

## 📅 最近 7 天的笔记

```bash
# 自动列出最近的笔记
ls -lt daily/*.md 2>/dev/null | head -7
```

---

## 🔍 快速搜索

### 按关键词搜索
```bash
grep -r "关键词" ~/notes/ --include="*.md"
```

### 按标签搜索
```bash
grep -r "#标签名" ~/notes/ --include="*.md"
```

### 按日期查找
```bash
ls daily/2026-*.md
```

---

## 📂 目录结构

```
notes/
├── daily/          # 每日笔记 (YYYY-MM-DD.md)
├── projects/       # 项目笔记 (项目名.md)
├── topics/         # 主题笔记 (主题名.md)
├── templates/      # 笔记模板
└── README.md       # 本文件
```

---

## 🏷️ 标签系统

使用 `#标签` 格式标记笔记：

- `#git` - Git 相关
- `#android` - Android 开发
- `#learn` - 学习笔记
- `#todo` - 待办事项
- `#idea` - 想法灵感

---

## 🚀 常用命令

| 命令 | 说明 |
|------|------|
| `note-new daily "标题"` | 创建新笔记 |
| `note-search "关键词"` | 搜索笔记 |
| `note-list` | 列出所有笔记 |
| `note-today` | 打开今日笔记 |

---

## 📝 快速开始

1. **写每日笔记**: `notes/daily/2026-04-09.md`
2. **写项目笔记**: `notes/projects/项目名.md`
3. **搜索内容**: `grep -r "关键词" ~/notes/`

---

*Powered by Markdown + Bash*
