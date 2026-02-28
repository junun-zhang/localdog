/**
 * OpenAI 翻译器核心模块
 * 使用用户提供的 OpenAI API Key 进行高质量翻译
 */
class OpenAITranslator {
  constructor() {
    this.apiKey = null;
    this.model = 'gpt-3.5-turbo';
    this.baseUrl = 'https://api.openai.com/v1';
    this.loadSettings();
  }

  /**
   * 加载API设置
   */
  async loadSettings() {
    const settings = await chrome.storage.local.get(['openaiApiKey', 'openaiModel', 'openaiBaseUrl']);
    if (settings.openaiApiKey) {
      this.apiKey = settings.openaiApiKey;
    }
    if (settings.openaiModel) {
      this.model = settings.openaiModel;
    }
    if (settings.openaiBaseUrl) {
      this.baseUrl = settings.openaiBaseUrl;
    }
  }

  /**
   * 保存API设置
   */
  async saveSettings(apiKey, model, baseUrl) {
    await chrome.storage.local.set({
      openaiApiKey: apiKey,
      openaiModel: model || 'gpt-3.5-turbo',
      openaiBaseUrl: baseUrl || 'https://api.openai.com/v1'
    });
    this.apiKey = apiKey;
    this.model = model || 'gpt-3.5-turbo';
    this.baseUrl = baseUrl || 'https://api.openai.com/v1';
  }

  /**
   * 获取当前设置状态
   */
  async getSettingsStatus() {
    const settings = await chrome.storage.local.get(['openaiApiKey', 'openaiModel', 'openaiBaseUrl']);
    return {
      hasApiKey: !!settings.openaiApiKey,
      model: settings.openaiModel || 'gpt-3.5-turbo',
      baseUrl: settings.openaiBaseUrl || 'https://api.openai.com/v1'
    };
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

  /**
   * 执行OpenAI翻译
   * @param {string} text - 要翻译的文本
   * @param {string} sourceLang - 源语言
   * @param {string} targetLang - 目标语言
   * @returns {Promise<string>} 翻译结果
   */
  async translate(text, sourceLang, targetLang) {
    if (!this.isConfigured()) {
      throw new Error('OpenAI API key not configured');
    }

    if (sourceLang === targetLang) {
      return text;
    }

    const sourceLangName = this.getLanguageName(sourceLang);
    const targetLangName = this.getLanguageName(targetLang);

    const prompt = `Translate the following text from ${sourceLangName} to ${targetLangName}. Only provide the translation, no additional text or explanations.

Text: "${text}"

Translation:`;

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
              role: 'user',
              content: prompt
            }
          ],
          temperature: 0.1,
          max_tokens: 2000
        })
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(`OpenAI API error: ${response.status} - ${errorData.error?.message || response.statusText}`);
      }

      const result = await response.json();
      const translation = result.choices?.[0]?.message?.content?.trim() || '';

      // 移除可能的前缀（如 "Translation:"）
      return translation.replace(/^Translation:\s*/i, '');
    } catch (error) {
      console.error('OpenAI translation failed:', error);
      throw error;
    }
  }

  /**
   * 测试API连接
   */
  async testConnection() {
    if (!this.apiKey) {
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