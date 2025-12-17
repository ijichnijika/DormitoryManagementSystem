package com.nijika.mapper;

import com.nijika.entity.SysRoom;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysRoomMapper {
    int insert(SysRoom sysRoom);

    int deleteById(Long id);

    int updateById(SysRoom sysRoom);

    SysRoom selectById(Long id);

    List<SysRoom> selectAll();

    List<SysRoom> selectByBuildingId(@Param("buildingId") Long buildingId);

    SysRoom selectByBuildingAndRoom(@Param("buildingId") Long buildingId, @Param("roomNumber") String roomNumber);
}
