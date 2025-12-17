-- ============================================================
-- 初始化测试数据
-- ============================================================
USE dormitory_db;

-- 插入班级数据
INSERT INTO sys_class (class_name, counselor_name) VALUES
('软件232', '张老师'),
('软件231', '李老师'),
('计算机232', '王老师');

-- 插入宿舍楼数据
INSERT INTO sys_building (building_name, manager_name) VALUES
('东区4号楼', '刘阿姨'),
('西区1号楼', '陈阿姨');

-- 插入宿舍房间数据
INSERT INTO sys_room (building_id, room_number, capacity, gender) VALUES
(1, '401', 4, 1),
(1, '402', 4, 1),
(1, '501', 6, 1),
(2, '301', 4, 2),
(2, '302', 4, 2);

-- 插入用户数据 (密码统一为 123456 的BCrypt加密结果)
INSERT INTO sys_user (username, password, real_name, phone, email, role, class_id, room_id) VALUES
-- 管理员
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', '13800000000', 'admin@example.com', 'ADMIN', NULL, NULL),
-- 教师
('T001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张老师', '13800000001', 'zhang@example.com', 'TEACHER', NULL, NULL),
-- 学生兼检查员
('20230101', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张三', '13800000101', 'zhangsan@example.com', 'STUDENT,INSPECTOR', 1, 1),
-- 普通学生
('20230102', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李四', '13800000102', 'lisi@example.com', 'STUDENT', 1, 1),
('20230103', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王五', '13800000103', 'wangwu@example.com', 'STUDENT', 1, 2),
('20230201', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵六', '13800000201', 'zhaoliu@example.com', 'STUDENT', 2, 4);

-- 插入卫生检查记录
INSERT INTO biz_inspection (room_id, inspector_id, total_score, remarks, check_date) VALUES
(1, 3, 95, '卫生良好,继续保持', '2025-12-10'),
(1, 3, 88, '地面有少许垃圾', '2025-12-11'),
(2, 3, 75, '桌面杂乱,需要整改', '2025-12-10'),
(4, 3, 92, '整体干净整洁', '2025-12-12');

-- 插入检查员申请记录
INSERT INTO biz_application (applicant_id, application_reason, status, reviewer_id, review_time) VALUES
(4, '我想为宿舍卫生管理贡献力量', 0, NULL, NULL),
(5, '有责任心,愿意服务同学', 1, 2, '2025-12-10 10:30:00');
