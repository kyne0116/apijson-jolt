-- APIJSON学生家长管理系统测试数据

-- 插入测试学生数据
INSERT INTO Student (student_no, name, gender, age, grade, class_name, phone, email, address, status) VALUES
('S2024001', '张小明', 0, 15, '九年级', '九年级(1)班', '13800001001', 'zhangxm@email.com', '北京市朝阳区建国路1号', 1),
('S2024002', '李小红', 1, 14, '八年级', '八年级(2)班', '13800001002', 'lixh@email.com', '上海市浦东新区陆家嘴路2号', 1),
('S2024003', '王小强', 0, 16, '九年级', '九年级(2)班', '13800001003', 'wangxq@email.com', '广州市天河区珠江新城3号', 1),
('S2024004', '刘小美', 1, 13, '七年级', '七年级(1)班', '13800001004', 'liuxm@email.com', '深圳市南山区科技园4号', 1),
('S2024005', '陈小华', 0, 15, '九年级', '九年级(1)班', '13800001005', 'chenxh@email.com', '杭州市西湖区文三路5号', 1);

-- 插入测试家长数据
INSERT INTO Parent (student_id, name, relationship, gender, age, phone, email, occupation, work_address, is_emergency_contact) VALUES
-- 张小明的家长
(1, '张大强', '父亲', 0, 42, '13900001001', 'zhangdq@email.com', '软件工程师', '北京市海淀区中关村大街10号', 1),
(1, '王美丽', '母亲', 1, 40, '13900001002', 'wangml@email.com', '会计师', '北京市朝阳区国贸大厦20层', 0),

-- 李小红的家长
(2, '李建国', '父亲', 0, 45, '13900001003', 'lijg@email.com', '医生', '上海市黄浦区人民医院', 1),
(2, '赵小芳', '母亲', 1, 43, '13900001004', 'zhaoxf@email.com', '教师', '上海市徐汇区第一中学', 0),

-- 王小强的家长
(3, '王大明', '父亲', 0, 48, '13900001005', 'wangdm@email.com', '企业家', '广州市越秀区珠江路100号', 1),
(3, '李小娟', '母亲', 1, 46, '13900001006', 'lixj@email.com', '护士', '广州市天河区中山医院', 0),
(3, '王老爷', '爷爷', 0, 72, '13900001007', 'wangly@email.com', '退休工人', '广州市海珠区老城区', 0),

-- 刘小美的家长
(4, '刘志华', '父亲', 0, 41, '13900001008', 'liuzh@email.com', 'IT经理', '深圳市福田区华强北科技大厦', 1),
(4, '周小玲', '母亲', 1, 39, '13900001009', 'zhouxl@email.com', '设计师', '深圳市南山区设计产业园', 0);

-- 插入权限配置数据
INSERT INTO Access (name, alias, `get`, head, gets, heads, post, put, `delete`, detail) VALUES
-- 学生表权限配置（允许所有用户查询，仅管理员可修改）
('Student', null, '["UNKNOWN", "LOGIN", "ADMIN"]', '["UNKNOWN", "LOGIN", "ADMIN"]', '["UNKNOWN", "LOGIN", "ADMIN"]', '["UNKNOWN", "LOGIN", "ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '学生表的访问权限配置'),
-- 家长表权限配置
('Parent', null, '["UNKNOWN", "LOGIN", "ADMIN"]', '["UNKNOWN", "LOGIN", "ADMIN"]', '["UNKNOWN", "LOGIN", "ADMIN"]', '["UNKNOWN", "LOGIN", "ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '家长表的访问权限配置'),
-- 权限表本身的权限配置
('Access', null, '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '权限配置表的访问权限'),
-- 请求表的权限配置  
('Request', null, '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '["ADMIN"]', '请求校验配置表的访问权限');

-- 插入请求校验配置数据
INSERT INTO Request (version, method, tag, structure, detail) VALUES
-- 学生表的请求校验配置
(1, 'GET', 'Student', '{"Student": {"@column": "id,student_no,name,gender,age,grade,class_name,phone,email"}}', '学生信息查询请求结构校验'),
(1, 'POST', 'Student', '{"Student": {"name!": "", "gender": 0, "age": 0, "grade": "", "student_no!": ""}}', '学生信息创建请求结构校验'),
(1, 'PUT', 'Student', '{"Student": {"id!": 0, "name": "", "gender": 0, "age": 0}}', '学生信息更新请求结构校验'),
(1, 'DELETE', 'Student', '{"Student": {"id!": 0}}', '学生信息删除请求结构校验'),

-- 家长表的请求校验配置
(1, 'GET', 'Parent', '{"Parent": {"@column": "id,student_id,name,relationship,phone,occupation,is_emergency_contact"}}', '家长信息查询请求结构校验'),
(1, 'POST', 'Parent', '{"Parent": {"student_id!": 0, "name!": "", "relationship!": "", "phone!": ""}}', '家长信息创建请求结构校验'),
(1, 'PUT', 'Parent', '{"Parent": {"id!": 0, "name": "", "phone": ""}}', '家长信息更新请求结构校验'),
(1, 'DELETE', 'Parent', '{"Parent": {"id!": 0}}', '家长信息删除请求结构校验');