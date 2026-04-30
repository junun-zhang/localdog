const mongoose = require('mongoose');

const bookSchema = new mongoose.Schema({
  title:       { type: String, required: true },
  author:      { type: String, required: true },
  description: { type: String, default: '' },
  coverUrl:    { type: String, default: '' },
  file:        { type: String, required: true },
  format:      { type: String, enum: ['epub','pdf','txt'], required: true },
  category:    { type: String, required: true },
  fileSize:    { type: Number, default: 0 },
  downloadCount: { type: Number, default: 0 },
  rating:      { type: Number, default: 0, min: 0, max: 5 },
  isFree:      { type: Boolean, default: true },
}, { timestamps: true });

module.exports = mongoose.model('Book', bookSchema);
