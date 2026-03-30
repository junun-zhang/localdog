const express = require('express');
const cors = require('cors');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// Mock book data
let books = [
  {
    id: '1',
    title: '百年孤独',
    author: '加西亚·马尔克斯',
    coverUrl: '/uploads/cover1.jpg',
    description: '这是拉丁美洲文学的经典之作，讲述了布恩迪亚家族七代人的传奇故事。',
    fileSize: 2500000,
    format: 'epub',
    downloadUrl: '/uploads/books/cien-anos-de-soledad.epub',
    category: '文学',
    rating: 4.8,
    price: 0,
    isFree: true,
    createdAt: new Date().toISOString()
  },
  {
    id: '2',
    title: '三体',
    author: '刘慈欣',
    coverUrl: '/uploads/cover2.jpg',
    description: '中国科幻文学的里程碑作品，探讨了人类文明与外星文明的接触。',
    fileSize: 3200000,
    format: 'epub',
    downloadUrl: '/uploads/books/santi.epub',
    category: '科幻',
    rating: 4.9,
    price: 0,
    isFree: true,
    createdAt: new Date().toISOString()
  },
  {
    id: '3',
    title: '活着',
    author: '余华',
    coverUrl: '/uploads/cover3.jpg',
    description: '讲述了一个普通人在动荡年代中的生存故事，展现了生命的坚韧。',
    fileSize: 1800000,
    format: 'epub',
    downloadUrl: '/uploads/books/alive.epub',
    category: '文学',
    rating: 4.7,
    price: 0,
    isFree: true,
    createdAt: new Date().toISOString()
  }
];

// Routes
app.get('/api/books', (req, res) => {
  const { category, search, page = 1, limit = 10 } = req.query;
  let filteredBooks = [...books];
  
  // Filter by category
  if (category) {
    filteredBooks = filteredBooks.filter(book => 
      book.category.toLowerCase().includes(category.toLowerCase())
    );
  }
  
  // Search by title or author
  if (search) {
    filteredBooks = filteredBooks.filter(book => 
      book.title.toLowerCase().includes(search.toLowerCase()) ||
      book.author.toLowerCase().includes(search.toLowerCase())
    );
  }
  
  // Pagination
  const startIndex = (page - 1) * limit;
  const paginatedBooks = filteredBooks.slice(startIndex, startIndex + parseInt(limit));
  
  res.json({
    success: true,
    data: paginatedBooks,
    total: filteredBooks.length,
    page: parseInt(page),
    limit: parseInt(limit)
  });
});

app.get('/api/books/:id', (req, res) => {
  const { id } = req.params;
  const book = books.find(b => b.id === id);
  
  if (!book) {
    return res.status(404).json({ success: false, message: 'Book not found' });
  }
  
  res.json({ success: true, data: book });
});

app.post('/api/books/:id/download', (req, res) => {
  const { id } = req.params;
  const book = books.find(b => b.id === id);
  
  if (!book) {
    return res.status(404).json({ success: false, message: 'Book not found' });
  }
  
  // In a real app, you would verify user permissions here
  res.json({ 
    success: true, 
    data: { downloadUrl: book.downloadUrl } 
  });
});

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'OK', timestamp: new Date().toISOString() });
});

app.listen(PORT, () => {
  console.log(`📚 IReader Book Store Server running on port ${PORT}`);
  console.log(`📖 API endpoints:`);
  console.log(`   GET  /api/books          - Get book list`);
  console.log(`   GET  /api/books/:id     - Get book details`);
  console.log(`   POST /api/books/:id/download - Get download URL`);
  console.log(`   GET  /health            - Health check`);
});