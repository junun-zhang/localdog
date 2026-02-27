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

// 导出全局实例
const translator = new OfflineTranslator();
export default translator;