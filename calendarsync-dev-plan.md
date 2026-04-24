# CalSync — 共享日历应用开发计划

## 一、项目概述

### 1.1 项目定位
类似 Google Calendar 的 Android 日历应用，核心差异化：**多人共享日历 + 实时同步 + 中国节假日/农历 + 天气集成**

### 1.2 目标用户
- 家庭用户（共享日程、节假日安排）
- 中小学家长（寒暑假、校历同步）
- 小团队/工作小组（共享任务、会议安排）

### 1.3 核心卖点
1. 所有安装者共享待办事项列表，实时同步
2. 支持开始前/结束前提醒
3. 月历/年历/每日待办 三种颗粒度展示
4. 中国节假日 + 中小学寒暑假自动导入
5. 基于位置的天气图标展示

---

## 二、技术架构

### 2.1 客户端技术栈
| 类别 | 技术 | 版本 |
|------|------|------|
| 语言 | Kotlin | 1.9+ |
| UI | Jetpack Compose | 1.6+ |
| 架构 | MVVM + Repository | - |
| 依赖注入 | Hilt | 2.50+ |
| 异步 | Coroutines + Flow | - |
| 本地数据库 | Room | 2.6+ |
| 网络 | Retrofit + OkHttp | 2.9+ / 4.12 |
| JSON | Kotlinx Serialization | 1.6+ |
| 图片 | Coil | 2.5+ |
| 日历视图 | Compose Calendar 自定义 | - |
| 通知 | WorkManager + AlarmManager | - |
| 位置 | FusedLocationProvider | Play Services |
| 数据同步 | WebSocket / SSE | - |

### 2.2 后端技术栈
| 类别 | 技术 |
|------|------|
| 框架 | Spring Boot 3.x (Kotlin) |
| 数据库 | PostgreSQL 15+ |
| 缓存 | Redis |
| 实时通信 | WebSocket (STOMP) |
| 认证 | JWT + OAuth2 |
| 任务调度 | Quartz (提醒触发) |
| API | RESTful + WebSocket |

### 2.3 系统架构
```
┌─────────────────────────────────────────────────────┐
│                    Android Client                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │  UI Layer │  │ViewModel │  │   Repository     │  │
│  │(Compose)  │  │  (MVVM)  │  │  (Local + Remote)│  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
│         │                              │            │
│  ┌──────────┐                    ┌──────────┐      │
│  │   Room   │◄──── Sync ──────► │  WebSocket│      │
│  │ (Local DB)                   │  Client   │      │
│  └──────────┘                    └──────────┘      │
└─────────────────────────────────────────────────────┘
                          │
                          │ HTTPS / WSS
                          ▼
┌─────────────────────────────────────────────────────┐
│                   Spring Boot Server                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │  REST API │  │WebSocket │  │  Reminder Engine │  │
│  │          │  │  (STOMP) │  │  (Quartz)        │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
│         │                              │            │
│  ┌──────────┐                    ┌──────────┐      │
│  │PostgreSQL│                    │  Redis   │      │
│  └──────────┘                    └──────────┘      │
└─────────────────────────────────────────────────────┘
```

---

## 三、功能需求详细清单

### 3.1 P0 — MVP核心功能

#### F01: 用户认证
- [ ] 手机号/邮箱注册登录
- [ ] JWT Token 认证
- [ ] 自动登录（Token 持久化）
- [ ] 退出登录

#### F02: 事件管理（CRUD）
- [ ] 创建事件：标题、日期时间、时长/全天、地点、描述
- [ ] 编辑事件：修改任意属性
- [ ] 删除事件：单个删除 / 系列删除（重复事件）
- [ ] 全天事件支持
- [ ] 多日事件支持
- [ ] 事件颜色标记（6种预设颜色）
- [ ] 事件备注（纯文本）

#### F03: 重复事件
- [ ] 每日重复
- [ ] 每周重复（可选星期几）
- [ ] 每月重复（按日期/按星期）
- [ ] 每年重复
- [ ] 自定义重复间隔（每N天/周/月/年）
- [ ] 重复结束：无限 / 指定日期 / 指定次数
- [ ] 重复事件例外处理（修改单次）

#### F04: 提醒机制
- [ ] 系统通知提醒
- [ ] 可配置提前时间：0/5/10/15/30/60分钟/自定义
- [ ] 支持设置多个提醒时间点
- [ ] 通知中操作：确认/推迟(Snooze)/关闭
- [ ] 推迟选项：5/10/30分钟/1小时/明天
- [ ] 提醒开始/结束前可分别设置

#### F05: 视图模式
- [ ] 日视图：时间轴（30分钟一格）
- [ ] 周视图：7天网格
- [ ] 月视图：月历网格（显示事件摘要）
- [ ] 日程视图：按日期排列的事件列表
- [ ] 视图切换动画
- [ ] 左右滑动切换日/周

#### F06: 共享日历
- [ ] 创建共享日历
- [ ] 邀请用户加入（通过手机号/分享链接）
- [ ] 所有加入者共享事件列表
- [ ] 实时同步（WebSocket推送）
- [ ] 权限：创建者/编辑者/查看者
- [ ] 事件创建者标识
- [ ] 事件修改历史记录

#### F07: 待办事项（Task）
- [ ] 创建待办：标题、截止日期、优先级
- [ ] 待办状态：待办/进行中/已完成
- [ ] 待办提醒（截止前提醒）
- [ ] 待办与事件关联
- [ ] 待办列表视图
- [ ] 批量完成/删除

#### F08: 搜索
- [ ] 按关键词搜索事件/待办
- [ ] 按日期范围搜索
- [ ] 搜索结果高亮

---

### 3.2 P1 — 第一迭代功能

#### F09: 节假日支持
- [ ] 中国法定节假日自动导入（API: 腾讯日历API / 自建）
- [ ] 节假日标记（月历高亮）
- [ ] 调休工作日标记
- [ ] 农历日期显示
- [ ] 农历节日（春节、端午、中秋等）
- [ ] 二十四节气

#### F10: 中小学寒暑假
- [ ] 按地区选择学期校历
- [ ] 寒暑假日期导入
- [ ] 校历事件（开学日、期末考试等）
- [ ] 自定义校历

#### F11: 天气集成
- [ ] 自动定位获取天气
- [ ] 手动设置城市
- [ ] 日视图顶部显示天气图标+温度
- [ ] 周视图每日天气概览
- [ ] 天气来源：和风天气 API / OpenWeatherMap
- [ ] 天气图标缩略展示（类似待办事项样式）

#### F12: 通知设置
- [ ] 全局通知开关
- [ ] 默认提前提醒时间设置
- [ ] 免打扰时段设置
- [ ] 提醒铃声选择

#### F13: 冲突检测
- [ ] 创建/编辑时检测时间冲突
- [ ] 冲突提示（高亮显示）

---

### 3.3 P2 — 增强功能

#### F14: 主题定制
- [ ] 浅色/深色/跟随系统
- [ ] 自定义主题色
- [ ] 一周开始日设置（周一/周日）

#### F15: 导入导出
- [ ] ICS 文件导入
- [ ] ICS 文件导出
- [ ] 批量导出

#### F16: 桌面小部件
- [ ] 月历Widget（4x2）
- [ ] 日程Widget（4x1）

#### F17: 应用锁
- [ ] 指纹解锁
- [ ] PIN码解锁

---

## 四、数据库设计

### 4.1 本地数据库 (Room)

```kotlin
// 事件表
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,           // UUID
    val calendarId: String,               // 所属日历
    val title: String,
    val description: String?,
    val location: String?,
    val startTime: Long,                  // Unix timestamp
    val endTime: Long,
    val isAllDay: Boolean,
    val color: Int,
    val recurrenceRule: String?,          // RRULE 字符串
    val reminders: String,                // JSON: [5, 15, 60] 分钟
    val isShared: Boolean,
    val createdBy: String,                // 创建者ID
    val modifiedAt: Long,
    val syncStatus: Int                   // 0=同步, 1=待同步, 2=已删除
)

// 待办事项表
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val calendarId: String,
    val title: String,
    val description: String?,
    val dueDate: Long?,                   // 截止日期
    val priority: Int,                    // 0=无, 1=低, 2=中, 3=高
    val status: Int,                      // 0=待办, 1=进行中, 2=已完成
    val reminders: String,                // JSON
    val isShared: Boolean,
    val createdBy: String,
    val modifiedAt: Long,
    val syncStatus: Int
)

// 日历表
@Entity(tableName = "calendars")
data class CalendarEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: Int,
    val isVisible: Boolean,
    val isShared: Boolean,
    val ownerUserId: String,
    val role: Int                         // 0=所有者, 1=编辑者, 2=查看者
)

// 用户表
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val nickname: String,
    val avatarUrl: String?,
    val phone: String?
)

// 同步记录表
@Entity(tableName = "sync_records")
data class SyncRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entityType: String,               // "event" / "task"
    val entityId: String,
    val lastSyncAt: Long,
    val serverVersion: Long
)
```

### 4.2 远程数据库 (PostgreSQL)

```sql
-- 用户表
CREATE TABLE users (
    id UUID PRIMARY KEY,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    nickname VARCHAR(50) NOT NULL,
    avatar_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 日历表
CREATE TABLE calendars (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    color INT DEFAULT -16777216,
    owner_id UUID REFERENCES users(id),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 日历成员表
CREATE TABLE calendar_members (
    calendar_id UUID REFERENCES calendars(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id),
    role VARCHAR(20) DEFAULT 'viewer',  -- owner/editor/viewer
    joined_at TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (calendar_id, user_id)
);

-- 事件表
CREATE TABLE events (
    id UUID PRIMARY KEY,
    calendar_id UUID REFERENCES calendars(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(300),
    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ NOT NULL,
    is_all_day BOOLEAN DEFAULT FALSE,
    color INT DEFAULT -16777216,
    recurrence_rule TEXT,              -- RRULE format
    reminders JSONB,                   -- [{"minutes": 15}, {"minutes": 60}]
    created_by UUID REFERENCES users(id),
    version INT DEFAULT 1,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 待办事项表
CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    calendar_id UUID REFERENCES calendars(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    due_date TIMESTAMPTZ,
    priority INT DEFAULT 0,            -- 0=none, 1=low, 2=medium, 3=high
    status INT DEFAULT 0,              -- 0=todo, 1=in_progress, 2=done
    reminders JSONB,
    created_by UUID REFERENCES users(id),
    version INT DEFAULT 1,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 提醒任务表
CREATE TABLE reminder_jobs (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID REFERENCES events(id),
    task_id UUID REFERENCES tasks(id),
    trigger_at TIMESTAMPTZ NOT NULL,
    status VARCHAR(20) DEFAULT 'pending', -- pending/fired/cancelled
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 节假日表
CREATE TABLE holidays (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(20),                  -- holiday/workday/festival
    lunar_date VARCHAR(20),            -- 农历日期
    is_adjustment BOOLEAN DEFAULT FALSE, -- 是否调休
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 索引
CREATE INDEX idx_events_calendar_time ON events(calendar_id, start_time);
CREATE INDEX idx_tasks_calendar_status ON tasks(calendar_id, status);
CREATE INDEX idx_reminder_jobs_trigger ON reminder_jobs(trigger_at, status);
CREATE INDEX idx_holidays_date ON holidays(date);
```

---

## 五、API 设计

### 5.1 REST API

```
# 认证
POST   /api/auth/register          # 注册
POST   /api/auth/login             # 登录
POST   /api/auth/refresh           # 刷新Token

# 日历
GET    /api/calendars              # 获取我的日历列表
POST   /api/calendars              # 创建日历
GET    /api/calendars/{id}         # 获取日历详情
PUT    /api/calendars/{id}         # 更新日历
DELETE /api/calendars/{id}         # 删除日历

# 日历成员
POST   /api/calendars/{id}/members        # 邀请成员
GET    /api/calendars/{id}/members        # 获取成员列表
PUT    /api/calendars/{id}/members/{uid}  # 修改成员权限
DELETE /api/calendars/{id}/members/{uid}  # 移除成员

# 通过链接加入
POST   /api/calendars/join?code={inviteCode}

# 事件
GET    /api/events?calendarId=&start=&end=  # 获取事件列表
POST   /api/events                          # 创建事件
GET    /api/events/{id}                     # 获取事件详情
PUT    /api/events/{id}                     # 更新事件
DELETE /api/events/{id}                     # 删除事件

# 待办
GET    /api/tasks?calendarId=&status=       # 获取待办列表
POST   /api/tasks                           # 创建待办
PUT    /api/tasks/{id}                      # 更新待办
DELETE /api/tasks/{id}                      # 删除待办

# 搜索
GET    /api/search?q=&type=&start=&end=     # 搜索

# 节假日
GET    /api/holidays?year=&month=           # 获取节假日
GET    /api/holidays/school?region=         # 获取校历

# 天气
GET    /api/weather?lat=&lon=               # 获取天气
```

### 5.2 WebSocket (STOMP)

```
# 连接
CONNECT /ws?token={jwt}

# 订阅
SUBSCRIBE /topic/calendar/{calendarId}    # 订阅日历变更
SUBSCRIBE /topic/reminders                 # 订阅提醒

# 发送
SEND /app/calendar/{calendarId}/event      # 推送事件变更
SEND /app/calendar/{calendarId}/task       # 推送待办变更
SEND /app/calendar/{calendarId}/member     # 推送成员变更

# 消息格式
{
    "type": "EVENT_CREATED" | "EVENT_UPDATED" | "EVENT_DELETED" |
            "TASK_CREATED" | "TASK_UPDATED" | "TASK_DELETED" |
            "MEMBER_JOINED" | "MEMBER_REMOVED",
    "calendarId": "...",
    "data": { ... },
    "timestamp": 1234567890,
    "userId": "..."  // 操作者
}
```

---

## 六、开发阶段规划

### Phase 1: 项目骨架 (1周)
| 任务 | 说明 |
|------|------|
| 创建Android项目 | Gradle配置、依赖引入 |
| 搭建架构 | MVVM + Repository + Hilt |
| 本地数据库 | Room建表、DAO |
| 网络层 | Retrofit配置、API接口定义 |
| WebSocket客户端 | STOMP连接、消息处理 |
| 认证模块 | 登录/注册UI + 逻辑 |

### Phase 2: 核心视图 (2周)
| 任务 | 说明 |
|------|------|
| 月历视图 | 自定义Compose月历组件 |
| 周视图 | 7天网格布局 |
| 日视图 | 时间轴布局 |
| 日程视图 | 按日期排列的事件列表 |
| 视图切换 | 底部导航 + 手势 |
| 今日高亮 | 当前日期标记 |

### Phase 3: 事件管理 (1.5周)
| 任务 | 说明 |
|------|------|
| 事件创建/编辑 | 表单UI + 验证 |
| 事件详情 | 详情页面 |
| 重复事件 | RRULE解析与生成 |
| 提醒设置 | 多提醒时间点配置 |
| 事件颜色 | 颜色选择器 |
| 本地存储 | Room CRUD |

### Phase 4: 共享与同步 (2周)
| 任务 | 说明 |
|------|------|
| 后端服务 | Spring Boot项目搭建 |
| 共享日历 | 创建/邀请/加入 |
| 实时同步 | WebSocket推送 |
| 冲突解决 | 最后写入优先 + 版本号 |
| 离线支持 | 本地优先 + 后台同步 |
| 权限管理 | 创建者/编辑者/查看者 |

### Phase 5: 待办事项 (1周)
| 任务 | 说明 |
|------|------|
| 待办CRUD | 创建/编辑/删除/完成 |
| 待办列表 | 按状态/优先级分组 |
| 待办提醒 | 截止日期提醒 |
| 待办与事件关联 | 关联UI |

### Phase 6: 节假日与农历 (1周)
| 任务 | 说明 |
|------|------|
| 节假日数据 | 接入节假日API / 内置数据 |
| 农历转换 | 农历算法实现 |
| 月历节假日 | 月历上显示节假日 |
| 寒暑假校历 | 校历数据导入 |

### Phase 7: 天气集成 (0.5周)
| 任务 | 说明 |
|------|------|
| 位置权限 | 获取用户位置 |
| 天气API | 接入天气服务 |
| 天气显示 | 日/周视图天气图标 |

### Phase 8: 提醒引擎 (1周)
| 任务 | 说明 |
|------|------|
| 通知权限 | Android通知权限申请 |
| AlarmManager | 定时触发提醒 |
| 通知UI | 系统通知栏展示 |
| 通知操作 | 确认/推迟/关闭 |
| 后端提醒 | Quartz定时任务 |

### Phase 9: 搜索与设置 (0.5周)
| 任务 | 说明 |
|------|------|
| 搜索功能 | 关键词搜索 |
| 设置页面 | 通知/主题/语言等 |
| 关于页面 | 版本信息 |

### Phase 10: 测试与优化 (1周)
| 任务 | 说明 |
|------|------|
| 单元测试 | ViewModel/Repository测试 |
| UI测试 | Compose测试 |
| 集成测试 | 端到端测试 |
| 性能优化 | 启动速度/内存/流畅度 |
| Bug修复 | - |
| 发布准备 | 签名/应用商店 |

---

## 七、项目目录结构

```
calendar-app/
├── app/
│   ├── src/main/
│   │   ├── java/com/calsync/app/
│   │   │   ├── di/                          # Hilt依赖注入
│   │   │   │   ├── AppModule.kt
│   │   │   │   ├── DatabaseModule.kt
│   │   │   │   ├── NetworkModule.kt
│   │   │   │   └── RepositoryModule.kt
│   │   │   ├── data/                        # 数据层
│   │   │   │   ├── local/                   # 本地数据
│   │   │   │   │   ├── database/
│   │   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   │   ├── EventDao.kt
│   │   │   │   │   │   ├── TaskDao.kt
│   │   │   │   │   │   ├── CalendarDao.kt
│   │   │   │   │   │   └── SyncDao.kt
│   │   │   │   │   └── entity/
│   │   │   │   │       ├── EventEntity.kt
│   │   │   │   │       ├── TaskEntity.kt
│   │   │   │   │       ├── CalendarEntity.kt
│   │   │   │   │       └── UserEntity.kt
│   │   │   │   ├── remote/                  # 远程数据
│   │   │   │   │   ├── api/
│   │   │   │   │   │   ├── CalSyncApi.kt
│   │   │   │   │   │   └── interceptor/
│   │   │   │   │   │       └── AuthInterceptor.kt
│   │   │   │   │   ├── model/               # API数据模型
│   │   │   │   │   │   ├── EventDto.kt
│   │   │   │   │   │   ├── TaskDto.kt
│   │   │   │   │   │   ├── CalendarDto.kt
│   │   │   │   │   │   └── AuthDto.kt
│   │   │   │   │   └── websocket/
│   │   │   │   │       ├── WebSocketManager.kt
│   │   │   │   │       └── StompClient.kt
│   │   │   │   └── repository/
│   │   │   │       ├── EventRepository.kt
│   │   │   │       ├── TaskRepository.kt
│   │   │   │       ├── CalendarRepository.kt
│   │   │   │       └── SyncRepository.kt
│   │   │   ├── domain/                      # 领域层
│   │   │   │   ├── model/                   # 领域模型
│   │   │   │   │   ├── Event.kt
│   │   │   │   │   ├── Task.kt
│   │   │   │   │   ├── Calendar.kt
│   │   │   │   │   ├── Holiday.kt
│   │   │   │   │   └── Weather.kt
│   │   │   │   ├── usecase/                 # 用例
│   │   │   │   │   ├── CreateEventUseCase.kt
│   │   │   │   │   ├── SyncCalendarUseCase.kt
│   │   │   │   │   └── ...
│   │   │   │   └── util/                    # 工具
│   │   │   │       ├── LunarCalendar.kt     # 农历算法
│   │   │   │       ├── RecurrenceRule.kt    # RRULE解析
│   │   │   │       └── DateUtils.kt
│   │   │   ├── ui/                          # UI层
│   │   │   │   ├── theme/                   # 主题
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   └── Type.kt
│   │   │   │   ├── navigation/              # 导航
│   │   │   │   │   └── NavGraph.kt
│   │   │   │   ├── calendar/                # 日历视图
│   │   │   │   │   ├── month/
│   │   │   │   │   │   ├── MonthView.kt
│   │   │   │   │   │   └── MonthViewModel.kt
│   │   │   │   │   ├── week/
│   │   │   │   │   │   ├── WeekView.kt
│   │   │   │   │   │   └── WeekViewModel.kt
│   │   │   │   │   ├── day/
│   │   │   │   │   │   ├── DayView.kt
│   │   │   │   │   │   └── DayViewModel.kt
│   │   │   │   │   └── schedule/
│   │   │   │   │       ├── ScheduleView.kt
│   │   │   │   │       └── ScheduleViewModel.kt
│   │   │   │   ├── event/                   # 事件
│   │   │   │   │   ├── EventDetailScreen.kt
│   │   │   │   │   ├── EventEditScreen.kt
│   │   │   │   │   └── EventViewModel.kt
│   │   │   │   ├── task/                    # 待办
│   │   │   │   │   ├── TaskListScreen.kt
│   │   │   │   │   ├── TaskEditScreen.kt
│   │   │   │   │   └── TaskViewModel.kt
│   │   │   │   ├── shared/                  # 共享
│   │   │   │   │   ├── CalendarManageScreen.kt
│   │   │   │   │   ├── InviteScreen.kt
│   │   │   │   │   └── MemberListScreen.kt
│   │   │   │   ├── settings/                # 设置
│   │   │   │   │   ├── SettingsScreen.kt
│   │   │   │   │   └── SettingsViewModel.kt
│   │   │   │   ├── auth/                    # 认证
│   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   ├── RegisterScreen.kt
│   │   │   │   │   └── AuthViewModel.kt
│   │   │   │   ├── common/                  # 通用组件
│   │   │   │   │   ├── CalendarHeader.kt
│   │   │   │   │   ├── EventCard.kt
│   │   │   │   │   ├── TaskCard.kt
│   │   │   │   │   ├── WeatherWidget.kt
│   │   │   │   │   ├── HolidayBadge.kt
│   │   │   │   │   └── ColorPicker.kt
│   │   │   │   └── MainActivity.kt
│   │   │   ├── service/                     # 后台服务
│   │   │   │   ├── SyncWorker.kt            # 同步Worker
│   │   │   │   ├── ReminderReceiver.kt      # 提醒广播
│   │   │   │   └── WebSocketService.kt      # WebSocket服务
│   │   │   └── CalSyncApplication.kt
│   │   ├── res/
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── themes.xml
│   │   │   └── xml/
│   │   │       └── network_security_config.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts                       # 根构建文件
├── settings.gradle.kts
└── gradle.properties
```

---

## 八、第三方服务

| 服务 | 用途 | 推荐方案 |
|------|------|----------|
| 节假日API | 中国法定节假日 | 腾讯日历API / 自建数据 |
| 天气API | 天气数据 | 和风天气(免费) / OpenWeatherMap |
| 推送通知 | 系统通知 | Android Notification + WorkManager |
| 位置服务 | 获取位置 | Google FusedLocation / 高德定位 |
| 后端托管 | 服务器部署 | 当前公网服务器 123.56.177.127 |

---

## 九、开发规范

### 9.1 代码规范
- Kotlin官方风格指南
- MVVM架构分层：UI → ViewModel → Repository → Data Source
- 所有网络请求使用 suspend 函数
- 状态管理使用 StateFlow / SharedFlow

### 9.2 命名规范
- 包名: `com.calsync.app`
- ViewModel: `XxxViewModel`
- Screen: `XxxScreen`
- Entity: `XxxEntity`
- DTO: `XxxDto`

### 9.3 Git规范
- 分支: `main` / `dev` / `feature/xxx` / `fix/xxx`
- 提交: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`

---

## 十、时间估算

| 阶段 | 时间 | 累计 |
|------|------|------|
| Phase 1: 项目骨架 | 1周 | 第1周 |
| Phase 2: 核心视图 | 2周 | 第2-3周 |
| Phase 3: 事件管理 | 1.5周 | 第4-5周 |
| Phase 4: 共享与同步 | 2周 | 第6-7周 |
| Phase 5: 待办事项 | 1周 | 第8周 |
| Phase 6: 节假日与农历 | 1周 | 第9周 |
| Phase 7: 天气集成 | 0.5周 | 第10周 |
| Phase 8: 提醒引擎 | 1周 | 第11周 |
| Phase 9: 搜索与设置 | 0.5周 | 第12周 |
| Phase 10: 测试与优化 | 1周 | 第13周 |
| **总计** | **约13周** | |

---

## 十一、风险与应对

| 风险 | 影响 | 应对 |
|------|------|------|
| WebSocket断连 | 同步失败 | 断连重连 + 轮询兜底 |
| 提醒不准 | 用户体验差 | AlarmManager + WorkManager双保险 |
| 数据冲突 | 数据不一致 | 版本号 + 最后写入优先 |
| 节假日API变更 | 数据不可用 | 内置基础数据 + API备选 |
| 后台限制 | 同步/提醒被杀 | Foreground Service + 厂商白名单 |

---

_文档版本: v1.0 | 创建日期: 2026-04-24_
