-- APIJSON H2数据库初始化数据

-- 插入用户基本信息
INSERT INTO User (id, name, tag, head, contactIdList, pictureList) VALUES 
(82001, 'TommyLemon', 'Android&Java', 'http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000', '[82003,82005,90814]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000"]'),
(82002, 'Steve', 'iOS', 'http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000', '[82001]', '[]'),
(82003, 'Angela', 'Web', 'http://static.oschina.net/uploads/user/629/1258397_50.jpg?t=1407063324000', '[82001,82002]', '[]'),
(82004, 'admin', 'Admin', NULL, '[]', '[]'),
(82005, 'test', 'Test', NULL, '[82001]', '[]');

-- 插入用户隐私信息
INSERT INTO Privacy (id, certified, phone, balance, _password, _payPassword) VALUES 
(82001, 1, '13000082001', 100.32, 'apijson', '123456'),
(82002, 1, '13000082002', 200.85, 'apijson', '123456'), 
(82003, 0, '13000082003', 50.00, 'apijson', '123456'),
(82004, 1, '13000082004', 999.99, 'apijson', '123456'),
(82005, 0, '13000082005', 0.00, 'apijson', '123456');

-- 插入访问权限配置
INSERT INTO Access (debug, name, alias, get, head, gets, heads, post, put, delete) VALUES 
(1, 'User', NULL, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER"]', '["UNKNOWN"]', '["OWNER"]', '["OWNER"]'),
(1, 'Privacy', NULL, '["OWNER"]', '["OWNER"]', '["OWNER"]', '["OWNER"]', '["UNKNOWN"]', '["OWNER"]', '["OWNER"]'),
(1, 'Verify', NULL, '[]', '[]', '[]', '[]', '["UNKNOWN"]', '[]', '[]'),
(1, 'Access', NULL, '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]'),
(1, 'Request', NULL, '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]'),
(1, 'Function', NULL, '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]');

-- 插入请求校验配置
INSERT INTO Request (debug, version, method, tag, structure, detail) VALUES 
(1, 1, 'GET', 'User', '{"User":{"@column":"id,name,tag,head,contactIdList,pictureList,date"}}', '获取用户信息'),
(1, 1, 'HEAD', 'User', '{"User":{"@column":"id,name"}}', '获取用户数量'),
(1, 1, 'GETS', 'User', '{"User":{"@column":"id,name,tag,head,contactIdList,pictureList,date"}}', '安全获取用户信息'),  
(1, 1, 'HEADS', 'User', '{"User":{"@column":"id,name"}}', '安全获取用户数量'),
(1, 1, 'POST', 'User', '{"User":{"name!":"","tag":"","head":"","contactIdList":"","pictureList":""}}', '新增用户'),
(1, 1, 'PUT', 'User', '{"User":{"id!":0,"name":"","tag":"","head":"","contactIdList":"","pictureList":""}}', '修改用户'),
(1, 1, 'DELETE', 'User', '{"User":{"id!":0}}', '删除用户');

-- 兼容数据 - 旧的apijson_user表
INSERT INTO apijson_user (id, name, phone) VALUES 
(1, '张三', '13800138001'),
(2, '李四', '13800138002'),
(3, '王五', '13800138003'),
(4, '赵六', '13800138004'),
(5, '钱七', '13800138005');