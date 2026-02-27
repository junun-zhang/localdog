#!/bin/bash

# 构建Chrome扩展程序
echo "Building Offline Translator Pro Chrome Extension..."

# 创建构建目录
mkdir -p build

# 复制所有必要文件
cp -r manifest.json popup.html popup.js background.js content.js lib/ models/ assets/ build/

# 如果存在node_modules，复制tesseract.js相关文件
if [ -d "node_modules/tesseract.js" ]; then
  mkdir -p build/node_modules/tesseract.js
  cp -r node_modules/tesseract.js/dist build/node_modules/tesseract.js/
fi

# 创建zip包
cd build
zip -r ../offline-translator-pro.zip .
cd ..

echo "Build completed! Package: offline-translator-pro.zip"
echo "You can load the 'build' folder in Chrome Extensions or upload the zip file."