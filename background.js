/**
 * 离线翻译器核心模块
 * 负责加载词典文件并执行翻译
 */
class OfflineTranslator {
  constructor() {
    this.dictionaries = new Map();
    this.loadedLanguages = new Set();
  }

  /**
   * 加载指定语言对的词典
   * @param {string} sourceLang - 源语言代码 (en, zh, ko)
   * @param {string} targetLang - 目标语言代码 (en, zh, ko)
   */
  async loadDictionary(sourceLang, targetLang) {
    const dictKey = `${sourceLang}-${targetLang}`;
    
    if (this.dictionaries.has(dictKey)) {
      return this.dictionaries.get(dictKey);
    }

    try {
      // 构建词典文件路径
      const dictPath = chrome.runtime.getURL(`lib/dictionary/${dictKey}.json`);
      
      // 加载词典文件
      const response = await fetch(dictPath);
      if (!response.ok) {
        throw new Error(`Failed to load dictionary: ${dictPath}`);
      }
      
      const dictionary = await response.json();
      this.dictionaries.set(dictKey, dictionary);
      this.loadedLanguages.add(dictKey);
      
      return dictionary;
    } catch (error) {
      console.error(`Error loading dictionary ${dictKey}:`, error);
      // 返回空词典，避免完全失败
      this.dictionaries.set(dictKey, {});
      return {};
    }
  }

  /**
   * 执行文本翻译
   * @param {string} text - 要翻译的文本
   * @param {string} sourceLang - 源语言
   * @param {string} targetLang - 目标语言
   * @returns {Promise<string>} 翻译结果
   */
  async translate(text, sourceLang, targetLang) {
    // 如果源语言和目标语言相同，直接返回原文
    if (sourceLang === targetLang) {
      return text;
    }

    // 加载对应的词典
    const dictionary = await this.loadDictionary(sourceLang, targetLang);
    
    // 简单的单词匹配翻译（实际应用中需要更复杂的句子处理）
    const words = text.split(/\s+/);
    const translatedWords = words.map(word => {
      // 移除标点符号进行匹配
      const cleanWord = word.replace(/[^\w\u4e00-\u9fff\uac00-\ud7af]/g, '');
      
      if (cleanWord && dictionary[cleanWord]) {
        // 保留原始标点符号
        const punctuation = word.replace(cleanWord, '');
        return dictionary[cleanWord] + punctuation;
      }
      
      return word; // 未找到翻译的词保持原样
    });

    return translatedWords.join(' ');
  }

  /**
   * 批量翻译多个文本
   * @param {string[]} texts - 文本数组
   * @param {string} sourceLang - 源语言
   * @param {string} targetLang - 目标语言
   */
  async translateBatch(texts, sourceLang, targetLang) {
    const promises = texts.map(text => this.translate(text, sourceLang, targetLang));
    return Promise.all(promises);
  }

  /**
   * 清理缓存的词典数据
   */
  clearCache() {
    this.dictionaries.clear();
    this.loadedLanguages.clear();
  }

  /**
   * 检查指定语言对是否已加载
   */
  isDictionaryLoaded(sourceLang, targetLang) {
    return this.loadedLanguages.has(`${sourceLang}-${targetLang}`);
  }
}

/**
 * OpenAI翻译器核心模块
 * 使用用户提供的 OpenAI API Key 进行高质量翻译
 */
class OpenAITranslator {
  constructor() {
    this.apiKey = null;
    this.model = 'gpt-3.5-turbo';
    this.baseUrl = 'https://api.openai.com/v1';
    this.loadSettings();
  }

  async loadSettings() {
    try {
      const settings = await chrome.storage.local.get(['openaiApiKey', 'openaiModel', 'openaiBaseUrl']);
      this.apiKey = settings.openaiApiKey || null;
      this.model = settings.openaiModel || 'gpt-3.5-turbo';
      this.baseUrl = settings.openaiBaseUrl || 'https://api.openai.com/v1';
    } catch (error) {
      console.warn('Failed to load OpenAI settings:', error);
    }
  }

  async saveSettings(apiKey, model, baseUrl) {
    try {
      this.apiKey = apiKey;
      this.model = model || 'gpt-3.5-turbo';
      this.baseUrl = baseUrl || 'https://api.openai.com/v1';
      await chrome.storage.local.set({
        openaiApiKey: apiKey,
        openaiModel: model || 'gpt-3.5-turbo',
        openaiBaseUrl: baseUrl || 'https://api.openai.com/v1'
      });
    } catch (error) {
      console.error('Failed to save OpenAI settings:', error);
      throw error;
    }
  }

  isConfigured() {
    return this.apiKey && this.apiKey.trim() !== '';
  }

  getLanguageName(langCode) {
    const langMap = {
      'en': 'English',
      'zh': 'Chinese',
      'ko': 'Korean'
    };
    return langMap[langCode] || langCode;
  }

  async translate(text, sourceLang, targetLang) {
    if (!this.isConfigured()) {
      throw new Error('OpenAI API key not configured');
    }

    if (sourceLang === targetLang) {
      return text;
    }

    const sourceLangName = this.getLanguageName(sourceLang);
    const targetLangName = this.getLanguageName(targetLang);

    const prompt = `Translate the following text from ${sourceLangName} to ${targetLangName}:\n\n"${text}"\n\nTranslation:`;

    try {
      const response = await fetch(`${this.baseUrl}/chat/completions`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${this.apiKey}`
        },
        body: JSON.stringify({
          model: this.model,
          messages: [
            {
              role: 'system',
              content: 'You are a professional translator. Provide accurate and natural translations.'
            },
            {
              role: 'user',
              content: prompt
            }
          ],
          temperature: 0.3,
          max_tokens: 1000
        })
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(`OpenAI API error: ${response.status} - ${errorData.error?.message || response.statusText}`);
      }

      const data = await response.json();
      const translation = data.choices[0].message.content.trim();
      
      // 移除可能的引号包装
      return translation.replace(/^["'](.*)["']$/, '$1');
    } catch (error) {
      console.error('OpenAI translation failed:', error);
      throw error;
    }
  }

  async testConnection() {
    if (!this.isConfigured()) {
      return { success: false, error: 'API key not configured' };
    }

    try {
      const response = await fetch(`${this.baseUrl}/models`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${this.apiKey}`
        }
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        return { 
          success: false, 
          error: `API test failed: ${response.status} - ${errorData.error?.message || response.statusText}` 
        };
      }

      return { success: true, message: 'API connection successful' };
    } catch (error) {
      return { success: false, error: `Connection test failed: ${error.message}` };
    }
  }
}

/**
 * 网络翻译器核心模块
 * 使用免费的在线翻译API进行翻译
 */
class OnlineTranslator {
  constructor() {
    // 支持的语言映射
    this.languageMap = {
      'en': 'en',
      'zh': 'zh',
      'ko': 'ko'
    };
  }

  /**
   * 执行网络翻译
   * @param {string} text - 要翻译的文本
   * @param {string} sourceLang - 源语言
   * @param {string} targetLang - 目标语言
   * @returns {Promise<string>} 翻译结果
   */
  async translate(text, sourceLang, targetLang) {
    if (sourceLang === targetLang) {
      return text;
    }

    try {
      // 验证语言支持
      if (!this.languageMap[sourceLang] || !this.languageMap[targetLang]) {
        throw new Error(`Unsupported language pair: ${sourceLang} -> ${targetLang}`);
      }

      // 使用 LibreTranslate 免费API（无需API密钥）
      const apiUrl = 'https://libretranslate.de/translate';
      
      const response = await fetch(apiUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          q: text,
          source: this.languageMap[sourceLang],
          target: this.languageMap[targetLang],
          format: 'text'
        })
      });

      if (!response.ok) {
        // 尝试备用API
        const backupResponse = await this.tryBackupTranslation(text, sourceLang, targetLang);
        if (backupResponse) {
          return backupResponse;
        }
        throw new Error(`Translation API error: ${response.status} ${response.statusText}`);
      }

      const result = await response.json();
      return result.translatedText || result.translation || '';
    } catch (error) {
      console.error('Online translation failed:', error);
      // 如果网络翻译失败，回退到离线翻译
      const offlineTranslator = new OfflineTranslator();
      return await offlineTranslator.translate(text, sourceLang, targetLang);
    }
  }

  /**
   * 尝试备用翻译API
   */
  async tryBackupTranslation(text, sourceLang, targetLang) {
    try {
      // 使用 Google Translate 的免费替代方案
      const backupUrl = `https://api.mymemory.translated.net/get?q=${encodeURIComponent(text)}&langpair=${sourceLang}|${targetLang}`;
      const response = await fetch(backupUrl);
      
      if (response.ok) {
        const result = await response.json();
        if (result.responseData && result.responseData.translatedText) {
          return result.responseData.translatedText;
        }
      }
    } catch (backupError) {
      console.warn('Backup translation also failed:', backupError);
    }
    return null;
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

class BackgroundService {
  constructor() {
    this.offlineTranslator = null;
    this.onlineTranslator = null;
    this.openaiTranslator = null;
    this.languageDetector = null;
    this.init();
  }

  init() {
    // 初始化翻译器和语言检测器
    this.offlineTranslator = new OfflineTranslator();
    this.onlineTranslator = new OnlineTranslator();
    this.openaiTranslator = new OpenAITranslator();
    this.languageDetector = new LanguageDetector();

    // 监听来自popup的消息
    chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
      if (request.action === 'translateText') {
        this.handleTextTranslation(request, sendResponse);
        return true; // 保持消息通道开放用于异步响应
      } else if (request.action === 'detectLanguage') {
        this.handleLanguageDetection(request, sendResponse);
        return true;
      } else if (request.action === 'getTranslationMode') {
        this.handleGetTranslationMode(sendResponse);
        return true;
      } else if (request.action === 'setTranslationMode') {
        this.handleSetTranslationMode(request.mode, sendResponse);
        return true;
      } else if (request.action === 'getOpenAISettings') {
        this.handleGetOpenAISettings(sendResponse);
        return true;
      } else if (request.action === 'saveOpenAISettings') {
        this.handleSaveOpenAISettings(request.apiKey, request.model, request.baseUrl, sendResponse);
        return true;
      } else if (request.action === 'testOpenAIConnection') {
        this.handleTestOpenAIConnection(sendResponse);
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

  async handleGetTranslationMode(sendResponse) {
    try {
      const settings = await chrome.storage.local.get(['translationMode']);
      sendResponse({
        success: true,
        mode: settings.translationMode || 'online-priority'
      });
    } catch (error) {
      sendResponse({
        success: false,
        error: error.message
      });
    }
  }

  async handleSetTranslationMode(mode, sendResponse) {
    try {
      await chrome.storage.local.set({ translationMode: mode });
      sendResponse({ success: true });
    } catch (error) {
      sendResponse({
        success: false,
        error: error.message
      });
    }
  }

  async handleGetOpenAISettings(sendResponse) {
    try {
      const settings = await chrome.storage.local.get(['openaiApiKey', 'openaiModel', 'openaiBaseUrl']);
      sendResponse({
        success: true,
        apiKey: settings.openaiApiKey || '',
        model: settings.openaiModel || 'gpt-3.5-turbo',
        baseUrl: settings.openaiBaseUrl || 'https://api.openai.com/v1'
      });
    } catch (error) {
      sendResponse({
        success: false,
        error: error.message
      });
    }
  }

  async handleSaveOpenAISettings(apiKey, model, baseUrl, sendResponse) {
    try {
      await this.openaiTranslator.saveSettings(apiKey, model, baseUrl);
      sendResponse({ success: true });
    } catch (error) {
      sendResponse({
        success: false,
        error: error.message
      });
    }
  }

  async handleTestOpenAIConnection(sendResponse) {
    try {
      const result = await this.openaiTranslator.testConnection();
      sendResponse(result);
    } catch (error) {
      sendResponse({
        success: false,
        error: error.message
      });
    }
  }

  async handleTextTranslation(request, sendResponse) {
    try {
      let sourceLang = request.sourceLang;
      
      // 自动检测语言
      if (sourceLang === 'auto') {
        const detected = await this.languageDetector.detect(request.text);
        sourceLang = detected.language;
      }

      // 获取当前翻译模式
      const modeSettings = await chrome.storage.local.get(['translationMode']);
      const translationMode = modeSettings.translationMode || 'online-priority';

      let translation;
      
      switch (translationMode) {
        case 'openai':
          if (this.openaiTranslator.isConfigured()) {
            try {
              translation = await this.openaiTranslator.translate(
                request.text, 
                sourceLang, 
                request.targetLang
              );
            } catch (openaiError) {
              // OpenAI失败，回退到网络翻译
              translation = await this.onlineTranslator.translate(
                request.text, 
                sourceLang, 
                request.targetLang
              );
            }
          } else {
            // OpenAI未配置，使用网络翻译
            translation = await this.onlineTranslator.translate(
              request.text, 
              sourceLang, 
              request.targetLang
            );
          }
          break;
          
        case 'online-only':
          translation = await this.onlineTranslator.translate(
            request.text, 
            sourceLang, 
            request.targetLang
          );
          break;
          
        case 'offline-only':
          translation = await this.offlineTranslator.translate(
            request.text, 
            sourceLang, 
            request.targetLang
          );
          break;
          
        case 'online-priority':
        default:
          // 默认：优先使用网络翻译，失败则回退到离线
          try {
            translation = await this.onlineTranslator.translate(
              request.text, 
              sourceLang, 
              request.targetLang
            );
          } catch (onlineError) {
            console.warn('Online translation failed, falling back to offline:', onlineError);
            translation = await this.offlineTranslator.translate(
              request.text, 
              sourceLang, 
              request.targetLang
            );
          }
          break;
      }

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

// 初始化后台服务
new BackgroundService();