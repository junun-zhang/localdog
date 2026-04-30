const mongoose = require('mongoose');
const Book = require('./models/Book');
require('dotenv').config();

const books = [
  { title:'三体', author:'刘慈欣', category:'科幻', format:'txt', file:'/books/santi.txt', coverUrl:'/covers/santi.jpg', description:'科幻巨著，讲述了地球人类文明和三体文明的信息交流、生死搏杀及两个文明在宇宙中的兴衰历程。', rating:4.9, downloadCount:12580 },
  { title:'活着', author:'余华', category:'文学', format:'epub', file:'/books/huozhe.epub', coverUrl:'/covers/huozhe.jpg', description:'讲述了农村人福贵悲惨的人生遭遇。', rating:4.8, downloadCount:9832 },
  { title:'百年孤独', author:'加西亚·马尔克斯', category:'文学', format:'epub', file:'/books/bainianguidu.epub', coverUrl:'/covers/bainian.jpg', description:'魔幻现实主义文学的代表作，描述了布恩迪亚家族七代人的传奇故事。', rating:4.7, downloadCount:7654 },
  { title:'深入理解Java虚拟机', author:'周志明', category:'科技', format:'pdf', file:'/books/jvm.pdf', coverUrl:'/covers/jvm.jpg', description:'Java开发者必读的JVM原理书籍。', rating:4.8, downloadCount:5432 },
  { title:'算法导论', author:'Thomas H.Cormen', category:'科技', format:'pdf', file:'/books/algorithms.pdf', coverUrl:'/covers/algorithms.jpg', description:'算法领域的经典教材，全面介绍了计算机算法。', rating:4.6, downloadCount:4321 },
  { title:'人类简史', author:'尤瓦尔·赫拉利', category:'历史', format:'epub', file:'/books/sapiens.epub', coverUrl:'/covers/sapiens.jpg', description:'从石器时代到21世纪，讲述人类如何从动物界脱颖而出。', rating:4.7, downloadCount:8765 },
  { title:'经济学原理', author:'曼昆', category:'经济', format:'pdf', file:'/books/economics.pdf', coverUrl:'/covers/economics.jpg', description:'最受欢迎的经济学入门教材。', rating:4.5, downloadCount:3210 },
  { title:'苏菲的世界', author:'乔斯坦·贾德', category:'哲学', format:'txt', file:'/books/sophie.txt', coverUrl:'/covers/sophie.jpg', description:'以小说的形式，通过一名哲学导师向一个叫苏菲的女孩传授哲学知识。', rating:4.6, downloadCount:6543 },
  { title:'红楼梦', author:'曹雪芹', category:'文学', format:'epub', file:'/books/hongloumeng.epub', coverUrl:'/covers/honglou.jpg', description:'中国古典四大名著之一，以贾宝玉与林黛玉的爱情悲剧为主线。', rating:4.9, downloadCount:15432 },
  { title:'数理化通俗演义', author:'梁衡', category:'教育', format:'txt', file:'/books/maths.txt', coverUrl:'/covers/maths.jpg', description:'用章回小说形式生动演绎数理化发展史。', rating:4.4, downloadCount:2109 },
  { title:'设计模式', author:'GoF', category:'科技', format:'pdf', file:'/books/design-patterns.pdf', coverUrl:'/covers/design.jpg', description:'面向对象软件设计模式的经典之作。', rating:4.5, downloadCount:3876 },
  { title:'国富论', author:'亚当·斯密', category:'经济', format:'epub', file:'/books/wealth.epub', coverUrl:'/covers/wealth.jpg', description:'现代经济学之父的奠基之作。', rating:4.4, downloadCount:2987 },
  { title:'时间简史', author:'史蒂芬·霍金', category:'科技', format:'pdf', file:'/books/hawking.pdf', coverUrl:'/covers/hawking.jpg', description:'探索时间和空间核心秘密的引人入胜的故事。', rating:4.7, downloadCount:7654 },
  { title:'平凡的世界', author:'路遥', category:'文学', format:'txt', file:'/books/pingfan.txt', coverUrl:'/covers/pingfan.jpg', description:'以中国70-80年代为背景，刻画了社会各阶层众多普通人的形象。', rating:4.8, downloadCount:10987 },
  { title:'逻辑哲学论', author:'维特根斯坦', category:'哲学', format:'pdf', file:'/books/logic.pdf', coverUrl:'/covers/logic.jpg', description:'分析哲学的代表作之一。', rating:4.2, downloadCount:1876 },
  { title:'艺术的故事', author:'贡布里希', category:'艺术', format:'epub', file:'/books/art.epub', coverUrl:'/covers/art.jpg', description:'最著名的艺术入门书籍。', rating:4.6, downloadCount:4321 },
  { title:'明朝那些事儿', author:'当年明月', category:'历史', format:'txt', file:'/books/ming.txt', coverUrl:'/covers/ming.jpg', description:'以史料为基础，以年代和具体人物为主线，对明朝历史进行全景展示。', rating:4.7, downloadCount:13210 },
  { title:'养生论', author:'王琦', category:'生活', format:'epub', file:'/books/yangsheng.epub', coverUrl:'/covers/yangsheng.jpg', description:'传统中医养生保健知识。', rating:4.3, downloadCount:2345 },
  { title:'人性的弱点', author:'戴尔·卡耐基', category:'教育', format:'txt', file:'/books/ruoxing.txt', coverUrl:'/covers/ruoxing.jpg', description:'人际关系的经典指南。', rating:4.5, downloadCount:6543 },
  { title:'墨菲定律', author:'张文华', category:'哲学', format:'epub', file:'/books/murphy.epub', coverUrl:'/covers/murphy.jpg', description:'揭示生活中的各种心理学效应。', rating:4.3, downloadCount:3456 },
];

async function seed() {
  try {
    await mongoose.connect(process.env.MONGODB_URI || 'mongodb://localhost:27017/ireader');
    await Book.deleteMany({});
    await Book.insertMany(books);
    console.log('Seeded:', books.length, 'books');
    process.exit(0);
  } catch (err) {
    console.error('Seed error:', err);
    process.exit(1);
  }
}
seed();
