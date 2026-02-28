/**
 * 网络翻译器模块
 * 使用免费的在线翻译API进行翻译
 */

class OnlineTranslator {
  constructor() {
    this.apiUrl = 'https://translate.googleapis.com/translate_a/single';
    this.timeout = 10000; // 10秒超时
  }

  /**
   * 执行网络翻译
   * @param {string} text - 要翻译的文本
   * @param {string} sourceLang - 源语言代码
   * @param {string} targetLang - 目标语言代码
   * @returns {Promise<string>} 翻译结果
   */
  async translate(text, sourceLang, targetLang) {
    if (sourceLang === targetLang) {
      return text;
    }

    // 处理语言代码映射（Google Translate API使用不同的代码）
    const langMap = {
      'zh': 'zh-CN',
      'en': 'en',
      'ko': 'ko'
    };

    const source = langMap[sourceLang] || sourceLang;
    const target = langMap[targetLang] || targetLang;

    try {
      const params = new URLSearchParams({
        client: 'gtx',
        sl: source,
        tl: target,
        dt: 't',
        q: text
      });

      const url = `${this.apiUrl}?${params}`;
      
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), this.timeout);

      const response = await fetch(url, {
        method: 'GET',
        signal: controller.signal
      });

      clearTimeout(timeoutId);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      
      // Google Translate API返回的数据结构
      if (data && data[0] && Array.isArray(data[0])) {
        let translatedText = '';
        for (const segment of data[0]) {
          if (Array.isArray(segment) && segment[0]) {
            translatedText += segment[0];
          }
        }
        return translatedText.trim();
      } else {
        throw new Error('Unexpected API response format');
      }
    } catch (error) {
      if (error.name === 'AbortError') {
        throw new Error('Translation request timed out');
      }
      throw error;
    }
  }

  /**
   * 检测文本语言
   * @param {string} text - 要检测的文本
   * @returns {Promise<{language: string, confidence: number}>}
   */
  async detectLanguage(text) {
    try {
      const params = new URLSearchParams({
        client: 'gtx',
        q: text
      });

      const url = `https://translate.googleapis.com/translate_a/single?client=gtx&dt=ld&q=${encodeURIComponent(text)}`;
      
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), this.timeout);

      const response = await fetch(url, {
        method: 'GET',
        signal: controller.signal
      });

      clearTimeout(timeoutId);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      
      // 解析语言检测结果
      if (data && data[2] && data[2][0]) {
        const detectedLang = data[2][0];
        // 映射回我们的语言代码
        const langReverseMap = {
          'zh-CN': 'zh',
          'zh-TW': 'zh',
          'en': 'en',
          'ko': 'ko'
        };
        const language = langReverseMap[detectedLang] || detectedLang.split('-')[0] || 'en';
        return { language, confidence: 1.0 };
      } else {
        return { language: 'en', confidence: 0 };
      }
    } catch (error) {
      if (error.name === 'AbortError') {
        throw new Error('Language detection timed out');
      }
      // 如果网络检测失败，返回默认值
      return { language: 'en', confidence: 0 };
    }
  }
}

// 导出实例
const onlineTranslator = new OnlineTranslator();
export default onlineTranslator;