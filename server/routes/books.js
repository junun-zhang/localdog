const express = require('express');
const Book = require('../models/Book');

const router = express.Router();

// 获取分类列表
router.get('/categories', async (req, res) => {
  try {
    const categories = await Book.distinct('category');
    res.json({ success: true, data: { categories } });
  } catch (err) {
    res.status(500).json({ success: false, message: '服务器错误' });
  }
});

// 推荐书籍
router.get('/featured', async (req, res) => {
  try {
    const limit = parseInt(req.query.limit) || 10;
    const books = await Book.find()
      .sort({ rating: -1, downloadCount: -1 })
      .limit(limit);
    res.json({ success: true, data: { books } });
  } catch (err) {
    res.status(500).json({ success: false, message: '服务器错误' });
  }
});

// 热门书籍
router.get('/popular', async (req, res) => {
  try {
    const limit = parseInt(req.query.limit) || 10;
    const books = await Book.find()
      .sort({ downloadCount: -1, createdAt: -1 })
      .limit(limit);
    res.json({ success: true, data: { books } });
  } catch (err) {
    res.status(500).json({ success: false, message: '服务器错误' });
  }
});

// 获取书籍列表（带分页/搜索/分类）
router.get('/', async (req, res) => {
  try {
    const { category, search, page = 1, limit = 20 } = req.query;
    const query = {};
    if (category && category !== 'all') query.category = category;
    if (search) {
      query. = [
        { title: { : search, : 'i' } },
        { author: { : search, : 'i' } }
      ];
    }
    const skip = (parseInt(page) - 1) * parseInt(limit);
    const [books, total] = await Promise.all([
      Book.find(query).skip(skip).limit(parseInt(limit)).sort({ createdAt: -1 }),
      Book.countDocuments(query)
    ]);
    res.json({
      success: true,
      data: { books, pagination: { page: parseInt(page), limit: parseInt(limit), total, pages: Math.ceil(total / parseInt(limit)) } }
    });
  } catch (err) {
    res.status(500).json({ success: false, message: '服务器错误' });
  }
});

// 获取书籍详情
router.get('/:id', async (req, res) => {
  try {
    const book = await Book.findById(req.params.id);
    if (!book) return res.status(404).json({ success: false, message: '书籍不存在' });
    res.json({ success: true, data: book });
  } catch (err) {
    res.status(500).json({ success: false, message: '服务器错误' });
  }
});

// 下载书籍（增加下载计数）
router.get('/:id/download', async (req, res) => {
  try {
    const book = await Book.findByIdAndUpdate(
      req.params.id,
      { : { downloadCount: 1 } },
      { new: true }
    );
    if (!book) return res.status(404).json({ success: false, message: '书籍不存在' });
    res.json({
      success: true,
      data: { downloadUrl: book.file, book }
    });
  } catch (err) {
    res.status(500).json({ success: false, message: '服务器错误' });
  }
});

module.exports = router;
