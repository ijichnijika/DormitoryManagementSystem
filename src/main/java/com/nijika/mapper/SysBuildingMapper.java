package com.nijika.mapper;

import com.nijika.entity.SysBuilding;
import java.util.List;

public interface SysBuildingMapper {
    int insert(SysBuilding sysBuilding);

    int deleteById(Long id);

    int updateById(SysBuilding sysBuilding);

    SysBuilding selectById(Long id);

    List<SysBuilding> selectAll();
}
