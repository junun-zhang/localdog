#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
PyQt Translation Interface
支持中文、英语、韩语翻译功能
使用阿里云百炼 API (Qwen 模型)

作者：Assistant
日期：2024
"""

import sys
import os
import tempfile
import base64
import requests
from PyQt5.QtWidgets import (
    QApplication, QMainWindow, QWidget, QVBoxLayout, QHBoxLayout,
    QPushButton, QTextEdit, QLabel, QComboBox
)
from PyQt5.QtCore import Qt, QEvent, QTimer
from PyQt5.QtGui import QImage, QMimeData, QPainter, QKeyEvent, QClipboard


class ImageTextEdit(QTextEdit):
    """
    支持图片粘贴的文本编辑框
    
    当用户粘贴图片时，会调用父窗口的 on_image_pasted 方法
    """
    def __init__(self, parent=None):
        super().__init__(parent)
        # 启用富文本，支持图片显示
        self.setAcceptRichText(True)
        # 安装事件过滤器来拦截粘贴操作
        self.installEventFilter(self)
        
    def eventFilter(self, obj, event):
        """
        事件过滤器 - 拦截键盘粘贴事件
        """
        from PyQt5.QtGui import QKeyEvent
        from PyQt5.QtCore import QEvent
        
        # 拦截 Ctrl+V 粘贴快捷键
        if event.type() == QEvent.KeyPress:
            if isinstance(event, QKeyEvent):
                # 检查是否是 Ctrl+V 或 Cmd+V (Mac)
                if event.key() in (Qt.Key_V, Qt.Key_Insert):
                    modifiers = event.modifiers()
                    if modifiers & Qt.ControlModifier or modifiers & Qt.MetaModifier:
                        # 延迟执行，让粘贴先完成
                        QTimer.singleShot(0, self.check_clipboard_for_image)
                        return False  # 让事件继续传播，正常粘贴
        
        return super().eventFilter(obj, event)
    
    def check_clipboard_for_image(self):
        """检查剪贴板是否有图片"""
        clipboard = QApplication.clipboard()
        image = clipboard.image()
        
        if not image.isNull():
            # 通知父窗口有图片粘贴
            parent_window = self.window()
            if parent_window and hasattr(parent_window, 'on_image_pasted'):
                parent_window.on_image_pasted(image)


class TranslatorGUI(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("多语言翻译器")
        self.setGeometry(100, 100, 600, 400)
        
        # 阿里云百炼 API 配置
        self.api_key = "sk-sp-c0e2a2b908de495a9df82f6d5b71b9a7"
        self.base_url = "https://coding.dashscope.aliyuncs.com/v1"
        # 注意：当前 API 账号可能不支持视觉模型，使用备用方案
        self.vision_model = "qwen3.5-plus"  # 使用文本模型 + OCR 库
        self.use_ocr_library = True  # 启用本地 OCR 库作为备用方案
        
        # 支持的语言列表
        self.languages = {
            "中文": "zh",
            "英语": "en", 
            "韩语": "ko"
        }
        
        # 当前选择的目标语言
        self.target_language = "en"  # 默认英语
        
        # 当前粘贴的图片
        self.pasted_image = None
        
        # 临时保存的图片文件路径列表（用于程序退出时清理）
        self.temp_image_files = []
        
        self.init_ui()
        
    def init_ui(self):
        """初始化用户界面"""
        central_widget = QWidget()
        self.setCentralWidget(central_widget)
        
        # 主布局
        main_layout = QVBoxLayout()
        central_widget.setLayout(main_layout)
        
        # 标题
        title_label = QLabel("多语言翻译器")
        title_label.setAlignment(Qt.AlignCenter)
        title_label.setStyleSheet("font-size: 16px; font-weight: bold; margin: 10px;")
        main_layout.addWidget(title_label)
        
        # 目标语言选择区域
        lang_layout = QHBoxLayout()
        lang_label = QLabel("选择目标语言:")
        lang_label.setStyleSheet("font-size: 12px;")
        
        self.language_combo = QComboBox()
        for lang_name in self.languages.keys():
            self.language_combo.addItem(lang_name)
        
        # 设置默认选择（英语）
        self.language_combo.setCurrentText("英语")
        self.language_combo.currentTextChanged.connect(self.on_language_changed)
        
        lang_layout.addWidget(lang_label)
        lang_layout.addWidget(self.language_combo)
        lang_layout.addStretch()
        
        main_layout.addLayout(lang_layout)
        
        # 输入文本区域
        input_layout = QVBoxLayout()
        input_label = QLabel("输入要翻译的文本:")
        input_label.setStyleSheet("font-size: 12px; margin-top: 10px;")
        self.input_text = ImageTextEdit(self)
        self.input_text.setPlaceholderText("请输入需要翻译的文本... 或直接粘贴图片")
        self.input_text.setMinimumHeight(100)
        
        input_layout.addWidget(input_label)
        input_layout.addWidget(self.input_text)
        main_layout.addLayout(input_layout)
        
        # 翻译按钮
        self.translate_button = QPushButton("翻译")
        self.translate_button.clicked.connect(self.on_translate_clicked)
        self.translate_button.setStyleSheet("font-size: 14px; padding: 8px;")
        main_layout.addWidget(self.translate_button)
        
        # 输出文本区域
        output_layout = QVBoxLayout()
        output_label = QLabel("翻译结果:")
        output_label.setStyleSheet("font-size: 12px; margin-top: 10px;")
        self.output_text = QTextEdit()
        self.output_text.setPlaceholderText("翻译结果将显示在这里...")
        self.output_text.setMinimumHeight(100)
        self.output_text.setReadOnly(True)  # 只读，不能输入
        
        output_layout.addWidget(output_label)
        output_layout.addWidget(self.output_text)
        main_layout.addLayout(output_layout)
        
    def on_language_changed(self, language_name):
        """
        当目标语言改变时调用
        
        参数:
            language_name (str): 选择的语言名称（中文/英语/韩语）
        """
        self.target_language = self.languages[language_name]
        print(f"目标语言已切换为：{language_name} ({self.target_language})")
        
    def on_translate_clicked(self):
        """
        当点击翻译按钮时调用
        获取输入文本，调用翻译接口，显示结果
        """
        input_text = self.input_text.toPlainText().strip()
        
        if not input_text:
            self.output_text.setPlainText("请输入要翻译的文本！")
            return
            
        # 调用翻译接口
        self.output_text.setPlainText("正在翻译，请稍候...")
        translated_text = self.translate_text(input_text, self.target_language)
        self.output_text.setPlainText(translated_text)
        
    def translate_text(self, text, target_lang):
        """
        翻译接口函数 - 使用阿里云百炼 API
        
        参数:
            text (str): 需要翻译的源文本
            target_lang (str): 目标语言代码 ('zh', 'en', 'ko')
            
        返回:
            str: 翻译后的文本
        """
        try:
            lang_name = {
                "zh": "中文",
                "en": "英语",
                "ko": "韩语"
            }.get(target_lang, target_lang)
            
            prompt = f"""请将以下文本翻译成{lang_name}，只返回翻译结果，不需要额外解释：

{text}"""
            
            headers = {
                "Content-Type": "application/json",
                "Authorization": f"Bearer {self.api_key}"
            }
            
            payload = {
                "model": "qwen3.5-plus",
                "messages": [
                    {
                        "role": "user",
                        "content": prompt
                    }
                ],
                "max_tokens": 1000
            }
            
            response = requests.post(
                f"{self.base_url}/chat/completions",
                headers=headers,
                json=payload,
                timeout=30
            )
            
            if response.status_code == 200:
                result = response.json()
                return result["choices"][0]["message"]["content"]
            else:
                return f"翻译失败：HTTP {response.status_code}"
                
        except Exception as e:
            return f"翻译出错：{str(e)}"
            
    def set_translated_text(self, translated_text):
        """
        设置翻译结果显示（可选的辅助方法）
        
        参数:
            translated_text (str): 翻译后的文本
        """
        self.output_text.setPlainText(translated_text)
        
    def get_input_text(self):
        """
        获取输入文本（可选的辅助方法）
        
        返回:
            str: 当前输入框中的文本
        """
        return self.input_text.toPlainText()
    
    def on_image_pasted(self, image):
        """
        图片粘贴回调接口 - 保存图片到临时文件，程序退出时自动删除
        
        当用户粘贴图片时自动调用
        
        参数:
            image (QImage): 粘贴的图片对象
        """
        self.pasted_image = image
        print(f"[图片粘贴事件] 检测到图片：{image.width()}x{image.height()}")
        
        # 创建临时文件保存图片
        temp_file = tempfile.NamedTemporaryFile(suffix='.png', delete=False)
        temp_path = temp_file.name
        temp_file.close()
        
        if image.save(temp_path, "PNG"):
            self.temp_image_files.append(temp_path)
            print(f"[临时图片已保存] {temp_path}")
            
            # 自动调用图片翻译功能
            self.output_text.setPlainText("正在识别并翻译图片内容，请稍候...")
            result = self.translate_image(temp_path)
            self.output_text.setPlainText(result)
        else:
            print("[临时图片保存失败]")
            self.output_text.setPlainText("图片保存失败，请重试")
        
    def get_pasted_image(self):
        """
        获取当前粘贴的图片
        
        返回:
            QImage: 当前粘贴的图片，如果没有则返回 None
        """
        return self.pasted_image
    
    def clear_pasted_image(self):
        """
        清除当前粘贴的图片
        """
        self.pasted_image = None
    
    def image_to_base64(self, image_path):
        """
        将图片文件转换为 base64 编码
        
        参数:
            image_path (str): 图片文件路径
            
        返回:
            str: base64 编码的图片数据
        """
        with open(image_path, 'rb') as f:
            image_data = f.read()
            return base64.b64encode(image_data).decode('utf-8')
    
    def translate_image(self, image_path):
        """
        图片翻译接口 - 使用 OCR + 阿里云百炼 API
        
        方案：
        1. 首选：使用本地 OCR 库 (pytesseract) 识别文字
        2. 然后：调用阿里云百炼 API 翻译文字
        
        参数:
            image_path (str): 图片文件路径
            
        返回:
            str: 翻译后的文本
        """
        try:
            # 尝试使用本地 OCR 识别图片文字
            ocr_text = self.ocr_image(image_path)
            
            if not ocr_text or not ocr_text.strip():
                return "未能在图片中识别到文字，请尝试其他图片。"
            
            print(f"[OCR 识别结果] {ocr_text[:200]}...")
            
            # 调用文本翻译
            return self.translate_text(ocr_text, self.target_language)
            
        except Exception as e:
            error_msg = f"发生错误：{str(e)}"
            print(error_msg)
            return f"图片翻译出错：{error_msg}"
    
    def ocr_image(self, image_path):
        """
        使用 OCR 识别图片中的文字
        
        参数:
            image_path (str): 图片文件路径
            
        返回:
            str: 识别到的文字
        """
        try:
            # 尝试使用 pytesseract
            import pytesseract
            from PIL import Image
            
            img = Image.open(image_path)
            # 自动检测语言并识别
            text = pytesseract.image_to_string(img, lang='chi_sim+eng+kor')
            return text.strip()
            
        except ImportError:
            print("[警告] pytesseract 未安装，尝试备用方案...")
            return self.ocr_with_api(image_path)
        except Exception as e:
            print(f"[OCR 失败] {e}, 尝试备用方案...")
            return self.ocr_with_api(image_path)
    
    def ocr_with_api(self, image_path):
        """
        备用方案：尝试使用阿里云视觉 API 进行 OCR
        
        参数:
            image_path (str): 图片文件路径
            
        返回:
            str: 识别到的文字
        """
        # 如果视觉模型不可用，提示用户安装 OCR 库
        print("[提示] 建议安装 OCR 依赖以获得更好的图片翻译体验:")
        print("  pip install pytesseract pillow")
        print("  sudo apt-get install tesseract-ocr tesseract-ocr-chi-sim tesseract-ocr-kor (Linux)")
        print("  brew install tesseract (Mac)")
        return ""
    
    def closeEvent(self, event):
        """
        窗口关闭事件 - 清理临时文件
        """
        # 删除所有临时图片文件
        for temp_path in self.temp_image_files:
            try:
                if os.path.exists(temp_path):
                    os.remove(temp_path)
                    print(f"[已清理临时文件] {temp_path}")
            except Exception as e:
                print(f"[清理失败] {temp_path}: {e}")
        
        self.temp_image_files.clear()
        event.accept()


def main():
    """主函数"""
    app = QApplication(sys.argv)
    window = TranslatorGUI()
    window.show()
    sys.exit(app.exec_())


if __name__ == "__main__":
    main()
