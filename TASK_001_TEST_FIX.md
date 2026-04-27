# 任务 001: 修复单元测试失败

## 背景
运行 `./gradlew testDebugUnitTest` 后有 11 个测试失败，20 个通过。

## 失败分析

### 1. Log.e not mocked (约8个失败)
- **文件**: TxtParser.kt, TxtReader.kt
- **原因**: Android `android.util.Log` 类在 JVM 单元测试中未实现，调用 Log.e 抛出 RuntimeException
- **修复方案**: 在 `app/build.gradle.kts` 的 android 块中添加：
  ```kotlin
  testOptions {
      unitTests.isReturnDefaultValues = true
  }
  ```

### 2. TxtParser 章节分割断言失败
- **文件**: TxtParserTest.kt `parse chapters by chapter title pattern`
- **原因**: 章节正则匹配后分割逻辑导致第一章内容不包含预期的"开始"二字
- **修复方案**: 查看 TxtParser.kt 的 `splitIntoChapters` 方法，确保章节标题被正确包含在对应章节内容中，或调整测试断言匹配实际行为

### 3. TxtReader close 后状态断言失败
- **文件**: TxtReaderTest.kt `close resets state`
- **原因**: close() 后 chapters 清空，getCurrentPosition().chapterIndex 的断言与空列表行为不匹配
- **修复方案**: 调整测试断言

### 4. GBK 编码测试
- **文件**: TxtParserTest.kt `handle GBK encoded file`
- **原因**: JVM 环境下 Charset.forName("GBK") 可能抛出异常
- **修复方案**: 添加 Charset.isSupported 检查或使用 java.nio.charset.StandardCharsets 替代

## 要求
1. 修复 build.gradle.kts 的 testOptions 配置
2. 修复所有失败的单元测试
3. 确保 `./gradlew testDebugUnitTest` 全部通过（31 tests, 0 failed）
4. 不修改任何现有功能行为，只修复测试和配置问题
