/**
 * 下载Tesseract OCR模型文件的脚本
 * 运行此脚本将自动下载所需的离线OCR模型
 */

const fs = require('fs');
const path = require('path');
const https = require('https');

// 模型配置
const models = [
  {
    name: 'eng',
    url: 'https://github.com/tesseract-ocr/tessdata/raw/main/eng.traineddata',
    size: '~10MB'
  },
  {
    name: 'chi_sim',
    url: 'https://github.com/tesseract-ocr/tessdata/raw/main/chi_sim.traineddata',
    size: '~30MB'
  },
  {
    name: 'kor',
    url: 'https://github.com/tesseract-ocr/tessdata/raw/main/kor.trieddata',
    size: '~40MB'
  }
];

// 修正韩语模型URL（上面有个拼写错误）
models[2].url = 'https://github.com/tesseract-ocr/tessdata/raw/main/kor.traineddata';

const modelDir = path.join(__dirname, 'models', 'tesseract');

// 确保目录存在
if (!fs.existsSync(modelDir)) {
  fs.mkdirSync(modelDir, { recursive: true });
}

async function downloadModel(model) {
  const filePath = path.join(modelDir, `${model.name}.traineddata`);
  
  // 检查是否已存在
  if (fs.existsSync(filePath)) {
    console.log(`✅ ${model.name} 模型已存在，跳过下载`);
    return;
  }

  console.log(`📥 正在下载 ${model.name} 模型 (${model.size})...`);
  
  return new Promise((resolve, reject) => {
    const file = fs.createWriteStream(filePath);
    let downloaded = 0;
    
    https.get(model.url, (response) => {
      if (response.statusCode !== 200) {
        reject(new Error(`HTTP ${response.statusCode}: ${response.statusMessage}`));
        return;
      }
      
      response.pipe(file);
      
      response.on('data', (chunk) => {
        downloaded += chunk.length;
        // 显示进度（简化版）
        if (downloaded % (1024 * 1024) === 0) {
          console.log(`   已下载: ${(downloaded / 1024 / 1024).toFixed(1)} MB`);
        }
      });
      
      file.on('finish', () => {
        file.close();
        console.log(`✅ ${model.name} 模型下载完成`);
        resolve();
      });
      
      file.on('error', (err) => {
        fs.unlink(filePath, () => {}); // 删除失败的文件
        reject(err);
      });
    }).on('error', (err) => {
      reject(err);
    });
  });
}

async function main() {
  console.log('🚀 开始下载Tesseract OCR模型文件...\n');
  
  try {
    for (const model of models) {
      await downloadModel(model);
      console.log('');
    }
    
    console.log('🎉 所有模型下载完成！');
    console.log('\n📁 模型文件位置:');
    console.log(`   ${modelDir}/`);
    console.log('\n💡 使用说明:');
    console.log('   1. 将这些模型文件包含在Chrome插件包中');
    console.log('   2. 在lib/ocr.js中配置正确的模型路径');
    console.log('   3. 插件将能够离线识别英、中、韩三种语言');
    
  } catch (error) {
    console.error('❌ 下载失败:', error.message);
    console.error('\n🔧 手动下载方法:');
    console.error('   访问 https://github.com/tesseract-ocr/tessdata');
    console.log('   下载以下文件到 models/tesseract/ 目录:');
    models.forEach(model => {
      console.log(`   - ${model.name}.traineddata`);
    });
  }
}

// 如果直接运行此脚本
if (require.main === module) {
  main();
}