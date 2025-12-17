package com.nijika.service;

import com.nijika.entity.SysRoom;
import java.util.List;

public interface SysRoomService {
    void createRoom(SysRoom room);

    void updateRoom(SysRoom room);

    void deleteRoom(Long id);

    SysRoom getRoomById(Long id);

    List<SysRoom> getAllRooms();

    List<SysRoom> getRoomsByBuildingId(Long buildingId);
}
