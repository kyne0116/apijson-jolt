-- 学生家长管理系统数据库表结构
-- 基于APIJSON框架的Student-Parent表设计

-- 学生表
CREATE TABLE IF NOT EXISTS `Student` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '学生ID，主键',
    `student_no` VARCHAR(20) NOT NULL COMMENT '学号，唯一标识',
    `name` VARCHAR(50) NOT NULL COMMENT '学生姓名',
    `gender` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '性别：0-男，1-女',
    `age` INT(3) NOT NULL COMMENT '年龄',
    `grade` VARCHAR(20) NOT NULL COMMENT '年级',
    `class_name` VARCHAR(50) COMMENT '班级名称',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `email` VARCHAR(100) COMMENT '邮箱地址',
    `address` TEXT COMMENT '家庭住址',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-正常',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_no` (`student_no`),
    KEY `idx_grade` (`grade`),
    KEY `idx_class` (`class_name`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

-- 家长表  
CREATE TABLE IF NOT EXISTS `Parent` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '家长ID，主键',
    `student_id` BIGINT(20) NOT NULL COMMENT '学生ID，外键关联Student表',
    `name` VARCHAR(50) NOT NULL COMMENT '家长姓名',
    `relationship` VARCHAR(20) NOT NULL COMMENT '与学生关系：父亲、母亲、爷爷、奶奶、外公、外婆、监护人等',
    `gender` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '性别：0-男，1-女',
    `age` INT(3) COMMENT '年龄',
    `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `email` VARCHAR(100) COMMENT '邮箱地址',
    `occupation` VARCHAR(100) COMMENT '职业',
    `work_address` TEXT COMMENT '工作地址',
    `is_emergency_contact` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否紧急联系人：0-否，1-是',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-停用，1-正常',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_relationship` (`relationship`),
    KEY `idx_emergency` (`is_emergency_contact`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_parent_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长信息表';

-- 为APIJSON权限控制添加Student和Parent表的Access配置
INSERT INTO `Access` (`name`, `alias`, `get`, `head`, `gets`, `heads`, `post`, `put`, `delete`) VALUES
('Student', NULL, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "ADMIN"]', '["LOGIN", "ADMIN"]', '["ADMIN"]'),
('Parent', NULL, '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["UNKNOWN", "LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "CONTACT", "CIRCLE", "OWNER", "ADMIN"]', '["LOGIN", "ADMIN"]', '["LOGIN", "ADMIN"]', '["ADMIN"]');

-- 为APIJSON请求结构添加配置
INSERT INTO `Request` (`version`, `method`, `tag`, `structure`, `detail`) VALUES
(1, 'GET', 'Student', '{"Student":{"@column":"id,student_no,name,gender,age,grade,class_name,phone,email,address,status,create_time,update_time"}}', '获取学生信息'),
(1, 'HEAD', 'Student', '{"Student":{"@column":"id,name"}}', '获取学生数量'),
(1, 'GETS', 'Student', '{"Student":{"@column":"id,student_no,name,gender,age,grade,class_name,phone,email,address,status,create_time,update_time"}}', '安全获取学生信息'),
(1, 'HEADS', 'Student', '{"Student":{"@column":"id,name"}}', '安全获取学生数量'),
(1, 'POST', 'Student', '{"Student":{"student_no!":"","name!":"","gender!":0,"age!":0,"grade!":"","class_name":"","phone":"","email":"","address":"","status":1}}', '新增学生'),
(1, 'PUT', 'Student', '{"Student":{"id!":0,"student_no":"","name":"","gender":0,"age":0,"grade":"","class_name":"","phone":"","email":"","address":"","status":1}}', '修改学生'),
(1, 'DELETE', 'Student', '{"Student":{"id!":0}}', '删除学生'),

(1, 'GET', 'Parent', '{"Parent":{"@column":"id,student_id,name,relationship,gender,age,phone,email,occupation,work_address,is_emergency_contact,status,create_time,update_time"}}', '获取家长信息'),
(1, 'HEAD', 'Parent', '{"Parent":{"@column":"id,name"}}', '获取家长数量'),
(1, 'GETS', 'Parent', '{"Parent":{"@column":"id,student_id,name,relationship,gender,age,phone,email,occupation,work_address,is_emergency_contact,status,create_time,update_time"}}', '安全获取家长信息'),
(1, 'HEADS', 'Parent', '{"Parent":{"@column":"id,name"}}', '安全获取家长数量'),
(1, 'POST', 'Parent', '{"Parent":{"student_id!":0,"name!":"","relationship!":"","gender!":0,"phone!":"","email":"","occupation":"","work_address":"","is_emergency_contact":0,"status":1}}', '新增家长'),
(1, 'PUT', 'Parent', '{"Parent":{"id!":0,"student_id":0,"name":"","relationship":"","gender":0,"age":0,"phone":"","email":"","occupation":"","work_address":"","is_emergency_contact":0,"status":1}}', '修改家长'),
(1, 'DELETE', 'Parent', '{"Parent":{"id!":0}}', '删除家长');