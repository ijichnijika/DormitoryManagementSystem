package com.nijika.service;

import com.nijika.common.PageResult;
import com.nijika.dto.InspectionQuery;
import com.nijika.entity.BizInspection;
import java.time.LocalDateTime;
import java.util.List;

public interface BizInspectionService {
    void createInspection(BizInspection inspection);

    void updateInspection(BizInspection inspection);

    void deleteInspection(Long id);

    BizInspection getInspectionById(Long id);

    List<BizInspection> getAllInspections();

    List<BizInspection> getInspectionsByRoomId(Long roomId);

    List<BizInspection> getInspectionsByRoomIdAndDateRange(Long roomId, LocalDateTime startDate, LocalDateTime endDate);

    List<BizInspection> getInspectionsByInspectorId(Long inspectorId);

    // 多条件分页查询
    PageResult<BizInspection> pageInspections(InspectionQuery query);
}
