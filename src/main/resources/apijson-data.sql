-- APIJSON 演示数据

-- 插入演示用户数据
INSERT INTO `apijson_user` (`id`, `name`, `phone`) VALUES
(82001, '张三', '13800138001'),
(82002, '李四', '13800138002'),
(82003, '王五', '13800138003'),
(82004, '赵六', '13800138004'),
(82005, '钱七', '13800138005');

-- 插入演示动态数据
INSERT INTO `Moment` (`id`, `userId`, `content`, `pictureList`) VALUES
(15, 82001, 'APIJSON，让接口和文档见鬼去吧！', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]'),
(235, 82002, '今天天气真不错~', '["https://apijson.cn/img/logo.png"]'),
(301, 82001, 'APIJSON零代码开发，太爽了！', '[]'),
(470, 82003, '测试一下APIJSON的功能', '["https://apijson.cn/img/logo.png"]'),
(551, 82004, '学习APIJSON中...', '[]');

-- 插入演示评论数据
INSERT INTO `Comment` (`id`, `toId`, `userId`, `momentId`, `content`) VALUES
(4, 0, 82002, 15, '哈哈，这个API框架确实很赞！'),
(22, 4, 82001, 15, '是的，APIJSON让开发变得简单多了'),
(45, 0, 82003, 235, '确实是好天气'),
(54, 0, 82004, 301, '零代码开发真的很有意思'),
(176, 22, 82003, 15, '我也要学习一下APIJSON');

-- 插入隐私配置数据
INSERT INTO `apijson_privacy` (`id`, `certified`, `role`, `_get`, `_head`, `_gets`, `_heads`, `_post`, `_put`, `_delete`) VALUES
(1, 0, NULL, 'id,name,phone', 'id,name,phone', 'id,name,phone', 'id,name,phone', 'name,phone', 'name,phone', 'id'),
(2, 1, 'USER', 'id,name,phone', 'id,name,phone', 'id,name,phone', 'id,name,phone', 'name,phone', 'name,phone', 'id'),
(3, 2, 'ADMIN', '*', '*', '*', '*', '*', '*', '*');

-- APIJSON 权限配置数据

-- 插入Access权限配置
INSERT INTO `Access` (`id`, `name`, `alias`, `get`, `head`, `gets`, `heads`, `post`, `put`, `delete`) VALUES
(1, 'apijson_user', 'User', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["CONTACT", "CIRCLE", "OWNER", "ADMIN"]'),
(2, 'Moment', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["CONTACT", "CIRCLE", "OWNER", "ADMIN"]'),
(3, 'Comment', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["CONTACT", "CIRCLE", "OWNER", "ADMIN"]'),
(4, 'Function', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', null, null, null),
(5, 'Request', null, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', null, null, null);

-- 插入Request请求结构配置
INSERT INTO `Request` (`id`, `version`, `method`, `tag`, `structure`, `detail`) VALUES
(1, 1, 'GET', 'User', '{"User": {"@column": "id,name,phone", "@order": "id+"}}', '获取用户信息'),
(2, 1, 'GET', 'Moment', '{"Moment": {"@column": "id,userId,content,pictureList,date", "@order": "date-"}}', '获取动态信息'),
(3, 1, 'GET', 'Comment', '{"Comment": {"@column": "id,toId,userId,momentId,content,date", "@order": "date-"}}', '获取评论信息'),
(4, 1, 'HEAD', 'User', '{"User": {"@column": "id,name,phone", "@order": "id+"}}', '获取用户信息数量'),
(5, 1, 'HEAD', 'Moment', '{"Moment": {"@column": "id,userId,content,pictureList,date", "@order": "date-"}}', '获取动态信息数量'),
(6, 1, 'HEAD', 'Comment', '{"Comment": {"@column": "id,toId,userId,momentId,content,date", "@order": "date-"}}', '获取评论信息数量');

-- 插入Function远程函数配置
INSERT INTO `Function` (`id`, `name`, `arguments`, `demo`, `detail`, `type`, `version`, `tag`, `methods`) VALUES
(1, 'count', 'table_name', '{"count": "User"}', '统计表记录数', 'Integer', 0, 'API', 'GET'),
(2, 'sum', 'table_name,column_name', '{"sum": "Moment.userId"}', '求和', 'Decimal', 0, 'API', 'GET'),
(3, 'avg', 'table_name,column_name', '{"avg": "User.id"}', '求平均值', 'Decimal', 0, 'API', 'GET'),
(4, 'max', 'table_name,column_name', '{"max": "User.id"}', '求最大值', 'Object', 0, 'API', 'GET'),
(5, 'min', 'table_name,column_name', '{"min": "User.id"}', '求最小值', 'Object', 0, 'API', 'GET');