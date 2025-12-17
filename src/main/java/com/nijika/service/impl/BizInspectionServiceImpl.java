package com.nijika.service.impl;

import com.nijika.entity.BizInspection;
import com.nijika.mapper.BizInspectionMapper;
import com.nijika.service.BizInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BizInspectionServiceImpl implements BizInspectionService {

    private final BizInspectionMapper inspectionMapper;

    @Override
    @Transactional
    public void createInspection(BizInspection inspection) {
        if (inspection.getTotalScore() < 0 || inspection.getTotalScore() > 100) {
            throw new IllegalArgumentException("分数必须在0-100之间");
        }
        if (inspection.getCheckDate() == null) {
            inspection.setCheckDate(LocalDate.now());
        }
        inspectionMapper.insert(inspection);
    }

    @Override
    @Transactional
    public void updateInspection(BizInspection inspection) {
        if (inspection.getTotalScore() < 0 || inspection.getTotalScore() > 100) {
            throw new IllegalArgumentException("分数必须在0-100之间");
        }
        inspectionMapper.updateById(inspection);
    }

    @Override
    @Transactional
    public void deleteInspection(Long id) {
        inspectionMapper.deleteById(id);
    }

    @Override
    public BizInspection getInspectionById(Long id) {
        return inspectionMapper.selectById(id);
    }

    @Override
    public List<BizInspection> getAllInspections() {
        return inspectionMapper.selectAll();
    }

    @Override
    public List<BizInspection> getInspectionsByRoomId(Long roomId) {
        return inspectionMapper.selectByRoomId(roomId);
    }

    @Override
    public List<BizInspection> getInspectionsByRoomIdAndDateRange(Long roomId, LocalDate startDate, LocalDate endDate) {
        return inspectionMapper.selectByRoomIdAndDateRange(roomId, startDate, endDate);
    }

    @Override
    public List<BizInspection> getInspectionsByInspectorId(Long inspectorId) {
        return inspectionMapper.selectByInspectorId(inspectorId);
    }
}
