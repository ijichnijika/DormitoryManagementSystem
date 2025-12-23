package com.nijika.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nijika.common.PageResult;
import com.nijika.dto.InspectionQuery;
import com.nijika.entity.BizInspection;
import com.nijika.mapper.BizInspectionMapper;
import com.nijika.service.BizInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 卫生检查业务实现 - 核心业务逻辑：三层防护（前端+Service+数据库CHECK）
 */
@Service
@RequiredArgsConstructor
public class BizInspectionServiceImpl implements BizInspectionService {
    private final BizInspectionMapper inspectionMapper;

    @Override
    @Transactional
    public void createInspection(BizInspection inspection) {
        if (inspection.getTotalScore() < 0 || inspection.getTotalScore() > 100) { // Service层第二道防线
            throw new IllegalArgumentException("分数必须在0-100之间");
        }
        if (inspection.getCheckDate() == null) { // 默认当天检查
            inspection.setCheckDate(java.time.LocalDate.now());
        }
        inspectionMapper.insert(inspection);
    }

    @Override
    @Transactional
    public void updateInspection(BizInspection inspection) { // 教师修正功能，会更新modifier_id
        if (inspection.getTotalScore() < 0 || inspection.getTotalScore() > 100) {
            throw new IllegalArgumentException("分数必须在0-100之间");
        }
        inspectionMapper.updateById(inspection);
    }

    @Override
    @Transactional
    public void deleteInspection(Long id) { // 教师删除错误记录（物理删除）
        inspectionMapper.deleteById(id);
    }

    @Override
    public BizInspection getInspectionById(Long id) {
        return inspectionMapper.selectById(id);
    }

    @Override
    public List<BizInspection> getAllInspections() { // 教师查看全校记录
        return inspectionMapper.selectAll();
    }

    @Override
    public List<BizInspection> getInspectionsByRoomId(Long roomId) { // 学生查看本宿舍记录
        return inspectionMapper.selectByRoomId(roomId);
    }

    @Override
    public List<BizInspection> getInspectionsByRoomIdAndDateRange(Long roomId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return inspectionMapper.selectByRoomIdAndDateRange(roomId, startDate, endDate); // 按时间段查询（统计用）
    }

    @Override
    public List<BizInspection> getInspectionsByInspectorId(Long inspectorId) { // 统计检查员工作量
        return inspectionMapper.selectByInspectorId(inspectorId);
    }

    @Override
    public PageResult<BizInspection> pageInspections(InspectionQuery query) { // 分页查询：PageHelper自动拦截SQL
        PageHelper.startPage(query.getPageNum(), query.getPageSize()); // 核心：startPage会拦截紧跟的第一条SELECT
        List<BizInspection> list = inspectionMapper.selectByCondition(query); // 执行查询，PageHelper自动添加LIMIT
        PageInfo<BizInspection> pageInfo = new PageInfo<>(list); // 封装分页信息（含total）
        return PageResult.of(pageInfo); // 转换为自定义PageResult
    }
}
