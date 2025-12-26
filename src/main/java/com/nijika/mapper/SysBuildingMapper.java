package com.nijika.mapper;

import com.nijika.entity.SysBuilding;
import java.util.List;

/**
 * 宿舍楼数据访问层
 * SQL映射文件：resources/mapper/SysBuildingMapper.xml
 */
public interface SysBuildingMapper {
    int insert(SysBuilding sysBuilding);

    int deleteById(Long id);

    int updateById(SysBuilding sysBuilding);

    SysBuilding selectById(Long id);

    List<SysBuilding> selectAll();
}
