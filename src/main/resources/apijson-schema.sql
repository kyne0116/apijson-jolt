-- APIJSON 演示数据库结构
-- 创建演示用的表

-- 用户表
CREATE TABLE IF NOT EXISTS `apijson_user` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

-- 动态表
CREATE TABLE IF NOT EXISTS `Moment` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `userId` bigint(15) NOT NULL COMMENT '用户id',
  `content` text NOT NULL COMMENT '内容',
  `pictureList` text COMMENT '图片列表',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='动态';

-- 评论表
CREATE TABLE IF NOT EXISTS `Comment` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `toId` bigint(15) NOT NULL DEFAULT '0' COMMENT '被回复的id',
  `userId` bigint(15) NOT NULL COMMENT '用户id',
  `momentId` bigint(15) NOT NULL COMMENT '动态id',
  `content` text NOT NULL COMMENT '内容',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论';

-- 隐私配置表 (APIJSON必需)
CREATE TABLE IF NOT EXISTS `apijson_privacy` (
  `id` bigint(15) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `certified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户类型：0-未认证，1-已认证',
  `role` varchar(20) DEFAULT NULL COMMENT '角色',
  `_get` text COMMENT 'GET可访问的字段',
  `_head` text COMMENT 'HEAD可访问的字段',
  `_gets` text COMMENT 'GETS可访问的字段',
  `_heads` text COMMENT 'HEADS可访问的字段',
  `_post` text COMMENT 'POST可访问的字段',
  `_put` text COMMENT 'PUT可访问的字段',
  `_delete` text COMMENT 'DELETE可访问的字段',
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='隐私配置';

-- APIJSON 必需的权限管理表结构

-- 访问权限表
CREATE TABLE IF NOT EXISTS `Access` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
    `name` VARCHAR(50) NULL DEFAULT NULL COMMENT '实际名称，例如：User',
    `alias` VARCHAR(20) NULL DEFAULT NULL COMMENT '外部调用的别名，例如：Member',
    `get` TEXT NULL COMMENT 'GET请求权限',
    `head` TEXT NULL COMMENT 'HEAD请求权限', 
    `gets` TEXT NULL COMMENT 'GETS请求权限',
    `heads` TEXT NULL COMMENT 'HEADS请求权限',
    `post` TEXT NULL COMMENT 'POST请求权限',
    `put` TEXT NULL COMMENT 'PUT请求权限',
    `delete` TEXT NULL COMMENT 'DELETE请求权限',
    `date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限配置';

-- 请求权限表
CREATE TABLE IF NOT EXISTS `Request` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
    `version` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '接口版本号：\n1-GET,HEAD可用任意结构访问任意开放内容，也可访问视图tables，functions等\n2-API,如果被请求的表config中没有结果符合条件的，那不能访问',
    `method` VARCHAR(10) NULL DEFAULT NULL COMMENT '只限于GET,HEAD外的操作方法。\n用','分隔：PUT,POST,DELETE',
    `tag` VARCHAR(20) NOT NULL COMMENT '标签',
    `structure` JSON NOT NULL COMMENT '结构',
    `detail` VARCHAR(10000) NULL DEFAULT NULL COMMENT '详细说明',
    `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请求结构规则';

-- 函数权限表
CREATE TABLE IF NOT EXISTS `Function` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
    `name` VARCHAR(30) NOT NULL COMMENT '方法名',
    `arguments` VARCHAR(100) NULL DEFAULT NULL COMMENT '方法参数',
    `demo` JSON NOT NULL COMMENT '可用的示例',
    `detail` VARCHAR(1000) NULL DEFAULT NULL COMMENT '详细描述',
    `type` VARCHAR(50) NOT NULL DEFAULT 'Object' COMMENT '返回值类型',
    `version` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '允许的最低版本号',
    `tag` VARCHAR(20) NULL DEFAULT 'API' COMMENT '标签',
    `methods` VARCHAR(50) NULL DEFAULT NULL COMMENT '允许的HTTP请求方法',
    `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='远程函数';

-- 测试用例表
CREATE TABLE IF NOT EXISTS `TestRecord` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
    `userId` BIGINT(20) NOT NULL COMMENT '用户id',
    `documentId` BIGINT(20) NOT NULL COMMENT '接口文档id',
    `response` JSON NOT NULL COMMENT '接口返回结果JSON',
    `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `compare` JSON NULL COMMENT '对比结果',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试记录';

-- 文档表
CREATE TABLE IF NOT EXISTS `Document` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
    `userId` BIGINT(20) NOT NULL COMMENT '用户id',
    `version` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '接口版本号',
    `name` VARCHAR(50) NOT NULL COMMENT '接口名称',
    `url` VARCHAR(250) NOT NULL COMMENT '请求地址',
    `request` JSON NOT NULL COMMENT '请求JSON',
    `header` VARCHAR(2000) NULL DEFAULT NULL COMMENT '请求头Request Header',
    `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口文档';