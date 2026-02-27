# 离线词典文件目录

此目录将存放英、中、韩三语的离线词典文件。

## 词典文件格式

每个词典文件为JSON格式，结构如下：

```json
{
  "word1": "translation1",
  "word2": "translation2",
  ...
}
```

## 文件命名约定

- `en-zh.json`: 英语到中文词典
- `zh-en.json`: 中文到英语词典  
- `en-ko.json`: 英语到韩语词典
- `ko-en.json`: 韩语到英语词典
- `zh-ko.json`: 中文到韩语词典
- `ko-zh.json`: 韩语到中文词典

## 数据来源建议

1. **CC-CEDICT**: 中英开源词典 (https://www.mdbg.net/chinese/dictionary?page=cedict)
2. **Korean-English Dictionary**: 开源韩英词典项目
3. **自建小型神经网络模型**: 用于句子级翻译

## 文件大小优化

- 使用gzip压缩
- 按常用词汇优先级排序
- 支持分块加载（按需加载常用词汇）