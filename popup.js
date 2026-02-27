class PopupController {
  constructor() {
    this.currentTab = 'input';
    this.init();
  }

  async init() {
    await this.loadSettings();
    this.bindEvents();
    this.updateUI();
  }

  async loadSettings() {
    const settings = await chrome.storage.local.get([
      'sourceLanguage', 'targetLanguage', 'autoTranslate'
    ]);
    
    this.sourceLanguage = settings.sourceLanguage || 'auto';
    this.targetLanguage = settings.targetLanguage || 'zh';
    this.autoTranslate = settings.autoTranslate !== false; // 默认开启
  }

  bindEvents() {
    // 标签页切换
    document.querySelectorAll('.tab').forEach(tab => {
      tab.addEventListener('click', (e) => {
        this.switchTab(e.target.dataset.tab);
      });
    });

    // 语言选择
    document.getElementById('sourceLanguage').addEventListener('change', (e) => {
      this.sourceLanguage = e.target.value;
      this.saveSettings();
    });

    document.getElementById('targetLanguage').addEventListener('change', (e) => {
      this.targetLanguage = e.target.value;
      this.saveSettings();
      this.translateText(); // 自动翻译
    });

    // 交换语言
    document.getElementById('swapLanguages').addEventListener('click', () => {
      const temp = this.sourceLanguage;
      this.sourceLanguage = this.targetLanguage;
      this.targetLanguage = temp;
      
      document.getElementById('sourceLanguage').value = this.sourceLanguage;
      document.getElementById('targetLanguage').value = this.targetLanguage;
      
      this.saveSettings();
      this.swapTextAndTranslation();
    });

    // 文本输入
    document.getElementById('textInput').addEventListener('input', () => {
      if (this.sourceLanguage === 'auto') {
        this.detectLanguageAndUpdate();
      }
    });

    document.getElementById('textInput').addEventListener('keydown', (e) => {
      if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        this.translateText();
      }
    });

    // 按钮事件
    document.getElementById('translateTextBtn').addEventListener('click', () => {
      this.translateText();
    });

    document.getElementById('clearTextBtn').addEventListener('click', () => {
      this.clearText();
    });

    document.getElementById('translatePageBtn').addEventListener('click', () => {
      this.translateCurrentPage();
    });

    document.getElementById('clearPageBtn').addEventListener('click', () => {
      this.clearPageTranslation();
    });

    // 自动翻译设置
    document.getElementById('autoTranslate').addEventListener('change', (e) => {
      this.autoTranslate = e.target.checked;
      this.saveSettings();
    });
  }

  switchTab(tabName) {
    this.currentTab = tabName;
    document.querySelectorAll('.tab').forEach(tab => {
      tab.classList.toggle('active', tab.dataset.tab === tabName);
    });
    document.querySelectorAll('.tab-content').forEach(content => {
      content.classList.toggle('active', content.id === tabName + '-tab');
    });
  }

  async saveSettings() {
    await chrome.storage.local.set({
      sourceLanguage: this.sourceLanguage,
      targetLanguage: this.targetLanguage,
      autoTranslate: this.autoTranslate
    });
  }

  async translateText() {
    const inputText = document.getElementById('textInput').value.trim();
    if (!inputText) return;

    const translateBtn = document.getElementById('translateTextBtn');
    translateBtn.disabled = true;
    translateBtn.textContent = '翻译中...';

    try {
      // 发送消息到后台进行离线翻译
      const response = await chrome.runtime.sendMessage({
        action: 'translateText',
        text: inputText,
        sourceLang: this.sourceLanguage,
        targetLang: this.targetLanguage
      });

      if (response.success) {
        document.getElementById('translationOutput').textContent = response.translation;
      } else {
        document.getElementById('translationOutput').textContent = '翻译失败: ' + response.error;
      }
    } catch (error) {
      document.getElementById('translationOutput').textContent = '翻译错误: ' + error.message;
    } finally {
      translateBtn.disabled = false;
      translateBtn.textContent = '翻译';
    }
  }

  async detectLanguageAndUpdate() {
    const inputText = document.getElementById('textInput').value.trim();
    if (!inputText) return;

    try {
      const response = await chrome.runtime.sendMessage({
        action: 'detectLanguage',
        text: inputText
      });

      if (response.success && response.language) {
        this.sourceLanguage = response.language;
        document.getElementById('sourceLanguage').value = response.language;
      }
    } catch (error) {
      console.warn('Language detection failed:', error);
    }
  }

  swapTextAndTranslation() {
    const inputText = document.getElementById('textInput').value;
    const outputText = document.getElementById('translationOutput').textContent;
    
    document.getElementById('textInput').value = outputText;
    document.getElementById('translationOutput').textContent = inputText || '翻译结果将显示在这里...';
    
    // 如果有输入文本，自动翻译
    if (outputText && outputText !== '翻译结果将显示在这里...') {
      this.translateText();
    }
  }

  clearText() {
    document.getElementById('textInput').value = '';
    document.getElementById('translationOutput').textContent = '翻译结果将显示在这里...';
  }

  translateCurrentPage() {
    chrome.tabs.query({active: true, currentWindow: true}, (tabs) => {
      chrome.tabs.sendMessage(tabs[0].id, {
        action: 'translatePage',
        targetLanguage: this.targetLanguage
      });
    });
  }

  clearPageTranslation() {
    chrome.tabs.query({active: true, currentWindow: true}, (tabs) => {
      chrome.tabs.sendMessage(tabs[0].id, {
        action: 'clearTranslation'
      });
    });
  }

  updateUI() {
    document.getElementById('sourceLanguage').value = this.sourceLanguage;
    document.getElementById('targetLanguage').value = this.targetLanguage;
    document.getElementById('autoTranslate').checked = this.autoTranslate;
  }
}

// 初始化
new PopupController();