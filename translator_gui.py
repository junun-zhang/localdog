#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
PyQt Translation Interface
支持中文、英语、韩语翻译功能

作者: Assistant
日期: 2024
"""

import sys
from PyQt5.QtWidgets import (
    QApplication, QMainWindow, QWidget, QVBoxLayout, QHBoxLayout,
    QPushButton, QTextEdit, QLabel, QComboBox
)
from PyQt5.QtCore import Qt
from PyQt5.QtGui import QImage, QMimeData, QPainter
from PyQt5.QtWidgets import QApplication


class ImageTextEdit(QTextEdit):
    """
    支持图片粘贴的文本编辑框
    
    当用户粘贴图片时，会调用父窗口的 on_image_pasted 方法
    """
    def __init__(self, parent=None):
        super().__init__(parent)
        
    def pasteEvent(self, event):
        """
        重写粘贴事件，捕获图片
        
        参数:
            event: QMimeData 事件
        """
        mime_data = event.mimeData()
        
        # 检查是否包含图片
        if mime_data.hasImage():
            # 获取图片
            image = mime_data.imageData()
            if isinstance(image, QImage):
                # 保存当前粘贴的图片
                if self.parent() and hasattr(self.parent(), 'on_image_pasted'):
                    self.parent().on_image_pasted(image)
                # 允许正常粘贴（图片会显示在文本框中）
                super().pasteEvent(event)
            else:
                super().pasteEvent(event)
        else:
            # 不是图片，正常处理
            super().pasteEvent(event)


class TranslatorGUI(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("多语言翻译器")
        self.setGeometry(100, 100, 600, 400)
        
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
        print(f"目标语言已切换为: {language_name} ({self.target_language})")
        
    def on_translate_clicked(self):
        """
        当点击翻译按钮时调用
        获取输入文本，调用翻译接口，显示结果
        """
        input_text = self.input_text.toPlainText().strip()
        
        if not input_text:
            self.output_text.setPlainText("请输入要翻译的文本！")
            return
            
        # 调用翻译接口 - 这里是你需要实现的部分
        translated_text = self.translate_text(input_text, self.target_language)
        self.output_text.setPlainText(translated_text)
        
    def translate_text(self, text, target_lang):
        """
        翻译接口函数 - 需要你来实现具体的翻译逻辑
        
        参数:
            text (str): 需要翻译的源文本
            target_lang (str): 目标语言代码 ('zh', 'en', 'ko')
            
        返回:
            str: 翻译后的文本
            
        TODO: 在这里实现你的翻译功能
        你可以使用API、本地模型或其他翻译服务
        """
        # 示例返回 - 你需要替换这部分为实际的翻译逻辑
        if target_lang == "zh":
            return f"[中文翻译结果] {text}"
        elif target_lang == "en":
            return f"[English translation result] {text}"
        elif target_lang == "ko":
            return f"[한국어 번역 결과] {text}"
        else:
            return f"[Unknown language] {text}"
            
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
        图片粘贴回调接口 - 你需要实现这个方法
        
        当用户粘贴图片时自动调用
        
        参数:
            image (QImage): 粘贴的图片对象
            
        TODO: 在这里实现你的图片翻译逻辑
        1. 保存或使用图片进行 OCR 识别
        2. 获取识别后的文字
        3. 调用翻译接口
        4. 显示翻译结果
        """
        self.pasted_image = image
        print(f"[图片粘贴事件] 检测到图片：{image.width()}x{image.height()}")
        # TODO: 在这里调用你的图片翻译功能
        # 示例：self.translate_image(image)
        
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
        
    def translate_image(self, image):
        """
        图片翻译接口 - 你需要实现这个方法
        
        参数:
            image (QImage): 需要翻译的图片
            
        返回:
            str: 翻译后的文本
            
        TODO: 实现图片翻译流程
        1. OCR 识别图片文字
        2. 调用翻译接口
        3. 返回翻译结果
        """
        # 占位实现 - 你需要替换为实际逻辑
        return "[图片翻译功能待实现]"


def main():
    """主函数"""
    app = QApplication(sys.argv)
    window = TranslatorGUI()
    window.show()
    sys.exit(app.exec_())


if __name__ == "__main__":
    main()