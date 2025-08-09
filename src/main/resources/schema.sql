-- APIJSON学生家长管理系统数据库结构

-- 创建学生表
CREATE TABLE Student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_no VARCHAR(20) UNIQUE COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-男，1-女',
    age INT DEFAULT 0 COMMENT '年龄',
    grade VARCHAR(20) COMMENT '年级',
    class_name VARCHAR(50) COMMENT '班级名称',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱地址',
    address TEXT COMMENT '家庭地址',
    status TINYINT DEFAULT 1 COMMENT '状态：0-停用，1-启用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '学生信息表';

-- 创建家长表
CREATE TABLE Parent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL COMMENT '学生ID（外键）',
    name VARCHAR(50) NOT NULL COMMENT '家长姓名',
    relationship VARCHAR(10) NOT NULL COMMENT '关系：父亲、母亲、爷爷、奶奶、外公、外婆、监护人等',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-男，1-女',
    age INT COMMENT '年龄',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱地址',
    occupation VARCHAR(100) COMMENT '职业',
    work_address TEXT COMMENT '工作地址',
    is_emergency_contact TINYINT DEFAULT 0 COMMENT '是否紧急联系人：0-否，1-是',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (student_id) REFERENCES Student(id) ON DELETE CASCADE
) COMMENT '家长信息表';

-- APIJSON 权限配置表
CREATE TABLE Access (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    debug TINYINT DEFAULT 0 NOT NULL COMMENT '是否为调试表：0-否，1-是',
    name VARCHAR(50) NOT NULL COMMENT '实际表名',
    alias VARCHAR(20) COMMENT '外部调用的表别名',
    `get` TEXT COMMENT 'GET请求，角色列表',
    head TEXT COMMENT 'HEAD请求，角色列表',
    gets TEXT COMMENT 'GETS请求，角色列表',
    heads TEXT COMMENT 'HEADS请求，角色列表',
    post TEXT COMMENT 'POST请求，角色列表',
    put TEXT COMMENT 'PUT请求，角色列表',
    `delete` TEXT COMMENT 'DELETE请求，角色列表',
    `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    detail TEXT COMMENT '详细说明'
) COMMENT '权限配置表';

-- APIJSON 请求参数校验表
CREATE TABLE Request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    debug TINYINT DEFAULT 0 NOT NULL COMMENT '是否为调试表：0-否，1-是',
    version TINYINT DEFAULT 1 NOT NULL COMMENT '接口版本号',
    method VARCHAR(10) DEFAULT 'GETS' NOT NULL COMMENT '请求方法',
    tag VARCHAR(20) NOT NULL COMMENT '标签',
    structure TEXT NOT NULL COMMENT '请求结构，JSONObject格式',
    detail TEXT COMMENT '详细说明',
    `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '请求参数校验表';