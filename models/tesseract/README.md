# Tesseract OCR 模型文件目录

此目录将存放Tesseract.js的离线OCR模型文件。

## 模型文件

需要下载以下语言的Tesseract模型文件：

- `eng.traineddata`: 英语OCR模型 (~10MB)
- `chi_sim.traineddata`: 中文简体OCR模型 (~30MB)  
- `kor.traineddata`: 韩语OCR模型 (~40MB)

## 下载地址

模型文件可以从Tesseract官方仓库下载：
https://github.com/tesseract-ocr/tessdata

## 文件管理策略

- **按需下载**: 用户首次使用某语言时才下载对应模型
- **本地缓存**: 使用Chrome Storage API缓存已下载的模型
- **清理机制**: 提供手动清理缓存的选项

## 集成方式

在`lib/ocr.js`中将集成Tesseract.js，通过以下方式加载模型：

```javascript
import Tesseract from 'tesseract.js';

const worker = await Tesseract.createWorker({
  langPath: chrome.runtime.getURL('models/tesseract/'),
  cacheMethod: 'none' // 离线模式
});
```