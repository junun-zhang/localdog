/**
 * 离线OCR核心模块
 * 基于Tesseract.js实现图片文字识别
 */

// 注意：Tesseract.js需要通过npm安装并在构建时包含
// import Tesseract from 'tesseract.js';

class OfflineOCR {
  constructor() {
    this.worker = null;
    this.languages = {
      'en': 'eng',
      'zh': 'chi_sim', 
      'ko': 'kor'
    };
    this.initializedLanguages = new Set();
  }

  /**
   * 初始化OCR工作器
   */
  async initialize() {
    if (!this.worker) {
      // 动态导入Tesseract.js（避免在不支持的环境中加载）
      try {
        const Tesseract = await import('tesseract.js');
        this.worker = await Tesseract.createWorker({
          logger: m => console.log(m),
          cacheMethod: 'none', // 离线模式
          langPath: chrome.runtime.getURL('models/tesseract/')
        });
      } catch (error) {
        console.error('Failed to initialize Tesseract.js:', error);
        throw new Error('OCR engine not available');
      }
    }
  }

  /**
   * 加载指定语言的OCR模型
   * @param {string} languageCode - 语言代码 (en, zh, ko)
   */
  async loadLanguage(languageCode) {
    if (!this.worker) {
      await this.initialize();
    }

    const tesseractLang = this.languages[languageCode];
    if (!tesseractLang) {
      throw new Error(`Unsupported language: ${languageCode}`);
    }

    if (!this.initializedLanguages.has(languageCode)) {
      await this.worker.loadLanguage(tesseractLang);
      await this.worker.initialize(tesseractLang);
      this.initializedLanguages.add(languageCode);
    }
  }

  /**
   * 识别图片中的文字
   * @param {string|HTMLImageElement|HTMLCanvasElement} imageData - 图片数据
   * @param {string} languageCode - 语言代码
   * @returns {Promise<string>} 识别出的文字
   */
  async recognize(imageData, languageCode = 'auto') {
    if (!this.worker) {
      await this.initialize();
    }

    // 自动检测语言（简化版本，实际可能需要多语言识别）
    if (languageCode === 'auto') {
      languageCode = 'en'; // 默认英语
    }

    await this.loadLanguage(languageCode);
    
    const result = await this.worker.recognize(imageData);
    return result.data.text;
  }

  /**
   * 批量识别多张图片
   * @param {Array} imageDatas - 图片数据数组
   * @param {string} languageCode - 语言代码
   */
  async recognizeBatch(imageDatas, languageCode = 'auto') {
    const promises = imageDatas.map(img => this.recognize(img, languageCode));
    return Promise.all(promises);
  }

  /**
   * 终止OCR工作器，释放资源
   */
  async terminate() {
    if (this.worker) {
      await this.worker.terminate();
      this.worker = null;
      this.initializedLanguages.clear();
    }
  }

  /**
   * 检查是否支持指定语言
   */
  isLanguageSupported(languageCode) {
    return this.languages.hasOwnProperty(languageCode);
  }
}

// 导出全局实例
const ocr = new OfflineOCR();
export default ocr;