const express = require('express');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const User = require('../models/User');

const router = express.Router();

// 用户注册
router.post('/register', async (req, res) => {
  try {
    const { email, password, name } = req.body;
    
    // 检查用户是否已存在
    const existingUser = await User.findOne({ email });
    if (existingUser) {
      return res.status(400).json({ message: '用户已存在' });
    }
    
    // 哈希密码
    const saltRounds = 10;
    const hashedPassword = await bcrypt.hash(password, saltRounds);
    
    // 创建新用户
    const newUser = new User({
      email,
      password: hashedPassword,
      name
    });
    
    await newUser.save();
    
    // 生成 JWT token
    const token = jwt.sign(
      { userId: newUser._id, email: newUser.email },
      process.env.JWT_SECRET || 'ireader-secret-key',
      { expiresIn: '24h' }
    );
    
    res.status(201).json({
      message: '注册成功',
      token,
      user: {
        id: newUser._id,
        email: newUser.email,
        name: newUser.name
      }
    });
    
  } catch (error) {
    console.error('注册错误:', error);
    res.status(500).json({ message: '服务器错误' });
  }
});

// 用户登录
router.post('/login', async (req, res) => {
  try {
    const { email, password } = req.body;
    
    // 查找用户
    const user = await User.findOne({ email });
    if (!user) {
      return res.status(401).json({ message: '邮箱或密码错误' });
    }
    
    // 验证密码
    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
      return res.status(401).json({ message: '邮箱或密码错误' });
    }
    
    // 生成 JWT token
    const token = jwt.sign(
      { userId: user._id, email: user.email },
      process.env.JWT_SECRET || 'ireader-secret-key',
      { expiresIn: '24h' }
    );
    
    res.json({
      message: '登录成功',
      token,
      user: {
        id: user._id,
        email: user.email,
        name: user.name
      }
    });
    
  } catch (error) {
    console.error('登录错误:', error);
    res.status(500).json({ message: '服务器错误' });
  }
});

// 获取当前用户信息（需要认证）
router.get('/me', async (req, res) => {
  try {
    const token = req.headers.authorization?.split(' ')[1];
    if (!token) {
      return res.status(401).json({ message: '未提供认证令牌' });
    }
    
    const decoded = jwt.verify(token, process.env.JWT_SECRET || 'ireader-secret-key');
    const user = await User.findById(decoded.userId).select('-password');
    
    if (!user) {
      return res.status(404).json({ message: '用户不存在' });
    }
    
    res.json({
      user: {
        id: user._id,
        email: user.email,
        name: user.name
      }
    });
    
  } catch (error) {
    console.error('获取用户信息错误:', error);
    res.status(401).json({ message: '无效的认证令牌' });
  }
});

module.exports = router;