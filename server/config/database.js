// 数据库配置
const config = {
  development: {
    username: 'root',
    password: 'password',
    database: 'ireader_dev',
    host: '127.0.0.1',
    dialect: 'mysql'
  },
  production: {
    username: process.env.DB_USERNAME || 'root',
    password: process.env.DB_PASSWORD || 'password',
    database: process.env.DB_NAME || 'ireader_prod',
    host: process.env.DB_HOST || '127.0.0.1',
    dialect: 'mysql'
  }
};

module.exports = config;