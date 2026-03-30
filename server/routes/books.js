const express = require('express');
const Book = require('../models/Book');
const fs = require('fs');
const path = require('path');

const router = express.Router();

// 获取书籍列表
router.get('/books', async (req, res) => {
  try {
    const { category, search, page = 1, limit = 20 } = req.query;
    let query = {};
    
    // 分类筛选
    if (category && category !== 'all') {
      query.category = category;
    }
    
    // 搜索功能
    if (search) {
      query.$or = [
        { title: { $regex: search, $options: 'i' } },
        { author: { $regex: search, $options: 'i' } }
      ];
    }
    
    const skip = (parseInt(page) - 1) * parseInt(limit);
    
    const books = await Book.find(query)
      .skip(skip)
      .limit(parseInt(limit))
      .sort({ createdAt: -1 });
    
    const total = await Book.countDocuments(query);
    
    res.json({
      success: true,
      data: {
        books,
        pagination: {
          page: parseInt(page),
          limit: parseInt(limit),
          total,
          pages: Math.ceil(total / parseInt(limit))
        }
      }
    });
  } catch (error) {
    console.error('获取书籍列表失败:', error);
    res.status(500).json({
      success: false,
      message: '服务器内部错误'
    });
  }
});

// 获取书籍详情
router.get('/books/:id', async (req, res) => {
  try {
    const book = await Book.findById(req.params.id);
    if (!book) {
      return res.status(404).json({
        success: false,
        message: '书籍不存在'
      });
    }
    
    res.json({
      success: true,
      data: book
    });
  } catch (error) {
    console.error('获取书籍详情失败:', error);
    res.status(500).json({
      success: false,
      message: '服务器内部错误'
    });
  }
});

// 下载书籍文件
router.get('/books/:id/download', async (req, res) => {
  try {
    const book = await Book.findById(req.params.id);
    if (!book) {
      return res.status(404).json({
        success: false,
        message: '书籍不存在'
      });
    }
    
    const filePath = path.join(__dirname, '..', 'uploads', book.file);
    if (!fs.existsSync(filePath)) {
      return res.status(404).json({
        success: false,
        message: '文件不存在'
      });
    }
    
    // 增加下载计数
    await Book.findByIdAndUpdate(req.params.id, {
      $inc: { downloadCount: 1 }
    });
    
    // 发送文件
    res.setHeader('Content-Type', book.mimeType);
    res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(book.filename)}"`);
    res.sendFile(filePath);
  } catch (error) {
    console.error('下载书籍失败:', error);
    res.status(500).json({
      success: false,
      message: '服务器内部错误'
    });
  }
});

module.exports = router;