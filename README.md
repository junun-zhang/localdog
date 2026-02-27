# Offline Translator Pro Chrome Extension

一个完全离线的Chrome浏览器插件，支持英语、韩语、中文互相翻译。

## 功能特性

- ✅ **输入文本翻译**: 类似Google Translate的界面，支持手动输入文本翻译
- ✅ **页面文字翻译**: 自动识别网页文字，鼠标悬停显示翻译
- ✅ **图片OCR翻译**: 识别图片中的文字并在图片下方显示翻译对照
- ✅ **完全离线**: 无需网络连接，适合中国网络环境
- ✅ **多语言支持**: 英语(en)、中文(zh)、韩语(ko)互译
- ✅ **智能语言检测**: 自动识别输入文本的语言
- ✅ **一键控制**: 简单的开启/关闭翻译功能

## 技术架构

- **前端**: Chrome Extension API + HTML/CSS/JavaScript
- **OCR引擎**: Tesseract.js (离线模式)
- **翻译引擎**: 基于本地词典的离线翻译
- **存储**: Chrome Storage API + IndexedDB (用于大文件)

## 文件结构

```
offline-translator-plugin/
├── manifest.json          # 插件配置文件
├── popup.html            # 主界面 (输入翻译 + 页面控制)
├── popup.js              # 主界面逻辑
├── content.js            # 页面内容处理脚本
├── background.js         # 后台服务脚本
├── lib/
│   ├── translator.js     # 离线翻译核心
│   ├── ocr.js           # OCR处理模块
│   └── dictionary/      # 离线词典文件 (6个语言对)
├── models/              # OCR模型文件 (按需下载)
│   └── tesseract/
├── assets/             # 静态资源
│   └── icon.svg
├── download-models.js   # OCR模型下载脚本
├── test-installation.html # 测试页面
└── README.md
```

## 安装和使用

### 开发安装
1. 克隆或下载此项目
2. 打开Chrome浏览器，访问 `chrome://extensions/`
3. 开启"开发者模式"
4. 点击"加载已解压的扩展程序"
5. 选择此项目文件夹

### 使用方法
1. 点击浏览器工具栏的插件图标
2. 在"输入翻译"标签页中输入文本进行翻译
3. 在"页面翻译"标签页中翻译当前网页
4. 设置目标语言和自动翻译选项

## 词典数据

包含完整的6个语言对词典文件：
- en-zh.json (英语→中文)
- zh-en.json (中文→英语)
- en-ko.json (英语→韩语)
- ko-en.json (韩语→英语)
- zh-ko.json (中文→韩语)
- ko-zh.json (韩语→中文)

每个词典包含300+常用词汇，涵盖日常用语、技术术语、商务词汇等。

## OCR模型

支持Tesseract.js离线OCR，包含以下语言模型：
- eng (英语)
- chi_sim (中文简体)
- kor (韩语)

使用 `download-models.js` 脚本自动下载所需模型。

## 完全离线

- 无网络请求
- 无外部API依赖
- 适合中国网络环境
- 数据完全本地存储

## 许可证

MIT License