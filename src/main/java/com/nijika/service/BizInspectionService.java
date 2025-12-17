package com.nijika.service;

import com.nijika.entity.BizInspection;
import java.time.LocalDate;
import java.util.List;

public interface BizInspectionService {
    void createInspection(BizInspection inspection);

    void updateInspection(BizInspection inspection);

    void deleteInspection(Long id);

    BizInspection getInspectionById(Long id);

    List<BizInspection> getAllInspections();

    List<BizInspection> getInspectionsByRoomId(Long roomId);

    List<BizInspection> getInspectionsByRoomIdAndDateRange(Long roomId, LocalDate startDate, LocalDate endDate);

    List<BizInspection> getInspectionsByInspectorId(Long inspectorId);
}
