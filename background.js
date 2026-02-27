class BackgroundService {
  constructor() {
    this.translator = null;
    this.languageDetector = null;
    this.init();
  }

  init() {
    // 延迟初始化翻译器（按需加载）
    this.translator = new OfflineTranslator();
    this.languageDetector = new LanguageDetector();

    // 监听来自popup的消息
    chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
      if (request.action === 'translateText') {
        this.handleTextTranslation(request, sendResponse);
        return true; // 保持消息通道开放用于异步响应
      } else if (request.action === 'detectLanguage') {
        this.handleLanguageDetection(request, sendResponse);
        return true;
      }
    });

    // 监听标签页更新（用于自动翻译）
    chrome.tabs.onUpdated.addListener((tabId, changeInfo, tab) => {
      if (changeInfo.status === 'complete' && tab.url) {
        this.handleTabUpdate(tabId, tab);
      }
    });
  }

  async handleTextTranslation(request, sendResponse) {
    try {
      let sourceLang = request.sourceLang;
      
      // 自动检测语言
      if (sourceLang === 'auto') {
        const detected = await this.languageDetector.detect(request.text);
        sourceLang = detected.language;
      }

      // 执行翻译
      const translation = await this.translator.translate(
        request.text, 
        sourceLang, 
        request.targetLang
      );

      sendResponse({
        success: true,
        translation: translation
      });
    } catch (error) {
      sendResponse({
        success: false,
        error: error.message
      });
    }
  }

  async handleLanguageDetection(request, sendResponse) {
    try {
      const result = await this.languageDetector.detect(request.text);
      sendResponse({
        success: true,
        language: result.language
      });
    } catch (error) {
      sendResponse({
        success: false,
        error: error.message
      });
    }
  }

  async handleTabUpdate(tabId, tab) {
    // 检查是否应该自动翻译
    const settings = await chrome.storage.local.get(['autoTranslate', 'targetLanguage']);
    
    if (settings.autoTranslate && this.shouldAutoTranslate(tab.url)) {
      // 延迟一点时间，确保页面完全加载
      setTimeout(() => {
        chrome.tabs.sendMessage(tabId, {
          action: 'translatePage',
          targetLanguage: settings.targetLanguage || 'zh'
        }).catch(() => {
          // 忽略发送失败（比如页面不支持content script）
        });
      }, 2000);
    }
  }

  shouldAutoTranslate(url) {
    // 排除一些不应该自动翻译的网站
    const excludePatterns = [
      'chrome://',
      'chrome-extension://',
      'about:',
      'localhost',
      '127.0.0.1'
    ];
    
    return !excludePatterns.some(pattern => url.startsWith(pattern));
  }
}

// 简单的语言检测器（基于字符集）
class LanguageDetector {
  async detect(text) {
    // 移除空白字符
    const cleanText = text.trim();
    if (!cleanText) {
      return { language: 'en', confidence: 0 };
    }

    // 统计不同字符类型的数量
    let chineseCount = 0;
    let koreanCount = 0;
    let englishCount = 0;
    let otherCount = 0;

    for (let char of cleanText) {
      const code = char.charCodeAt(0);
      
      // 中文字符范围
      if ((code >= 0x4e00 && code <= 0x9fff) || 
          (code >= 0x3400 && code <= 0x4dbf) ||
          (code >= 0x20000 && code <= 0x2a6df)) {
        chineseCount++;
      }
      // 韩文字符范围
      else if ((code >= 0xac00 && code <= 0xd7af) ||
               (code >= 0x1100 && code <= 0x11ff) ||
               (code >= 0x3130 && code <= 0x318f)) {
        koreanCount++;
      }
      // 英文字符（包括标点和数字）
      else if ((code >= 0x0041 && code <= 0x005a) || // A-Z
               (code >= 0x0061 && code <= 0x007a) || // a-z
               (code >= 0x0030 && code <= 0x0039) || // 0-9
               (code >= 0x0020 && code <= 0x007e)) { // 常见标点
        englishCount++;
      }
      else {
        otherCount++;
      }
    }

    const totalChars = cleanText.length;
    const chineseRatio = chineseCount / totalChars;
    const koreanRatio = koreanCount / totalChars;
    const englishRatio = englishCount / totalChars;

    // 判断主要语言
    if (chineseRatio > 0.3) {
      return { language: 'zh', confidence: chineseRatio };
    } else if (koreanRatio > 0.3) {
      return { language: 'ko', confidence: koreanRatio };
    } else {
      return { language: 'en', confidence: englishRatio };
    }
  }
}

// 离线翻译器基类（后续实现具体逻辑）
class OfflineTranslator {
  async translate(text, sourceLang, targetLang) {
    // 这里将实现具体的离线翻译逻辑
    // 目前返回一个占位符
    if (sourceLang === targetLang) {
      return text;
    }
    
    // 简单的测试翻译（实际会使用词典或模型）
    const testTranslations = {
      'en-zh': { 'hello': '你好', 'world': '世界', 'good morning': '早上好' },
      'zh-en': { '你好': 'hello', '世界': 'world', '早上好': 'good morning' },
      'en-ko': { 'hello': '안녕하세요', 'world': '세계', 'good morning': '좋은 아침' },
      'ko-en': { '안녕하세요': 'hello', '세계': 'world', '좋은 아침': 'good morning' },
      'zh-ko': { '你好': '안녕하세요', '世界': '세계', '早上好': '좋은 아침' },
      'ko-zh': { '안녕하세요': '你好', '세계': '世界', '좋은 아침': '早上好' }
    };

    const key = `${sourceLang}-${targetLang}`;
    const lowerText = text.toLowerCase();
    
    if (testTranslations[key] && testTranslations[key][text]) {
      return testTranslations[key][text];
    } else if (testTranslations[key] && testTranslations[key][lowerText]) {
      return testTranslations[key][lowerText];
    }

    // 如果没有匹配，返回原文本（实际应用中会使用更复杂的翻译逻辑）
    return `[${sourceLang}→${targetLang}] ${text}`;
  }
}

// 初始化后台服务
new BackgroundService();