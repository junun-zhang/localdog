#!/bin/bash
# 笔记管理工具

NOTES_DIR="$HOME/.openclaw/workspace/notes"

case "$1" in
    new)
        # 创建新笔记
        type="$2"
        name="$3"
        if [ -z "$type" ] || [ -z "$name" ]; then
            echo "用法：note new <类型> <文件名>"
            echo "类型：daily, projects, topics"
            exit 1
        fi
        file="$NOTES_DIR/$type/${name}.md"
        if [ -f "$file" ]; then
            echo "⚠️  文件已存在：$file"
            exit 1
        fi
        cat > "$file" << EOF
# ${name}

**创建时间**: $(date '+%Y-%m-%d %H:%M')
**标签**: 

---

## 内容


EOF
        echo "✅ 已创建：$file"
        ${EDITOR:-nano} "$file"
        ;;
    
    today)
        # 打开今日笔记
        today_file="$NOTES_DIR/daily/$(date '+%Y-%m-%d').md"
        if [ ! -f "$today_file" ]; then
            cat > "$today_file" << EOF
# $(date '+%Y年%m月%d日') 笔记

**日期**: $(date '+%Y-%m-%d')
**星期**: $(date '+%A')

---

## 📋 今日待办

- [ ] 

---

## 📝 今日记录

### 工作


### 学习


### 其他

---

## 💡 想法与灵感


---

## 📚参考资料

EOF
        fi
        echo "📖 打开今日笔记：$today_file"
        ${EDITOR:-nano} "$today_file"
        ;;
    
    search)
        # 搜索笔记
        if [ -z "$2" ]; then
            echo "用法：note search <关键词>"
            exit 1
        fi
        echo "🔍 搜索：$2"
        echo "---"
        grep -rn "$2" "$NOTES_DIR" --include="*.md" --color=always
        ;;
    
    list)
        # 列出所有笔记
        echo "📁 每日笔记:"
        ls -1 "$NOTES_DIR/daily/"*.md 2>/dev/null | wc -l | xargs -I {} echo "   {} 篇"
        ls -lt "$NOTES_DIR/daily/"*.md 2>/dev/null | head -5 | awk '{print "   " $NF}' | sed 's|.*/||'
        
        echo ""
        echo "📁 项目笔记:"
        ls -1 "$NOTES_DIR/projects/"*.md 2>/dev/null | wc -l | xargs -I {} echo "   {} 篇"
        ls "$NOTES_DIR/projects/"*.md 2>/dev/null | sed 's|.*/||' | sed 's|\.md$||' | head -5 | xargs -I {} echo "   {}"
        
        echo ""
        echo "📁 主题笔记:"
        ls -1 "$NOTES_DIR/topics/"*.md 2>/dev/null | wc -l | xargs -I {} echo "   {} 篇"
        ls "$NOTES_DIR/topics/"*.md 2>/dev/null | sed 's|.*/||' | sed 's|\.md$||' | head -5 | xargs -I {} echo "   {}"
        ;;
    
    stats)
        # 统计信息
        echo "📊 笔记统计"
        echo "---"
        echo "每日笔记：$(ls -1 "$NOTES_DIR/daily/"*.md 2>/dev/null | wc -l) 篇"
        echo "项目笔记：$(ls -1 "$NOTES_DIR/projects/"*.md 2>/dev/null | wc -l) 篇"
        echo "主题笔记：$(ls -1 "$NOTES_DIR/topics/"*.md 2>/dev/null | wc -l) 篇"
        echo "---"
        echo "总计：$(($(ls -1 "$NOTES_DIR/daily/"*.md 2>/dev/null | wc -l) + $(ls -1 "$NOTES_DIR/projects/"*.md 2>/dev/null | wc -l) + $(ls -1 "$NOTES_DIR/topics/"*.md 2>/dev/null | wc -l))) 篇"
        ;;
    
    dashboard)
        # 打开 Dashboard
        ${EDITOR:-nano} "$NOTES_DIR/README.md"
        ;;
    
    *)
        echo "📝 笔记管理工具"
        echo ""
        echo "用法：note <命令> [参数]"
        echo ""
        echo "命令:"
        echo "  new <类型> <文件名>   创建新笔记 (类型：daily/projects/topics)"
        echo "  today                 打开/创建今日笔记"
        echo "  search <关键词>       搜索笔记"
        echo "  list                  列出所有笔记"
        echo "  stats                 显示统计"
        echo "  dashboard             打开 Dashboard"
        echo ""
        echo "示例:"
        echo "  note new projects my-project"
        echo "  note today"
        echo "  note search git"
        echo "  note list"
        ;;
esac
