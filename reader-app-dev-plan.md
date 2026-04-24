# Android 书籍阅读 App 开发计划

## 项目概述
开发一款参考"掌阅"(iReader)的Android书籍阅读App，支持多格式阅读、书架管理、图书商店、
书签批注等核心功能。采用渐进式开发，从基础到高级，每个阶段可独立运行验证。

---

## 技术栈建议
- 语言: Kotlin
- 架构: MVVM + Repository
- UI: Jetpack Compose
- 数据库: Room (本地) + SQLite
- 网络: Retrofit + OkHttp
- 图片加载: Coil
- 依赖注入: Hilt
- 异步: Kotlin Coroutines + Flow
- 文件处理: java.io + DocumentFile API

---

## 参考掌阅App的核心功能清单

基于掌阅(iReader)的实际功能，整理如下需求分类：

### 阅读体验类
- 多格式支持 (EPUB/PDF/TXT/AZW3/MOBI/CHM)
- 字体大小/类型/行距调节
- 阅读主题 (白天/夜间/护眼/自定义)
- 屏幕亮度调节
- 翻页动画 (仿真/覆盖/滚动/无动画)
- 进度显示 (章节进度/百分比/已读时间)
- 目录导航
- 全屏沉浸式阅读
- 点击区域自定义 (翻页/菜单/翻页+菜单)
- 文字搜索
- 文字转语音朗读

### 书架管理类
- 本地书籍扫描与导入
- 书籍封面自动获取
- 阅读进度记忆
- 书籍排序 (时间/名称/大小/进度)
- 书架分类/分组
- 批量管理 (删除/移动)
- 最近阅读快速入口

### 书签批注类
- 添加/删除书签
- 文字高亮标注 (多颜色)
- 手写/文字批注
- 批注列表与跳转
- 批注导出分享

### 图书商店类
- 书籍分类浏览
- 搜索 (书名/作者/标签)
- 书籍详情 (简介/目录/试读/评价)
- 购买/付费
- 推荐算法 (个性化推荐/热门/新书)
- 购物车/订单管理
- 下载管理

### 社交与云同步类
- 用户账号系统
- 云端同步 (进度/书签/批注)
- 阅读统计报告
- 书评与评分
- 书籍分享
- 阅读排行榜

### 其他功能
- 内置字典/翻译
- 离线阅读
- 推送通知 (新书/活动)
- 无障碍支持

---

## 渐进式开发阶段 (从易到难)

### 第一阶段: 项目骨架搭建 (1-2周)
难度: ★☆☆☆☆

目标: 建立项目基础结构，能跑通基本流程

任务清单:
  [ ] 1.1 创建Android项目
      - 配置Kotlin + Compose
      - 设置Gradle依赖 (Retrofit, Room, Coil, Hilt)
      - 配置minSdkVersion (建议24+)
      - 设置版本管理

  [ ] 1.2 搭建项目架构
      - 建立MVVM包结构 (ui/model/repository/data)
      - 配置Hilt依赖注入
      - 设置Navigation组件 (页面路由)
      - 建立统一主题/颜色/字体资源

  [ ] 1.3 创建底部导航栏
      - 首页 (书架)
      - 发现 (商店入口)
      - 我的 (设置/个人中心)
      - 底部三个Tab + 对应Fragment/Composable

  [ ] 1.4 建立基础UI组件库
      - 自定义Toolbar
      - 通用Loading/Empty/Error状态视图
      - 通用Dialog/Toast组件
      - 基础按钮/输入框样式

  [ ] 1.5 建立数据层基础
      - Room数据库初始化
      - Book实体类定义 (id, title, author, filePath, coverPath, progress, lastReadTime)
      - BookDao基础CRUD操作
      - BookRepository封装

验收标准: App能启动，显示底部导航，能切换三个Tab页面

---

### 第二阶段: 本地书架核心 (2-3周)
难度: ★★☆☆☆

目标: 能扫描本地书籍，在书架上展示，点击可进入阅读

任务清单:
  [ ] 2.1 本地文件扫描
      - 申请READ_EXTERNAL_STORAGE权限
      - 扫描设备上的书籍文件 (epub/pdf/txt)
      - 支持手动选择文件夹导入
      - 支持单文件导入
      - 去重逻辑 (按文件名+大小)

  [ ] 2.2 书架页面
      - 网格/列表视图切换 (书架布局)
      - 书籍卡片: 封面 + 书名 + 作者 + 进度条
      - 封面获取策略:
        * 优先使用书籍内嵌封面
        * 其次使用本地缓存封面
        * 最后使用默认封面
      - 空书架状态 (引导导入)
      - 下拉刷新

  [ ] 2.3 阅读进度管理
      - 记录当前阅读章节/页码
      - 记录阅读进度百分比
      - 记录最后阅读时间
      - 按最后阅读时间排序

  [ ] 2.4 书籍管理操作
      - 长按选择模式
      - 删除书籍 (仅删除记录，不删文件)
      - 书籍详情页面 (基本信息展示)
      - 排序功能 (时间/名称/进度)

  [ ] 2.5 TXT文件阅读器 (最简单的格式)
      - 读取TXT文件内容
      - 按屏幕宽度分页
      - 显示章节 (按换行/标题识别)
      - 基础阅读界面 (文字居中显示)
      - 点击屏幕中央弹出/隐藏菜单栏

  [ ] 2.6 阅读界面基础框架
      - 顶部状态栏 (书名 + 章节名)
      - 底部菜单栏 (目录/书签/设置)
      - 全屏模式 (隐藏系统UI)
      - 屏幕常亮 (WakeLock)
      - 返回书架

验收标准: 能导入TXT书籍到书架，点击可进入阅读，退出后记住进度

---

### 第三阶段: 阅读体验优化 (2-3周)
难度: ★★☆☆☆

目标: 阅读体验达到可用水平

任务清单:
  [ ] 3.1 字体设置
      - 字体大小调节 (滑块 + 预设值)
      - 字体选择 (系统内置字体 + 可选下载字体)
      - 行间距调节 (1.0x - 3.0x)
      - 段间距调节
      - 页边距调节
      - 实时预览效果

  [ ] 3.2 阅读主题
      - 预设主题: 白天/夜间/羊皮纸/护眼绿
      - 背景色自定义
      - 文字颜色自定义
      - 主题切换动画
      - 记住用户选择

  [ ] 3.3 翻页效果
      - 仿真翻页 (PageTurn动画)
      - 覆盖翻页
      - 滚动模式 (类网页滚动)
      - 无动画 (直接切换)
      - 翻页设置持久化

  [ ] 3.4 阅读菜单与设置面板
      - 点击屏幕中央弹出菜单
      - 目录面板 (侧滑/底部弹窗)
      - 设置面板 (字体/主题/翻页)
      - 进度条拖动跳转
      - 章节跳转

  [ ] 3.5 亮度调节
      - 集成亮度滑块
      - 自动亮度开关
      - 手动亮度覆盖系统设置

  [ ] 3.6 阅读统计
      - 本次阅读时长
      - 累计阅读时长
      - 阅读速度估算 (字/分钟)
      - 阅读进度百分比显示

验收标准: 阅读体验流畅，可自定义字体/主题/翻页方式，设置可持久化

---

### 第四阶段: 书签与批注系统 (2周)
难度: ★★★☆☆

目标: 支持完整的书签和批注功能

任务清单:
  [ ] 4.1 书签系统
      - 数据库设计: Bookmark表 (id, bookId, chapter, position, createTime)
      - 添加/删除书签 (点击书签图标)
      - 书签列表页面
      - 从书签列表跳转回原文位置
      - 阅读界面书签图标状态 (有/无书签的视觉反馈)

  [ ] 4.2 文字选择与高亮
      - 长按选择文字
      - 选择工具栏 (复制/高亮/批注/分享)
      - 多颜色高亮 (黄/绿/蓝/红/紫)
      - 高亮数据持久化 (位置信息存储)
      - 重新打开书籍时恢复高亮

  [ ] 4.3 批注功能
      - 选中文字后添加批注
      - 批注输入框 (键盘弹出)
      - 批注列表页面 (按书籍分组)
      - 批注与原文位置关联
      - 从批注列表跳转回原文
      - 批注编辑与删除

  [ ] 4.4 批注管理
      - 全部批注列表
      - 按书籍筛选批注
      - 批注搜索
      - 批注导出 (文本文件/图片)
      - 批量删除批注

验收标准: 能添加书签、高亮文字、添加文字批注，所有批注可持久化并跳转

---

### 第五阶段: EPUB格式支持 (3-4周)
难度: ★★★☆☆

目标: 支持EPUB格式书籍的阅读

任务清单:
  [ ] 5.1 EPUB解析库集成
      - 选择并集成EPUB解析库 (推荐: epublib 或 readium)
      - 解析EPUB文件结构 (OPF/NCX/HTML)
      - 提取书籍元数据 (书名/作者/封面/目录)
      - 提取章节HTML内容

  [ ] 5.2 EPUB渲染引擎
      - 使用WebView渲染HTML章节
      - CSS样式注入 (应用用户设置)
      - 图片加载与缓存
      - 字体嵌入支持
      - 目录解析与导航

  [ ] 5.3 EPUB阅读功能
      - 章节前后翻
      - 目录跳转
      - 进度计算 (基于章节和SPINE顺序)
      - 书签在EPUB中的定位 (CFI或章节+偏移)
      - 高亮在EPUB中的定位与恢复

  [ ] 5.4 EPUB高级特性
      - 目录层级展示
      - 脚注/尾注跳转
      - 内嵌图片显示
      - 表格渲染适配
      - 字体嵌入(.ttf/.otf)

验收标准: 能打开EPUB书籍，正确显示内容，支持书签批注

---

### 第六阶段: PDF格式支持 (3-4周)
难度: ★★★★☆

目标: 支持PDF格式书籍的阅读

任务清单:
  [ ] 6.1 PDF渲染引擎集成
      - 选择PDF渲染方案:
        * 方案A: MuPDF (高性能，C底层)
        * 方案B: PdfRenderer (Android原生API)
        * 方案C: AndroidPdfViewer (第三方封装)
      - 集成并优化渲染性能

  [ ] 6.2 PDF阅读功能
      - 逐页渲染与缓存
      - 缩放 (pinch to zoom)
      - 滚动模式
      - 页面跳转
      - 双页模式 (平板)

  [ ] 6.3 PDF优化
      - 预加载相邻页
      - 内存管理 (及时释放不可见页)
      - 大图优化 (分块渲染)
      - 滚动流畅度优化 (60fps)

  [ ] 6.4 PDF书签批注
      - PDF书签定位 (页码+坐标)
      - PDF文字选择 (文本层提取)
      - PDF高亮与批注
      - 注意: 扫描版PDF需OCR才能选文字

验收标准: 能打开PDF书籍，流畅阅读，支持缩放和书签

---

### 第七阶段: 用户账号系统 (2-3周)
难度: ★★★☆☆

目标: 建立用户体系，为云同步和商店做准备

任务清单:
  [ ] 7.1 后端架构设计
      - 选择后端方案:
        * 自建 (Spring Boot / Node.js)
        * BaaS (Firebase / LeanCloud)
      - 设计用户表、书籍表、订单表、书签表
      - API接口设计 (RESTful)

  [ ] 7.2 注册登录
      - 手机号注册/登录
      - 短信验证码
      - 密码登录
      - 第三方登录 (微信/QQ/微博)
      - Token管理 (JWT)
      - 自动登录/记住密码

  [ ] 7.3 个人中心
      - 头像上传
      - 昵称/性别/签名修改
      - 阅读偏好设置
      - 账号安全设置
      - 退出登录

  [ ] 7.4 本地用户数据管理
      - User实体与DAO
      - 本地Token存储 (EncryptedSharedPreferences)
      - 登录状态管理
      - 网络请求拦截器 (自动附加Token)

验收标准: 能注册/登录，个人信息可修改，Token自动附加到请求

---

### 第八阶段: 云同步功能 (2-3周)
难度: ★★★★☆

目标: 书签、批注、阅读进度云端同步

任务清单:
  [ ] 8.1 同步数据模型
      - 云端书签表
      - 云端批注表
      - 云端阅读进度表
      - 冲突解决策略 (时间戳/服务端优先)

  [ ] 8.2 同步机制
      - 增量同步 (只同步变更数据)
      - 同步触发时机:
        * 登录时全量同步
        * 阅读退出时增量同步
        * 定时后台同步
      - 离线处理 (本地缓存，联网后同步)
      - 同步状态指示

  [ ] 8.3 冲突处理
      - 同一书籍多设备阅读
      - 进度冲突 (取最新时间)
      - 批注冲突 (合并/覆盖)
      - 同步失败重试

  [ ] 8.4 同步UI
      - 同步状态指示器
      - 手动触发同步
      - 同步日志/提示
      - 冲突解决提示

验收标准: 更换设备后书签/批注/进度自动同步

---

### 第九阶段: 图书商店基础 (3-4周)
难度: ★★★★☆

目标: 建立图书商店，支持浏览和搜索

任务清单:
  [ ] 9.1 商店首页
      - Banner轮播 (推荐/活动)
      - 分类入口 (小说/文学/科技/生活...)
      - 热门推荐区
      - 新书上架区
      - 免费专区

  [ ] 9.2 分类浏览
      - 分类列表 (一级/二级分类)
      - 分类书籍列表 (分页加载)
      - 排序 (热门/新书/评分/价格)
      - 筛选 (免费/付费/格式)

  [ ] 9.3 搜索功能
      - 搜索框 (搜索历史)
      - 热门搜索推荐
      - 搜索建议 (自动补全)
      - 搜索结果列表
      - 搜索历史管理

  [ ] 9.4 书籍详情
      - 封面/书名/作者
      - 简介/标签
      - 目录预览
      - 试读功能
      - 评分与评论
      - 相关推荐
      - 购买/加入书架按钮

  [ ] 9.5 后端书籍管理
      - 书籍数据模型 (服务端)
      - 书籍上传/管理后台
      - 书籍元数据API
      - 封面图片存储 (OSS/CDN)
      - 书籍文件存储与下载

验收标准: 能浏览商店、搜索书籍、查看详情、试读

---

### 第十阶段: 购买与下载 (3-4周)
难度: ★★★★★

目标: 实现完整的购买流程和下载管理

任务清单:
  [ ] 10.1 支付集成
      - 微信支付SDK集成
      - 支付宝SDK集成
      - 支付流程:
        * 创建订单
        * 调起支付
        * 支付回调处理
        * 订单状态确认
      - 支付安全 (签名验证)

  [ ] 10.2 订单管理
      - 订单数据模型
      - 订单创建API
      - 订单列表
      - 订单详情
      - 订单状态流转 (待支付/已支付/已取消)
      - 退款流程

  [ ] 10.3 下载管理
      - 下载队列管理
      - 断点续传
      - 下载进度显示
      - 下载暂停/恢复/取消
      - 下载完成自动入库
      - 下载失败重试

  [ ] 10.4 版权保护 (DRM)
      - 书籍文件加密存储
      - 下载权限验证
      - 设备绑定 (限制设备数)
      - 防止简单复制

  [ ] 10.5 购物车 (可选)
      - 添加书籍到购物车
      - 购物车列表
      - 批量结算
      - 优惠券支持

验收标准: 能购买书籍，完成支付，下载并阅读

---

### 第十一阶段: 推荐系统 (3-4周)
难度: ★★★★★

目标: 个性化书籍推荐

任务清单:
  [ ] 11.1 用户行为数据采集
      - 浏览记录
      - 搜索记录
      - 购买记录
      - 阅读时长
      - 收藏/加入书架
      - 评分/评论

  [ ] 11.2 推荐算法
      - 基于协同过滤 (相似用户偏好)
      - 基于内容推荐 (书籍标签匹配)
      - 基于热门趋势
      - 混合推荐策略
      - 冷启动策略 (新用户)

  [ ] 11.3 推荐展示
      - 首页个性化推荐区
      - "猜你喜欢"模块
      - 书籍详情页"相关推荐"
      - 商店首页推荐位
      - 推荐效果追踪

  [ ] 11.4 后端推荐服务
      - 推荐引擎搭建
      - 用户画像构建
      - 书籍标签体系
      - 推荐结果缓存
      - A/B测试框架

验收标准: 商店首页有个性化推荐，推荐内容与用户偏好相关

---

### 第十二阶段: 社交与阅读报告 (2-3周)
难度: ★★★☆☆

目标: 增加社交功能和阅读统计

任务清单:
  [ ] 12.1 阅读统计
      - 每日/每周/每月阅读报告
      - 阅读时长统计
      - 阅读书籍数量
      - 阅读页数统计
      - 阅读习惯分析 (时间段分布)
      - 阅读报告分享 (生成图片)

  [ ] 12.2 书评系统
      - 书籍评论列表
      - 发表评论
      - 评论回复
      - 点赞/踩
      - 评论举报

  [ ] 12.3 分享功能
      - 书籍分享 (链接/卡片)
      - 精彩段落分享
      - 阅读报告分享
      - 分享到微信/QQ/微博

  [ ] 12.4 排行榜
      - 热门书籍排行
      - 新书排行
      - 畅销书排行
      - 分类排行
      - 用户阅读排行

验收标准: 有阅读报告、书评、分享、排行榜功能

---

### 第十三阶段: 高级阅读功能 (2-3周)
难度: ★★★★☆

目标: 增加TTS朗读、字典等高级功能

任务清单:
  [ ] 13.1 文字转语音 (TTS)
      - 集成Android TTS引擎
      - 朗读控制 (播放/暂停/快进/调速)
      - 朗读进度同步
      - 后台朗读 (Notification控制)
      - 声音选择

  [ ] 13.2 内置字典
      - 选中文字查词典
      - 英汉词典集成
      - 汉语词典
      - 生词本功能
      - 查词历史

  [ ] 13.3 翻译功能
      - 选中文字翻译
      - 集成翻译API (百度/有道)
      - 多语言支持

  [ ] 13.4 阅读设置进阶
      - 定时关闭 (睡眠定时器)
      - 震动翻页 (外接设备)
      - 蓝牙翻页器支持
      - 音量键翻页

验收标准: 支持TTS朗读、字典查询、翻译

---

### 第十四阶段: 性能优化与打磨 (2-3周)
难度: ★★★★☆

目标: 提升App整体质量和用户体验

任务清单:
  [ ] 14.1 性能优化
      - 启动速度优化 (冷启动 < 2s)
      - 书架加载优化 (分页/懒加载)
      - 阅读器内存优化
      - 图片缓存策略
      - 数据库查询优化
      - 布局渲染优化

  [ ] 14.2 稳定性
      - 崩溃率降低 (< 0.1%)
      - 内存泄漏检测 (LeakCanary)
      - 异常捕获与上报
      - 大文件处理优化
      - 低内存设备适配

  [ ] 14.3 兼容性
      - Android版本兼容 (7.0 - 14+)
      - 不同屏幕适配 (手机/平板/折叠屏)
      - 深色模式适配
      - 横屏适配
      - 不同厂商ROM适配

  [ ] 14.4 无障碍
      - TalkBack支持
      - 字体大小无障碍模式
      - 颜色对比度检查
      - 触摸目标大小

  [ ] 14.5 安全
      - 网络通信加密 (HTTPS)
      - 敏感数据加密存储
      - 反调试检测
      - 代码混淆 (R8/ProGuard)

验收标准: 性能指标达标，崩溃率低，兼容主流设备

---

### 第十五阶段: 上线准备 (1-2周)
难度: ★★★☆☆

目标: 完成上线前所有准备工作

任务清单:
  [ ] 15.1 应用商店准备
      - App图标设计
      - 应用截图制作
      - 应用描述文案
      - 隐私政策编写
      - 用户协议编写

  [ ] 15.2 测试
      - 功能测试 (全功能回归)
      - 兼容性测试 (主流机型)
      - 压力测试
      - 安全测试
      - Beta测试 (邀请用户)

  [ ] 15.3 监控与分析
      - 集成崩溃上报 (Bugly/Sentry)
      - 集成数据分析 (友盟/GA)
      - 集成推送服务
      - 服务器监控

  [ ] 15.4 发布
      - 生成签名APK/AAB
      - 提交应用商店审核
      - 灰度发布策略
      - 版本更新机制 (强制/可选)

验收标准: App成功上架，核心功能正常运行

---

## 开发周期估算

| 阶段 | 内容 | 周期 | 难度 |
|------|------|------|------|
| 1 | 项目骨架 | 1-2周 | ★☆☆ |
| 2 | 本地书架 | 2-3周 | ★★☆ |
| 3 | 阅读体验 | 2-3周 | ★★☆ |
| 4 | 书签批注 | 2周 | ★★★ |
| 5 | EPUB支持 | 3-4周 | ★★★ |
| 6 | PDF支持 | 3-4周 | ★★★★ |
| 7 | 账号系统 | 2-3周 | ★★★ |
| 8 | 云同步 | 2-3周 | ★★★★ |
| 9 | 图书商店 | 3-4周 | ★★★★ |
| 10 | 购买下载 | 3-4周 | ★★★★★ |
| 11 | 推荐系统 | 3-4周 | ★★★★★ |
| 12 | 社交报告 | 2-3周 | ★★★ |
| 13 | 高级功能 | 2-3周 | ★★★★ |
| 14 | 性能优化 | 2-3周 | ★★★★ |
| 15 | 上线准备 | 1-2周 | ★★★ |
| **合计** | | **约36-50周** | |

> 注: 以上为单人开发估算。如团队协作可并行部分阶段。
> 最小可用产品(MVP)建议完成阶段1-4，约7-10周。

---

## MVP (最小可用产品) 范围

如果希望快速验证产品，建议先完成:
- 阶段1-4: 项目骨架 + 书架 + 基础阅读 + 书签批注
- 支持格式: TXT + EPUB
- 商店: 暂不做，先做好本地阅读体验
- 预计: 7-10周

---

## 后端架构建议

```
┌─────────────────────────────────────────┐
│                 前端 (Android)           │
│  书架 │ 阅读器 │ 商店 │ 我的            │
└──────────────┬──────────────────────────┘
               │ HTTPS (REST API)
┌──────────────┴──────────────────────────┐
│              API Gateway                │
│         (Nginx / Kong)                  │
└─────┬──────────┬──────────┬─────────────┘
      │          │          │
┌─────┴───┐ ┌───┴────┐ ┌───┴──────┐
│ 用户服务 │ │ 书籍服务 │ │ 订单服务 │
│ Spring  │ │ Spring │ │ Spring  │
│ Boot    │ │ Boot   │ │ Boot    │
└────┬────┘ └───┬────┘ └────┬─────┘
     │          │           │
┌────┴──────────┴───────────┴────┐
│        数据存储层               │
│  MySQL │ Redis │ OSS │ ES      │
└────────────────────────────────┘
```

---

## 数据库核心表设计

### 本地数据库 (Room)
```sql
-- 书籍表
CREATE TABLE books (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    author TEXT,
    format TEXT,           -- 'txt', 'epub', 'pdf'
    file_path TEXT,
    cover_path TEXT,
    file_size INTEGER,
    add_time INTEGER,
    last_read_time INTEGER,
    current_chapter INTEGER,
    progress REAL,         -- 0.0 ~ 1.0
    is_downloaded BOOLEAN, -- 商店书籍
    book_source TEXT       -- 'local', 'store'
);

-- 书签表
CREATE TABLE bookmarks (
    id TEXT PRIMARY KEY,
    book_id TEXT NOT NULL,
    chapter_index INTEGER,
    position TEXT,         -- 文本偏移量或CFI
    create_time INTEGER,
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- 批注表
CREATE TABLE annotations (
    id TEXT PRIMARY KEY,
    book_id TEXT NOT NULL,
    chapter_index INTEGER,
    start_position TEXT,
    end_position TEXT,
    highlighted_text TEXT,
    color INTEGER,         -- 高亮颜色
    note TEXT,             -- 批注文字
    create_time INTEGER,
    update_time INTEGER,
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- 阅读历史
CREATE TABLE reading_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_id TEXT NOT NULL,
    read_start INTEGER,
    read_end INTEGER,
    duration INTEGER,      -- 阅读时长(秒)
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- 用户设置
CREATE TABLE user_settings (
    key TEXT PRIMARY KEY,
    value TEXT
);
```

### 云端数据库 (MySQL)
```sql
-- 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone VARCHAR(20) UNIQUE,
    nickname VARCHAR(50),
    avatar_url VARCHAR(255),
    password_hash VARCHAR(255),
    create_time DATETIME,
    update_time DATETIME
);

-- 书籍表 (商店)
CREATE TABLE store_books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100),
    description TEXT,
    cover_url VARCHAR(255),
    file_url VARCHAR(255),
    format VARCHAR(10),
    price DECIMAL(10,2),
    is_free BOOLEAN DEFAULT FALSE,
    category_id BIGINT,
    tags VARCHAR(500),
    rating DECIMAL(3,1),
    rating_count INTEGER DEFAULT 0,
    download_count INTEGER DEFAULT 0,
    status TINYINT,        -- 0上架 1下架
    create_time DATETIME
);

-- 用户书籍关系 (我的书架-商店)
CREATE TABLE user_books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    purchase_time DATETIME,
    download_count INTEGER DEFAULT 0,
    UNIQUE KEY uk_user_book (user_id, book_id)
);

-- 云端书签
CREATE TABLE cloud_bookmarks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    position TEXT,
    create_time DATETIME,
    update_time DATETIME
);

-- 云端批注
CREATE TABLE cloud_annotations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    position_start TEXT,
    position_end TEXT,
    text_content TEXT,
    color INTEGER,
    note TEXT,
    create_time DATETIME,
    update_time DATETIME
);

-- 阅读进度
CREATE TABLE reading_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    chapter_index INTEGER,
    position TEXT,
    progress REAL,
    update_time DATETIME,
    UNIQUE KEY uk_user_book (user_id, book_id)
);

-- 订单表
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) UNIQUE,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    amount DECIMAL(10,2),
    pay_method VARCHAR(20),  -- 'wechat', 'alipay'
    status TINYINT,          -- 0待支付 1已支付 2已取消
    pay_time DATETIME,
    create_time DATETIME
);
```

---

## 项目目录结构建议

```
app/
├── src/main/
│   ├── java/com/yourapp/reader/
│   │   ├── di/                          # Hilt依赖注入模块
│   │   │   ├── AppModule.kt
│   │   │   ├── DatabaseModule.kt
│   │   │   └── NetworkModule.kt
│   │   ├── data/
│   │   │   ├── local/                   # Room数据库
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   ├── dao/
│   │   │   │   │   ├── BookDao.kt
│   │   │   │   │   ├── BookmarkDao.kt
│   │   │   │   │   └── AnnotationDao.kt
│   │   │   │   └── entity/
│   │   │   │       ├── Book.kt
│   │   │   │       ├── Bookmark.kt
│   │   │   │       └── Annotation.kt
│   │   │   ├── remote/                  # Retrofit API
│   │   │   │   ├── ApiClient.kt
│   │   │   │   ├── api/
│   │   │   │   │   ├── BookApi.kt
│   │   │   │   │   ├── UserApi.kt
│   │   │   │   │   └── OrderApi.kt
│   │   │   │   └── model/
│   │   │   │       ├── BookResponse.kt
│   │   │   │       └── ...
│   │   │   └── repository/
│   │   │       ├── BookRepository.kt
│   │   │       └── ...
│   │   ├── reader/                      # 阅读器核心
│   │   │   ├── engine/
│   │   │   │   ├── ReadingEngine.kt     # 接口
│   │   │   │   ├── TxtReader.kt
│   │   │   │   ├── EpubReader.kt
│   │   │   │   └── PdfReader.kt
│   │   │   ├── model/
│   │   │   │   ├── Chapter.kt
│   │   │   │   └── ReadingPosition.kt
│   │   │   └── parser/
│   │   │       ├── TxtParser.kt
│   │   │       └── EpubParser.kt
│   │   ├── ui/
│   │   │   ├── theme/                   # Compose主题
│   │   │   ├── navigation/              # 导航路由
│   │   │   ├── bookshelf/               # 书架
│   │   │   │   ├── BookshelfScreen.kt
│   │   │   │   └── BookshelfViewModel.kt
│   │   │   ├── reader/                  # 阅读器界面
│   │   │   │   ├── ReaderScreen.kt
│   │   │   │   ├── ReaderViewModel.kt
│   │   │   │   ├── ReadingSettingsPanel.kt
│   │   │   │   └── TableOfContentsPanel.kt
│   │   │   ├── bookmark/                # 书签
│   │   │   │   ├── BookmarkListScreen.kt
│   │   │   │   └── BookmarkViewModel.kt
│   │   │   ├── annotation/              # 批注
│   │   │   │   ├── AnnotationListScreen.kt
│   │   │   │   └── AnnotationViewModel.kt
│   │   │   ├── store/                   # 商店
│   │   │   │   ├── StoreScreen.kt
│   │   │   │   ├── BookDetailScreen.kt
│   │   │   │   └── SearchScreen.kt
│   │   │   ├── profile/                 # 我的
│   │   │   │   ├── ProfileScreen.kt
│   │   │   │   └── SettingsScreen.kt
│   │   │   └── component/               # 通用组件
│   │   │       ├── LoadingView.kt
│   │   │       ├── EmptyView.kt
│   │   │       └── ...
│   │   ├── util/                        # 工具类
│   │   │   ├── FileUtil.kt
│   │   │   ├── StorageUtil.kt
│   │   │   └── PreferenceUtil.kt
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── values/
│   │   └── ...
│   └── AndroidManifest.xml
└── build.gradle.kts
```

---

## 关键第三方库推荐

| 用途 | 推荐库 | 说明 |
|------|--------|------|
| EPUB解析 | epublib (epublib-core) | 成熟的EPUB解析库 |
| PDF渲染 | tv.danmaku.ijk.media:AndroidPdfViewer | 基于muPDF封装 |
| 网络请求 | Retrofit2 + OkHttp3 | 标准方案 |
| 图片加载 | Coil | Compose友好 |
| 数据库 | Room | Jetpack官方 |
| 依赖注入 | Hilt | Jetpack官方 |
| 页面导航 | Navigation Compose | Compose导航 |
| 文件选择 | android-filepicker | 本地文件选择 |
| 分页加载 | Paging3 | 列表分页 |
| 图片压缩 | Luban | 封面图压缩 |
| 崩溃上报 | Bugly / Sentry | 线上监控 |
| 支付 | 微信支付SDK + 支付宝SDK | 国内支付 |

---

## 风险与注意事项

1. EPUB渲染: 不同EPUB文件质量差异大，需处理各种边界情况
2. PDF性能: 大文件PDF容易OOM，需精细的内存管理
3. 版权合规: 图书商店需确保内容版权合法
4. 支付合规: 需完成企业资质认证才能接入支付
5. 隐私合规: 需符合《个人信息保护法》要求
6. 格式兼容: TXT编码多样 (UTF-8/GBK/GB2312)，需自动检测
7. 书城内容: 早期内容冷启动是关键挑战
