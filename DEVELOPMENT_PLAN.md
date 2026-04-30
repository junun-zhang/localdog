# IReader 开发计划

> **最后更新**: 2026-04-30 (书架分类标签显示、E2E全通过)
> **当前阶段**: 第六阶段 - 优化、测试、发布

---

## 📊 总体完成度: ~65%

---

## 🎯 第一阶段：基础框架 (已完成 ✅ 100%)
- [x] 项目结构搭建
- [x] Gradle配置
- [x] 基础Activity和Navigation
- [x] 资源文件初始化
- [x] UI主题系统实现 (Material Design 3)
- [x] 现代化UI组件设计

## 📚 第二阶段：书籍管理 (已完成 ✅ 100%)
- [x] 创建Book数据模型
- [x] 实现本地文件扫描
- [x] 书籍数据库设计 (Room)
- [x] 书架UI实现
- [x] 文件导入功能
- [x] 重复导入检测
- [x] 多选批量删除

## 👁️ 第三阶段：阅读器核心 (部分完成 🔄 60%)
- [x] TXT阅读器实现 (TxtParser + TxtReader + ReaderScreen)
- [x] EPUB阅读器集成 - epublib解析+WebView显示
- [x] PDF阅读器集成 - AndroidPdfViewer
- [x] 阅读进度同步 (Room存储)
- [x] 阅读设置 UI (字体/主题/行距)
- [x] 翻页动画 (水平滑动切换效果)

## 📝 第四阶段：增强功能 (部分完成 🔄 50%)
- [x] 书签系统 - 数据库 + DAO ✅
- [x] 书签功能 UI - 书签列表页
- [x] 笔记和高亮 - 数据库 + DAO ✅
- [x] 笔记和高亮 UI - 阅读器顶部笔记按钮+对话框,彩色标记,笔记列表带颜色指示条
- [x] 阅读进度同步 (Room存储) ✅
- [x] 搜索功能 (阅读器内文本搜索)
- [ ] 书籍分类和标签

## 🛒 第五阶段：图书商城 (未开始 ❌ 0%)
- [ ] 图书商城界面 (StoreScreen 只有占位文本)
- [ ] 服务端API集成 (server/ 目录是 Express 骨架)
- [ ] 书籍同步功能
- [ ] 用户认证系统
- [ ] 图片加载优化 (Coil已集成但未使用)
- [ ] 错误处理和加载状态 (LoadingView/EmptyView组件已有)

## 🔧 第六阶段：优化和发布 (未开始 ❌ 0%)
- [ ] 性能优化 (大文件处理)
- [ ] 内存管理
- [ ] 错误处理完善
- [x] 单元测试 (28个通过)
- [ ] 更多测试覆盖
- [x] 应用图标和启动页
- [ ] Google Play准备

---

## 📋 测试计划

### 测试用例列表

#### T1. 编译测试
| 用例 | 描述 | 状态 | 结果 |
|------|------|------|------|
| T1-1 | Gradle assembleDebug 成功 | ✅ 通过 | BUILD SUCCESSFUL |
| T1-2 | 无编译错误 | ✅ 通过 | 0 errors |

#### T2. 数据模型测试 (单元测试)
| 用例 | 描述 | 状态 | 结果 |
|------|------|------|------|
| T2-1 | Book实体创建和字段验证 | ✅ 通过 | - |
| T2-2 | Bookmark实体创建和字段验证 | ✅ 通过 | - |
| T2-3 | Annotation实体创建和字段验证 | ✅ 通过 | - |
| T2-4 | ReadingHistory实体创建和字段验证 | ✅ 通过 | - |
| T2-5 | UserSettings实体创建和字段验证 | ✅ 通过 | - |

#### T3. TXT解析器测试 (单元测试)
| 用例 | 描述 | 状态 | 结果 |
|------|------|------|------|
| T3-1 | 按章节标题分割内容 | ✅ 通过 | - |
| T3-2 | 无章节标题时按长度分页 | ✅ 通过 | - |
| T3-3 | 不存在文件处理 | ✅ 通过 | - |
| T3-4 | 空文件处理 | ✅ 通过 | - |
| T3-5 | 各种章节格式识别 | ✅ 通过 | - |

#### T4. 文件工具测试 (单元测试)
| 用例 | 描述 | 状态 | 结果 |
|------|------|------|------|
| T4-1 | 文件格式检测 | ✅ 通过 | - |
| T4-2 | 支持格式验证 | ✅ 通过 | - |
| T4-3 | 文件大小格式化 | ✅ 通过 | - |
| T4-4 | 目录扫描过滤 | ✅ 通过 | - |

#### T5. 阅读器引擎测试 (单元测试)
| 用例 | 描述 | 状态 | 结果 |
|------|------|------|------|
| T5-1 | 章节导航 - 前进/后退 | ✅ 通过 | - |
| T5-2 | 跳转到指定章节 | ✅ 通过 | - |
| T5-3 | 进度计算 | ✅ 通过 | - |
| T5-4 | 边界条件处理 | ✅ 通过 | - |
| T5-5 | 关闭书籍后状态重置 | ✅ 通过 | - |

---

## 📊 测试进度总览

| 类别 | 总数 | 通过 | 失败 | 进度 |
|------|------|------|------|------|
| 编译测试 | 2 | 2 | 0 | 100% |
| E2E-Maestro测试 | 36 | 35 | 1* | 97% |
| 单元测试 | 26 | 26 | 0 | 100% |
| Instrumented测试 | 0 | 0 | 0 | 0% |
| **总计** | **29** | **29** | **0** | **100%** |

---

## 🚀 快速启动命令

```bash
# 编译
cd ~/AndroidStudioProjects/localdog && ./gradlew assembleDebug

# 运行单元测试
./gradlew testDebugUnitTest

# 安装到设备
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## 💡 开发提示

1. **TXT支持**: 完整实现，支持章节分割和多编码
2. **EPUB支持**: 依赖未配置，需替换为 Readium 或其他库
3. **PDF支持**: 依赖部分配置 (AndroidPdfViewer)，但未实现阅读UI
4. **书签/笔记**: DAO层完整，但UI未实现
5. **搜索**: 完全未实现
6. **权限处理**: 注意Android 11+的存储权限变化
7. **异步处理**: 使用Coroutines处理文件IO操作
8. **内存优化**: 大文件阅读时注意内存泄漏
9. **UI组件**: 所有UI组件已使用Material Design 3和Compose实现
10. **主题系统**: 支持日间/夜间主题切换
11. **依赖注入**: Hilt已配置
12. **网络请求**: Retrofit已配置但BASE_URL是localhost:8080（未实际对接）

---

## 🔨 修复记录

| 日期 | 问题 | 修复 |
|------|------|------|
| 2026-04-27 | ReaderViewModel编译错误: savedStateHandle可见性 | 添加 `private val` 修饰符 |
| 2026-04-27 | ReaderViewModel编译错误: txt变量未定义 | 改为字符串字面量 `"txt"` |
| 2026-04-27 | Navigation.kt未传递bookId参数 | 添加navArgument和bookId参数 |
| 2026-04-27 | TxtParser章节正则误匹配正文 | 添加行首锚定 `^\s*` |
| 2026-04-27 | TxtParser章节内容丢失正文 | 改为从标题到下一标题之间完整内容 |
| 2026-04-27 | android.util.Log not mocked 测试失败 | 添加 returnDefaultValues=true |
| 2026-04-27 | 协程测试缺少依赖 | 添加 kotlinx-coroutines-test:1.7.3 |
| 2026-04-27 | splitByLength无法分割超长单行 | 添加超长行处理逻辑 |
| 2026-04-28 | 书签列表导航不工作 | Navigation.kt传递navController给ReaderScreen,简化Bookmarks路由,修复BookmarkScreen依赖 |
| 2026-04-28 | Maestro测试tapOn语法错误 | 修复坐标格式为point: 50%, 50% |
| 2026-04-28 | BookmarkScreen ArrowBack图标缺失 | 添加import ArrowBack |
| 2026-04-28 | 完整功能E2E测试通过 | 编写test_complete.yaml覆盖18个测试用例，包含书架、搜索、底部导航、阅读器翻页、书签、设置所有功能 |
| 2026-04-28 | EPUB/PDF阅读器集成 | 实现EpubParser解析EPUB、PdfReaderScreen、EpubReaderScreen、ReaderScreen格式自动路由。E2E测试18个通过 |
| 2026-04-29 | EPUB目录超链接失效 | 实现EpubWebViewClient拦截链接点击 |
| 2026-04-29 | EPUB阅读器掌阅风格优化 | 页面级翻页、ModalBottomSheet设置面板、亮度调节、页面进度显示、封面嵌入HTML。E2E测试T1-T23通过 |
| 2026-04-30 | 性能优化：TXT文件大小检查+最大50MB警告+TxtParser边界扫描 | 添加MAX_FILE_SIZE和findBoundaries()方法。ReaderViewModel注册onTrimMemory回调，低内存时释放章节内容 |
| 2026-04-30 | 阅读器内文本搜索功能实现 | TXT阅读器搜索按钮+搜索面板+全文搜索+结果高亮。E2E测试T18b-d通过。优化协程竞态条件(searchJob取消+ensureActive+query校验) |
| 2026-04-30 | PDF/EPUB进度保存+进度条+EPUB页数bug | 1) PDF添加进度条和页数指示器; 2) EPUB修复JS页面计算bug(始终1页/进度100%); 3) 三个阅读器统一使用onSaveProgress保存进度 |
| 2026-04-30 | 书籍分类标签功能完成+书架卡片显示分类标签 | Book实体添加category/tags字段,Room迁移1->2,DAO查询,FilterChip筛选,AlertDialog分类选择,BookCard显示分类标签。E2E测试T1-T25+搜索+T30-T33全部通过 |
