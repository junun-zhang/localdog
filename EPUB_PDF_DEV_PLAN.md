# EPUB/PDF阅读器集成开发计划

## 当前状态
- Book实体format字段已支持 txt/epub/pdf
- 文件扫描已支持 epub/pdf 格式
- PDF依赖已添加：AndroidPdfViewer 2.8.1
- EPUB依赖缺失：FolioReader被注释
- ReaderViewModel只处理txt格式

## 开发方案

### 第一阶段：PDF阅读器 (使用已有依赖)
1. 创建 PdfReaderScreen - 使用 AndroidPdfViewer 显示PDF
2. 添加 Navigation 路由：Screen.PdfReader
3. 修改 ReaderViewModel 路由到 PDF 阅读器
4. 测试：打开PDF文件正常显示

### 第二阶段：EPUB阅读器 (新增依赖)
1. 添加 epublib-android 依赖
2. 创建 EpubParser - 解析EPUB章节结构
3. 创建 EpubReaderScreen - 使用 WebView 显示EPUB
4. 添加 Navigation 路由：Screen.EpubReader
5. 修改 ReaderViewModel 路由到 EPUB 阅读器
6. 测试：打开EPUB文件正常显示

## 技术选型

### PDF: AndroidPdfViewer (已有)
- 优点：已在依赖中，支持PDFium引擎
- 缺点：基于Android View，需要与Compose集成
- 集成方式：使用 AndroidView 包装

### EPUB: epublib-android + WebView
- epublib: 纯Java EPUB解析库，无UI依赖
- WebView: 加载解析后的HTML内容
- 优点：轻量，可控制显示效果
- 集成方式：解析EPUB章节 -> 生成HTML -> WebView加载

## 测试计划
- PDF: 导入PDF文件 -> 打开 -> 翻页 -> 返回书架
- EPUB: 导入EPUB文件 -> 打开 -> 章节导航 -> 返回书架
