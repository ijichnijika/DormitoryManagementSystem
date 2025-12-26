package com.nijika.mapper;

import com.nijika.dto.InspectionQuery;
import com.nijika.entity.BizInspection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 卫生检查数据访问层
 * SQL映射文件：resources/mapper/BizInspectionMapper.xml
 * 功能：检查记录的增删改查，支持多条件动态SQL查询
 */
@Mapper
public interface BizInspectionMapper {
    int insert(BizInspection bizInspection);

    int deleteById(Long id);

    int updateById(BizInspection bizInspection);

    BizInspection selectById(Long id);

    List<BizInspection> selectAll();

    List<BizInspection> selectByRoomId(@Param("roomId") Long roomId);

    List<BizInspection> selectByRoomIdAndDateRange(@Param("roomId") Long roomId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<BizInspection> selectByInspectorId(@Param("inspectorId") Long inspectorId);

    // 多条件分页查询 - PageHelper自动拦截生成count和limit SQL
    List<BizInspection> selectByCondition(@Param("query") InspectionQuery query);
}
