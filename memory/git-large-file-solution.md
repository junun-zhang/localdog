# Git 大文件推送问题解决方案

**创建时间**: 2026-04-09  
**标签**: #git #github #gradle #android #问题解决

---

## 📌 问题描述

向 GitHub 推送 Android 项目时失败，错误信息：

```
remote: error: File gradle/caches/xxx.jar is 191.28 MB; 
this exceeds GitHub's file size limit of 100.00 MB
remote: error: GH001: Large files detected.
! [remote rejected] iread -> iread (pre-receive hook declined)
```

---

## 🔍 根本原因

- **Gradle 缓存文件** (`gradle/caches/`) 被提交到 Git
- 这些是**本地构建产物**，包含编译缓存、依赖 JAR
- GitHub 限制单个文件 ≤100MB
- 此类文件**不应该**进入版本控制

---

## ✅ 完整解决方案

### 步骤 1: 创建 .gitignore

```bash
cat > .gitignore << 'EOF'
# Gradle 缓存
.gradle/
gradle/caches/
gradle/wrapper/dists/
build/
*/build/
app/build/

# 编译产物
*.class
*.jar
!gradle/wrapper/gradle-wrapper.jar
*.apk
*.aab

# IDE
.idea/
*.iml
*.ipr
*.iws
.vscode/

# 本地配置
local.properties

# 日志
*.log
EOF
```

### 步骤 2: 暂存当前修改

```bash
git add -A
git commit -m "保存当前状态"
```

### 步骤 3: 从 Git 历史中移除大文件

```bash
git filter-branch --force --index-filter \
  'git rm -rf --cached --ignore-unmatch gradle/caches .gradle build' \
  --prune-empty HEAD
```

### 步骤 4: 清理备份引用

```bash
git for-each-ref --format="%(refname)" refs/original/ | xargs -n 1 git update-ref -d
```

### 步骤 5: 垃圾回收

```bash
git gc --prune=now --aggressive
```

### 步骤 6: 验证大文件已移除

```bash
git rev-list --objects --all | grep "$(git verify-pack -v .git/objects/pack/*.idx | sort -k 3 -n | tail -5 | awk '{print $1}')"
# 无输出 = 成功 ✅
```

### 步骤 7: 强制推送

```bash
git push origin iread --force
```

---

## ⚠️ 注意事项

| 要点 | 说明 |
|------|------|
| 工作区必须干净 | `filter-branch` 要求没有未暂存的修改 |
| 必须强制推送 | 修改历史后需要用 `--force` |
| 通知协作者 | 多人协作时，他们需要重新克隆 |
| 提前配置 .gitignore | 防止未来再次提交大文件 |
| 验证后再推送 | 确保大文件真的被移除了 |

---

## 🛠️ 常用诊断命令

```bash
# 查看仓库中最大的 5 个文件
git rev-list --objects --all | grep "$(git verify-pack -v .git/objects/pack/*.idx | sort -k 3 -n | tail -5 | awk '{print $1}')"

# 查看仓库大小
git count-objects -vH

# 查看哪些文件最大
git rev-list --objects --all | sort -k 2 | tail -20

# 查看当前 git 状态
git status

# 查看提交历史
git log --oneline -10
```

---

## 🔄 替代方案

### 使用 BFG Repo-Cleaner（更快）

```bash
# 下载 BFG
wget https://repo1.maven.org/maven2/com/madgag/bfg/1.14.0/bfg-1.14.0.jar

# 运行清理
java -jar bfg-1.14.0.jar --delete-files 'gradle/caches/**/*' --no-blob-protection
```

### 使用 git-filter-repo（推荐）

```bash
# 安装
pip install git-filter-repo

# 清理
git filter-repo --path gradle/caches/ --invert-paths
git filter-repo --path .gradle/ --invert-paths
git filter-repo --path build/ --invert-paths

# 推送
git push origin iread --force
```

---

## 📋 Android 项目推荐 .gitignore

```gitignore
# 编译产物
*.class
*.jar
!gradle/wrapper/gradle-wrapper.jar
*.apk
*.aab
*.ap_

# Gradle
.gradle/
build/
*/build/
gradle/caches/
gradle/wrapper/dists/

# IDE
.idea/
*.iml
*.ipr
*.iws
.vscode/
*.swp
*.swo
*~

# 本地配置
local.properties
gradle-local.properties

# 日志
*.log

# 系统文件
.DS_Store
Thumbs.db
```

---

## 💡 核心原则

> **构建产物和缓存文件永远不应该进入版本控制！**

应该提交的：
- ✅ 源代码
- ✅ 构建脚本 (build.gradle, settings.gradle)
- ✅ 资源文件
- ✅ 配置文件模板

不应该提交的：
- ❌ 编译输出 (build/, *.class, *.apk)
- ❌ 依赖缓存 (gradle/caches/, .gradle/)
- ❌ IDE 配置 (.idea/, *.iml)
- ❌ 本地配置 (local.properties)

---

## 📚 相关资源

- [Git LFS 官方文档](https://git-lfs.github.com/)
- [BFG Repo-Cleaner](https://rtyley.github.io/bfg-repo-cleaner/)
- [git-filter-repo](https://github.com/newren/git-filter-repo)
- [GitHub 文件限制说明](https://docs.github.com/en/repositories/working-with-files/managing-large-files)

---

**状态**: ✅ 已解决  
**最后更新**: 2026-04-09
