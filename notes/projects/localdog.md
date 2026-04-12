# localdog 项目笔记

**创建时间**: 2026-04-09
**标签**: #android #gradle #git #项目
**仓库**: https://github.com/junun-zhang/localdog.git

---

## 📋 项目概述

Android 项目，使用 Gradle 构建系统。

---

## 🛠️ 技术栈

- **语言**: Kotlin/Java
- **构建工具**: Gradle 8.14.4 / 8.4
- **IDE**: Android Studio

---

## 📁 项目结构

```
localdog/
├── app/              # 主应用模块
├── gradle/           # Gradle 配置和包装器
├── build.gradle      # 项目级构建配置
├── settings.gradle   # 项目设置
└── .gitignore        # Git 忽略配置
```

---

## ⚠️ 已解决的问题

### 1. Git 推送大文件问题

**问题**: Gradle 缓存文件超过 GitHub 100MB 限制

**解决方案**:
1. 创建 `.gitignore` 排除 `gradle/caches/`, `.gradle/`, `build/`
2. 使用 `git filter-branch` 清理历史
3. 强制推送 `git push --force`

**详细笔记**: [[Git-大文件推送解决方案]]

---

## 🔧 构建命令

```bash
# 清理构建
./gradlew clean

# 构建 Debug 版本
./gradlew assembleDebug

# 构建 Release 版本
./gradlew assembleRelease

# 运行测试
./gradlew test

# 查看依赖
./gradlew dependencies
```

---

## 📝 待办事项

- [ ] 配置 CI/CD
- [ ] 添加单元测试
- [ ] 优化 APK 大小
- [ ] 完善文档

---

## 🔗 相关资源

- [Gradle 官方文档](https://docs.gradle.org/)
- [Android 开发者文档](https://developer.android.com/)
- [Git 大文件解决方案](./Git-大文件推送解决方案.md)

---

*最后更新：2026-04-09*
