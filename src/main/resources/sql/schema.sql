-- ============================================================
-- 数据库初始化脚本
-- 学生宿舍卫生管理系统
-- ============================================================

DROP DATABASE IF EXISTS dormitory_db;
CREATE DATABASE dormitory_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE dormitory_db;

-- ============================================================
-- 系统域 - 基础配置表
-- ============================================================

-- 班级表
CREATE TABLE `sys_class` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `class_name` VARCHAR(64) NOT NULL COMMENT '班级名称',
  `counselor_name` VARCHAR(32) DEFAULT NULL COMMENT '辅导员姓名',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_class_name` (`class_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班级信息表';

-- 宿舍楼表
CREATE TABLE `sys_building` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_name` VARCHAR(32) NOT NULL COMMENT '楼宇名称',
  `manager_name` VARCHAR(32) DEFAULT NULL COMMENT '宿管姓名',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间(软删除)',
  PRIMARY KEY (`id`),
  KEY `idx_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍楼信息表';

-- 宿舍房间表
CREATE TABLE `sys_room` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_id` BIGINT NOT NULL COMMENT '所属楼宇ID',
  `room_number` VARCHAR(16) NOT NULL COMMENT '房间号',
  `capacity` INT NOT NULL DEFAULT 4 COMMENT '床位容量',
  `gender` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '性别限制:1-男寝,2-女寝',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_room` (`building_id`, `room_number`),
  KEY `idx_building_id` (`building_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宿舍房间表';

-- 用户表
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(32) NOT NULL COMMENT '账号(学号/工号)',
  `password` VARCHAR(128) NOT NULL COMMENT '加密密码',
  `real_name` VARCHAR(32) NOT NULL COMMENT '真实姓名',
  `phone` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(64) DEFAULT NULL COMMENT '邮箱',
  `role` VARCHAR(128) NOT NULL DEFAULT 'STUDENT' COMMENT '角色(多角色逗号分隔)',
  `class_id` BIGINT DEFAULT NULL COMMENT '所属班级ID',
  `room_id` BIGINT DEFAULT NULL COMMENT '所属宿舍ID',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态:1-正常,0-禁用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` DATETIME DEFAULT NULL COMMENT '删除时间(软删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_class_id` (`class_id`),
  KEY `idx_deleted_at` (`deleted_at`),
  KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- ============================================================
-- 业务域 - 核心业务表
-- ============================================================

-- 卫生检查记录表
CREATE TABLE `biz_inspection` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` BIGINT NOT NULL COMMENT '被检查宿舍ID',
  `inspector_id` BIGINT NOT NULL COMMENT '检查员ID',
  `modifier_id` BIGINT DEFAULT NULL COMMENT '最后修改人ID',
  `total_score` INT NOT NULL COMMENT '总分(0-100)',
  `remarks` TEXT DEFAULT NULL COMMENT '备注说明',
  `evidence_imgs` TEXT DEFAULT NULL COMMENT '图片路径(逗号分隔)',
  `check_date` DATE NOT NULL COMMENT '检查日期',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_room_date` (`room_id`, `check_date`),
  KEY `idx_inspector_id` (`inspector_id`),
  KEY `idx_check_date` (`check_date`),
  CONSTRAINT `chk_total_score` CHECK (`total_score` BETWEEN 0 AND 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='卫生检查记录表';

-- 检查员权限申请表
CREATE TABLE `biz_application` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `applicant_id` BIGINT NOT NULL COMMENT '申请人ID',
  `application_reason` VARCHAR(500) NOT NULL COMMENT '申请理由',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态:0-待审核,1-已通过,2-已驳回',
  `reviewer_id` BIGINT DEFAULT NULL COMMENT '审核人ID',
  `review_comment` TEXT DEFAULT NULL COMMENT '审核意见',
  `review_time` DATETIME DEFAULT NULL COMMENT '审核时间',
  `apply_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请提交时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_applicant_status` (`applicant_id`, `status`),
  KEY `idx_status` (`status`),
  KEY `idx_apply_time` (`apply_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='检查员权限申请表';
