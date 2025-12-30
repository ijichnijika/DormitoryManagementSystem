-- ============================================================
-- 学生宿舍卫生管理系统 - 扩展测试数据
-- 执行方式：在服务器上运行
-- docker exec -i dorm-mysql mysql -u root -p123456 dormitory_db < src/main/resources/sql/test-data.sql
-- ============================================================

USE dormitory_db;

-- ============================================================
-- 1. 新增班级（共8个班级）
-- ============================================================
INSERT INTO `sys_class` (`class_name`, `counselor_name`) VALUES
('软件233', '陈老师'),
('软件234', '刘老师'),
('计算机232', '赵老师'),
('计算机233', '周老师'),
('网络安全231', '吴老师');

-- ============================================================
-- 2. 新增宿舍楼（共6栋）
-- ============================================================
INSERT INTO `sys_building` (`building_name`, `manager_name`) VALUES
('D栋', '李宿管'),
('E栋', '王宿管'),
('F栋', '张宿管');

-- ============================================================
-- 3. 新增宿舍房间（共24间，每栋4间）
-- ============================================================
INSERT INTO `sys_room` (`building_id`, `room_number`, `capacity`, `gender`) VALUES
-- A栋新增房间
(1, '103', 4, 1),
(1, '104', 4, 1),
(1, '202', 6, 1),
-- B栋新增房间
(2, '303', 4, 2),
(2, '304', 4, 2),
(2, '401', 6, 2),
-- C栋新增房间
(3, '402', 4, 1),
(3, '403', 4, 1),
(3, '501', 6, 1),
-- D栋房间
(4, '101', 4, 1),
(4, '102', 4, 1),
(4, '201', 6, 1),
(4, '202', 4, 1),
-- E栋房间（女寝）
(5, '101', 4, 2),
(5, '102', 4, 2),
(5, '201', 6, 2),
(5, '202', 4, 2),
-- F栋房间
(6, '101', 4, 1),
(6, '102', 4, 1),
(6, '201', 6, 1),
(6, '202', 4, 1);

-- ============================================================
-- 4. 新增教师账号（密码均为 admin123）
-- BCrypt: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi
-- ============================================================
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `role`, `status`) VALUES
('teacher02', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '陈志明', '13800138002', 'chen@dorm.com', 'TEACHER', 1),
('teacher03', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '刘芳华', '13800138003', 'liu@dorm.com', 'TEACHER', 1),
('teacher04', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵建国', '13800138004', 'zhao@dorm.com', 'TEACHER,INSPECTOR', 1);

-- ============================================================
-- 5. 新增学生账号
-- 按班级分组，每个班级4-6名学生，部分学生是检查员
-- ============================================================

-- 软件231班学生（已有张三2023001、李四2023002，新增4人）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `role`, `class_id`, `room_id`, `status`) VALUES
('2023004', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵六', '13900000004', 'STUDENT', 1, 1, 1),
('2023005', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '钱七', '13900000005', 'STUDENT', 1, 1, 1),
('2023006', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '孙八', '13900000006', 'STUDENT,INSPECTOR', 1, 7, 1),
('2023007', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '周九', '13900000007', 'STUDENT', 1, 7, 1);

-- 软件232班学生（已有王五2023003，新增5人）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `role`, `class_id`, `room_id`, `status`) VALUES
('2023008', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '吴十', '13900000008', 'STUDENT', 2, 2, 1),
('2023009', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '郑十一', '13900000009', 'STUDENT', 2, 2, 1),
('2023010', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王丽娜', '13900000010', 'STUDENT', 2, 8, 1),
('2023011', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '冯小刚', '13900000011', 'STUDENT,INSPECTOR', 2, 8, 1),
('2023012', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '陈雨', '13900000012', 'STUDENT', 2, 3, 1);

-- 计算机231班学生（新增6人）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `role`, `class_id`, `room_id`, `status`) VALUES
('2023013', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '林浩', '13900000013', 'STUDENT', 3, 3, 1),
('2023014', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '黄磊', '13900000014', 'STUDENT', 3, 3, 1),
('2023015', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '梁静', '13900000015', 'STUDENT', 3, 9, 1),
('2023016', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '谢霆', '13900000016', 'STUDENT,INSPECTOR', 3, 9, 1),
('2023017', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '韩梅', '13900000017', 'STUDENT', 3, 10, 1),
('2023018', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '唐杰', '13900000018', 'STUDENT', 3, 10, 1);

-- 软件233班学生（女生班，新增6人）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `role`, `class_id`, `room_id`, `status`) VALUES
('2023019', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '杨柳', '13900000019', 'STUDENT', 4, 4, 1),
('2023020', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '朱红', '13900000020', 'STUDENT', 4, 4, 1),
('2023021', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '何洁', '13900000021', 'STUDENT,INSPECTOR', 4, 4, 1),
('2023022', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '邓紫', '13900000022', 'STUDENT', 4, 5, 1),
('2023023', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '许晴', '13900000023', 'STUDENT', 4, 5, 1),
('2023024', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '范冰', '13900000024', 'STUDENT', 4, 5, 1);

-- 软件234班学生（女生班，新增4人）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `role`, `class_id`, `room_id`, `status`) VALUES
('2023025', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李冰冰', '13900000025', 'STUDENT', 5, 11, 1),
('2023026', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵薇', '13900000026', 'STUDENT,INSPECTOR', 5, 11, 1),
('2023027', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '周迅', '13900000027', 'STUDENT', 5, 12, 1),
('2023028', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '章子怡', '13900000028', 'STUDENT', 5, 12, 1);

-- 计算机232班学生（新增5人）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `role`, `class_id`, `room_id`, `status`) VALUES
('2023029', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '刘德华', '13900000029', 'STUDENT', 6, 16, 1),
('2023030', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张学友', '13900000030', 'STUDENT', 6, 16, 1),
('2023031', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '郭富城', '13900000031', 'STUDENT,INSPECTOR', 6, 16, 1),
('2023032', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '黎明', '13900000032', 'STUDENT', 6, 17, 1),
('2023033', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '周杰伦', '13900000033', 'STUDENT', 6, 17, 1);

-- 计算机233班学生（新增4人）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `role`, `class_id`, `room_id`, `status`) VALUES
('2023034', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '林俊杰', '13900000034', 'STUDENT', 7, 22, 1),
('2023035', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '陈奕迅', '13900000035', 'STUDENT', 7, 22, 1),
('2023036', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王力宏', '13900000036', 'STUDENT,INSPECTOR', 7, 23, 1),
('2023037', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '蔡依林', '13900000037', 'STUDENT', 7, 23, 1);

-- 网络安全231班学生（新增4人）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `role`, `class_id`, `room_id`, `status`) VALUES
('2023038', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '邓超', '13900000038', 'STUDENT', 8, 24, 1),
('2023039', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '孙俪', '13900000039', 'STUDENT', 8, 24, 1),
('2023040', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '吴京', '13900000040', 'STUDENT,INSPECTOR', 8, 25, 1),
('2023041', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵丽颖', '13900000041', 'STUDENT', 8, 25, 1);

-- ============================================================
-- 6. 新增卫生检查记录
-- 覆盖最近一个月，多个检查员，各种评分情况
-- ============================================================
INSERT INTO `biz_inspection` (`room_id`, `inspector_id`, `total_score`, `remarks`, `check_date`) VALUES
-- 12月第一周检查记录
(1, 4, 88, '宿舍整洁，物品摆放整齐', '2024-12-02'),
(2, 4, 75, '地面有纸屑，需加强清洁', '2024-12-02'),
(3, 4, 92, '卫生状况优秀，保持良好', '2024-12-02'),
(4, 10, 85, '阳台较乱，其他良好', '2024-12-02'),
(5, 10, 90, '非常整洁，值得表扬', '2024-12-02'),
(6, 10, 78, '垃圾未及时清理', '2024-12-02'),

-- 12月第二周检查记录
(1, 4, 90, '比上周有明显进步', '2024-12-09'),
(2, 4, 82, '有所改善，继续保持', '2024-12-09'),
(7, 8, 95, '卫生状况极佳', '2024-12-09'),
(8, 8, 88, '整体良好', '2024-12-09'),
(9, 8, 72, '被子未叠，扣分', '2024-12-09'),
(10, 15, 86, '地面干净，桌面需整理', '2024-12-09'),

-- 12月第三周检查记录
(1, 4, 85, '保持稳定', '2024-12-16'),
(3, 4, 88, '略有下降，需注意', '2024-12-16'),
(4, 10, 92, '进步明显', '2024-12-16'),
(5, 10, 88, '保持良好', '2024-12-16'),
(11, 22, 80, '阳台杂物较多', '2024-12-16'),
(12, 22, 75, '需加强打扫', '2024-12-16'),

-- 12月第四周检查记录
(1, 4, 82, '节前检查，整体不错', '2024-12-23'),
(2, 4, 90, '有很大进步', '2024-12-23'),
(3, 4, 85, '保持良好', '2024-12-23'),
(6, 10, 88, '比之前整洁很多', '2024-12-23'),
(7, 8, 92, '持续保持优秀', '2024-12-23'),
(8, 8, 85, '稳定表现', '2024-12-23'),
(16, 28, 78, '书桌较乱', '2024-12-23'),
(17, 28, 82, '整体良好', '2024-12-23'),

-- 12月最后一周检查记录（最新）
(1, 4, 88, '年终检查，表现优秀', '2024-12-30'),
(2, 4, 85, '整洁有序', '2024-12-30'),
(4, 10, 95, '本月最佳宿舍', '2024-12-30'),
(5, 10, 90, '保持优秀', '2024-12-30'),
(9, 8, 80, '有所进步', '2024-12-30'),
(10, 15, 88, '状态良好', '2024-12-30'),
(22, 33, 86, '整体表现不错', '2024-12-30'),
(23, 33, 82, '需注意个人物品摆放', '2024-12-30');

-- ============================================================
-- 7. 新增检查员权限申请记录
-- 包含待审核、已通过、已驳回各种状态
-- ============================================================
INSERT INTO `biz_application` (`applicant_id`, `application_reason`, `status`, `reviewer_id`, `review_comment`, `review_time`, `apply_time`) VALUES
-- 待审核申请
(14, '我热爱公益活动，希望能为宿舍卫生管理贡献力量', 0, NULL, NULL, NULL, '2024-12-28 10:00:00'),
(17, '我有责任心，能够公正地进行卫生检查', 0, NULL, NULL, NULL, '2024-12-29 14:30:00'),
(30, '我想锻炼自己的管理能力，同时帮助改善宿舍环境', 0, NULL, NULL, NULL, '2024-12-30 09:15:00'),

-- 已通过申请
(8, '我认真负责，已有半年的宿舍长经验', 1, 1, '表现优秀，同意申请', '2024-12-01 16:00:00', '2024-11-28 11:00:00'),
(15, '我曾获得优秀学生干部称号，有组织管理经验', 1, 2, '条件符合，通过', '2024-12-05 10:30:00', '2024-12-03 08:45:00'),
(22, '我是班级卫生委员，熟悉卫生检查标准', 1, 1, '具备相关经验，通过', '2024-12-10 14:00:00', '2024-12-08 16:20:00'),
(28, '我做事认真细致，能够客观评价', 1, 2, '态度认真，同意', '2024-12-15 11:00:00', '2024-12-12 09:30:00'),
(33, '希望能在实践中提升自己', 1, 1, '支持学生锻炼，通过', '2024-12-20 15:30:00', '2024-12-18 13:00:00'),

-- 已驳回申请
(13, '想试试当检查员', 2, 1, '申请理由过于简单，请认真填写后重新申请', '2024-12-05 11:00:00', '2024-12-04 10:00:00'),
(20, '我想当检查员', 2, 2, '请说明具体原因和自身优势', '2024-12-12 16:00:00', '2024-12-11 14:00:00');

-- ============================================================
-- 数据统计
-- ============================================================
-- 班级: 8个
-- 宿舍楼: 6栋
-- 宿舍房间: 27间
-- 用户: 管理员1人 + 教师4人 + 学生约40人
-- 卫生检查记录: 约35条
-- 检查员申请: 11条（3待审核 + 5通过 + 2驳回）
