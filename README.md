# IReader - 类似掌阅的安卓阅读应用

一个功能完整的电子书阅读器应用，支持多种格式和个性化阅读体验。

## 📱 核心功能

### 基础功能
- **多格式支持**：EPUB、PDF、TXT
- **本地书籍管理**：自动扫描、分类、搜索
- **阅读器核心**：流畅翻页、字体调整、主题切换
- **阅读体验**：书签、笔记、高亮、进度保存

### 技术栈
- **语言**：Kotlin (主) + Java (兼容)
- **架构**：MVVM + Repository Pattern
- **依赖注入**：Hilt
- **异步处理**：Coroutines + Flow

## 📁 项目结构

```
app/
├── src/main/
│   ├── java/com/example/ireader/
│   │   ├── data/           # 数据层
│   │   ├── domain/         # 领域层  
│   │   ├── ui/             # 界面层
│   │   └── di/             # 依赖注入
│   └── res/                # 资源文件
└── build.gradle.kts        # 构建配置
```

## 🚀 快速开始

1. 克隆项目
2. 在Android Studio中打开
3. 同步Gradle依赖
4. 运行到设备或模拟器

## 🔧 下一步开发计划

- [ ] 创建基础Activity和Fragment
- [ ] 实现书籍扫描和管理
- [ ] 集成EPUB阅读器
- [ ] 添加PDF支持
- [ ] 实现阅读设置和主题
- [ ] 开发书签和笔记功能