package com.nijika.service;

import com.nijika.entity.SysRoom;
import java.util.List;

/**
 * 宿舍房间服务接口
 * 功能：房间信息的增删改查、按楼栋查询
 */
public interface SysRoomService {
    void createRoom(SysRoom room);

    void updateRoom(SysRoom room);

    void deleteRoom(Long id);

    SysRoom getRoomById(Long id);

    List<SysRoom> getAllRooms();

    List<SysRoom> getRoomsByBuildingId(Long buildingId);
}
