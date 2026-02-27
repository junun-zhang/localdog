class WebPageTranslator {
  constructor() {
    this.targetLanguage = 'zh'; // 默认中文
    this.translatedElements = new Set();
    this.ocrWorker = null;
  }

  async initialize() {
    // 从存储中加载用户设置
    const settings = await chrome.storage.local.get(['targetLanguage']);
    this.targetLanguage = settings.targetLanguage || 'zh';
    
    // 监听翻译请求
    chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
      if (request.action === 'translatePage') {
        this.targetLanguage = request.targetLanguage || 'zh';
        this.translateCurrentPage();
        sendResponse({status: 'started'});
      } else if (request.action === 'clearTranslation') {
        this.clearAllTranslations();
        sendResponse({status: 'cleared'});
      }
    });
  }

  // 主要翻译函数
  async translateCurrentPage() {
    // 1. 翻译纯文本内容
    await this.translateTextNodes();
    
    // 2. 处理图片中的文字
    await this.processImages();
  }

  // 翻译网页中的文字节点
  async translateTextNodes() {
    const walker = document.createTreeWalker(
      document.body,
      NodeFilter.SHOW_TEXT,
      {
        acceptNode: (node) => {
          // 过滤条件：非空、非脚本/样式、长度合理
          if (node.nodeValue.trim().length < 2) return NodeFilter.FILTER_REJECT;
          if (node.parentElement?.tagName === 'SCRIPT') return NodeFilter.FILTER_REJECT;
          if (node.parentElement?.tagName === 'STYLE') return NodeFilter.FILTER_REJECT;
          return NodeFilter.FILTER_ACCEPT;
        }
      }
    );

    const textNodes = [];
    let node;
    while (node = walker.nextNode()) {
      textNodes.push(node);
    }

    // 批量翻译
    for (const textNode of textNodes) {
      try {
        const originalText = textNode.nodeValue.trim();
        // 这里调用离线翻译（简化版本，实际会调用后台服务）
        const translatedText = this.simpleTranslate(originalText);
        
        if (translatedText && translatedText !== originalText) {
          // 创建翻译显示元素
          this.createTranslationOverlay(textNode, originalText, translatedText);
        }
      } catch (error) {
        console.warn('Translation failed:', error);
      }
    }
  }

  // 简单的翻译函数（实际会调用后台服务）
  simpleTranslate(text) {
    // 基于目标语言的简单翻译
    const translations = {
      'zh': {
        'hello': '你好',
        'world': '世界',
        'good morning': '早上好',
        'welcome': '欢迎'
      },
      'en': {
        '你好': 'hello',
        '世界': 'world', 
        '早上好': 'good morning',
        '欢迎': 'welcome'
      },
      'ko': {
        'hello': '안녕하세요',
        'world': '세계',
        'good morning': '좋은 아침',
        'welcome': '환영합니다'
      }
    };

    const lowerText = text.toLowerCase();
    if (translations[this.targetLanguage]) {
      if (translations[this.targetLanguage][text]) {
        return translations[this.targetLanguage][text];
      } else if (translations[this.targetLanguage][lowerText]) {
        return translations[this.targetLanguage][lowerText];
      }
    }
    
    // 如果没有匹配，返回原文本
    return text;
  }

  // 处理网页中的图片
  async processImages() {
    const images = document.querySelectorAll('img');
    
    for (const img of images) {
      // 跳过已处理的图片
      if (img.dataset.translated) continue;
      
      try {
        // 检查图片是否包含文字（基于尺寸、alt文本等启发式判断）
        if (this.shouldProcessImage(img)) {
          await this.processImageWithOCR(img);
        }
      } catch (error) {
        console.warn('Image processing failed:', error);
      }
    }
  }

  // 判断是否应该处理图片
  shouldProcessImage(img) {
    // 启发式规则：大图片、有alt文本、不是图标等
    const width = img.naturalWidth || img.width;
    const height = img.naturalHeight || img.height;
    
    // 排除小图标
    if (width < 50 || height < 50) return false;
    
    // 排除装饰性图片
    if (img.alt === '' && !img.title) return false;
    
    // 包含可能的文字内容
    return true;
  }

  // 处理单个图片的OCR和翻译（简化版本，实际会集成Tesseract.js）
  async processImageWithOCR(img) {
    try {
      // 模拟OCR识别（实际会使用Tesseract.js）
      const detectedText = this.simulateOCR(img);
      
      if (detectedText && detectedText.trim().length > 0) {
        // 翻译识别出的文字
        const translatedText = this.simpleTranslate(detectedText);
        
        if (translatedText && translatedText !== detectedText) {
          // 在图片下方插入翻译对照
          this.insertImageTranslation(img, detectedText, translatedText);
          
          // 标记为已处理
          img.dataset.translated = 'true';
        }
      }
    } catch (error) {
      console.error('OCR processing failed:', error);
    }
  }

  // 模拟OCR识别（实际会替换为真正的OCR）
  simulateOCR(img) {
    // 基于alt文本或图片URL的简单模拟
    if (img.alt) {
      return img.alt;
    }
    // 实际应用中这里会调用Tesseract.js进行OCR
    return '';
  }

  // 在图片下方插入翻译对照
  insertImageTranslation(img, originalText, translatedText) {
    // 创建翻译容器
    const translationContainer = document.createElement('div');
    translationContainer.className = 'offline-translator-image-translation';
    translationContainer.style.cssText = `
      margin: 10px 0;
      padding: 10px;
      background: #f8f9fa;
      border: 1px solid #dee2e6;
      border-radius: 4px;
      font-size: 14px;
      max-width: ${img.offsetWidth}px;
    `;
    
    // 原文显示
    const originalDiv = document.createElement('div');
    originalDiv.style.color = '#6c757d';
    originalDiv.style.fontSize = '12px';
    originalDiv.textContent = `原文: ${originalText}`;
    
    // 译文显示
    const translatedDiv = document.createElement('div');
    translatedDiv.style.fontWeight = 'bold';
    translatedDiv.style.color = '#212529';
    translatedDiv.textContent = `译文: ${translatedText}`;
    
    translationContainer.appendChild(originalDiv);
    translationContainer.appendChild(translatedDiv);
    
    // 插入到图片后面
    img.parentNode.insertBefore(translationContainer, img.nextSibling);
    
    // 记录以便清理
    this.translatedElements.add(translationContainer);
  }

  // 创建文字翻译覆盖层
  createTranslationOverlay(textNode, originalText, translatedText) {
    // 包装原文本节点
    const wrapper = document.createElement('span');
    wrapper.style.position = 'relative';
    wrapper.style.display = 'inline-block';
    
    // 创建翻译提示
    const tooltip = document.createElement('div');
    tooltip.className = 'offline-translator-tooltip';
    tooltip.textContent = translatedText;
    tooltip.style.cssText = `
      position: absolute;
      bottom: 100%;
      left: 0;
      background: #333;
      color: white;
      padding: 5px 8px;
      border-radius: 4px;
      font-size: 12px;
      white-space: nowrap;
      z-index: 10000;
      opacity: 0;
      transition: opacity 0.2s;
      pointer-events: none;
    `;
    
    wrapper.appendChild(textNode.cloneNode(true));
    wrapper.appendChild(tooltip);
    
    // 鼠标悬停显示翻译
    wrapper.addEventListener('mouseenter', () => {
      tooltip.style.opacity = '1';
    });
    
    wrapper.addEventListener('mouseleave', () => {
      tooltip.style.opacity = '0';
    });
    
    // 替换原文本节点
    textNode.parentNode.replaceChild(wrapper, textNode);
    this.translatedElements.add(wrapper);
  }

  // 清除所有翻译
  clearAllTranslations() {
    this.translatedElements.forEach(element => {
      if (element.parentNode) {
        element.parentNode.removeChild(element);
      }
    });
    this.translatedElements.clear();
    
    // 清除图片标记
    document.querySelectorAll('img[data-translated]').forEach(img => {
      delete img.dataset.translated;
    });
  }
}

// 初始化页面翻译器
const webPageTranslator = new WebPageTranslator();
webPageTranslator.initialize();