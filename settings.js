class SettingsController {
  constructor() {
    this.init();
  }

  async init() {
    await this.loadSettings();
    this.bindEvents();
  }

  async loadSettings() {
    const settings = await chrome.storage.local.get([
      'openaiApiKey', 
      'openaiModel', 
      'openaiBaseUrl',
      'translationMode'
    ]);
    
    document.getElementById('apiKey').value = settings.openaiApiKey || '';
    document.getElementById('modelId').value = settings.openaiModel || 'gpt-3.5-turbo';
    document.getElementById('baseUrl').value = settings.openaiBaseUrl || 'https://api.openai.com/v1';
    document.getElementById('translationMode').value = settings.translationMode || 'auto';
  }

  bindEvents() {
    document.getElementById('saveBtn').addEventListener('click', () => {
      this.saveSettings();
    });

    document.getElementById('testBtn').addEventListener('click', () => {
      this.testConnection();
    });

    document.getElementById('clearBtn').addEventListener('click', () => {
      this.clearSettings();
    });
  }

  async saveSettings() {
    const apiKey = document.getElementById('apiKey').value.trim();
    const modelId = document.getElementById('modelId').value.trim();
    const baseUrl = document.getElementById('baseUrl').value.trim();
    const translationMode = document.getElementById('translationMode').value;

    if (translationMode === 'openai' && !apiKey) {
      this.showStatus('请先配置 OpenAI API Key', 'error');
      return;
    }

    // 验证URL格式
    try {
      new URL(baseUrl);
    } catch (e) {
      this.showStatus('OpenAI URL 格式不正确', 'error');
      return;
    }

    await chrome.storage.local.set({
      openaiApiKey: apiKey,
      openaiModel: modelId || 'gpt-3.5-turbo',
      openaiBaseUrl: baseUrl || 'https://api.openai.com/v1',
      translationMode: translationMode
    });

    this.showStatus('设置保存成功！', 'success');
    
    // 通知后台服务重新加载设置
    chrome.runtime.sendMessage({
      action: 'reloadSettings'
    });
  }

  async testConnection() {
    const apiKey = document.getElementById('apiKey').value.trim();
    const baseUrl = document.getElementById('baseUrl').value.trim();

    if (!apiKey) {
      this.showStatus('请先配置 OpenAI API Key', 'error');
      return;
    }

    if (!baseUrl) {
      this.showStatus('请先配置 OpenAI URL', 'error');
      return;
    }

    this.showStatus('正在测试连接...', 'success');
    
    try {
      const response = await fetch(`${baseUrl}/models`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${apiKey}`
        }
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(`API test failed: ${response.status} - ${errorData.error?.message || response.statusText}`);
      }

      this.showStatus('✅ 连接测试成功！API 配置正确。', 'success');
    } catch (error) {
      console.error('Connection test failed:', error);
      this.showStatus(`❌ 连接测试失败: ${error.message}`, 'error');
    }
  }

  async clearSettings() {
    await chrome.storage.local.remove([
      'openaiApiKey', 
      'openaiModel', 
      'openaiBaseUrl',
      'translationMode'
    ]);
    
    document.getElementById('apiKey').value = '';
    document.getElementById('modelId').value = 'gpt-3.5-turbo';
    document.getElementById('baseUrl').value = 'https://api.openai.com/v1';
    document.getElementById('translationMode').value = 'auto';
    
    this.showStatus('设置已清除', 'success');
    
    // 通知后台服务重新加载设置
    chrome.runtime.sendMessage({
      action: 'reloadSettings'
    });
  }

  showStatus(message, type) {
    const statusEl = document.getElementById('status');
    statusEl.textContent = message;
    statusEl.className = `status ${type}`;
    statusEl.style.display = 'block';
    
    // 3秒后自动隐藏
    setTimeout(() => {
      statusEl.style.display = 'none';
    }, 3000);
  }
}

// 初始化设置控制器
new SettingsController();