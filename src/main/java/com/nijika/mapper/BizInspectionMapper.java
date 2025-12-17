package com.nijika.mapper;

import com.nijika.entity.BizInspection;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

public interface BizInspectionMapper {
    int insert(BizInspection bizInspection);

    int deleteById(Long id);

    int updateById(BizInspection bizInspection);

    BizInspection selectById(Long id);

    List<BizInspection> selectAll();

    List<BizInspection> selectByRoomId(@Param("roomId") Long roomId);

    List<BizInspection> selectByRoomIdAndDateRange(@Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<BizInspection> selectByInspectorId(@Param("inspectorId") Long inspectorId);
}
