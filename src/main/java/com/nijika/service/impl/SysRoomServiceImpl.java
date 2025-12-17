package com.nijika.service.impl;

import com.nijika.entity.SysRoom;
import com.nijika.mapper.SysRoomMapper;
import com.nijika.mapper.SysUserMapper;
import com.nijika.service.SysRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoomServiceImpl implements SysRoomService {

    private final SysRoomMapper roomMapper;
    private final SysUserMapper userMapper;

    @Override
    @Transactional
    public void createRoom(SysRoom room) {
        SysRoom existing = roomMapper.selectByBuildingAndRoom(room.getBuildingId(), room.getRoomNumber());
        if (existing != null) {
            throw new IllegalArgumentException("该楼栋的房间号已存在");
        }
        roomMapper.insert(room);
    }

    @Override
    @Transactional
    public void updateRoom(SysRoom room) {
        roomMapper.updateById(room);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        int count = userMapper.selectByRoomId(id).size();
        if (count > 0) {
            throw new IllegalArgumentException("该宿舍还有住户,无法删除");
        }
        roomMapper.deleteById(id);
    }

    @Override
    public SysRoom getRoomById(Long id) {
        return roomMapper.selectById(id);
    }

    @Override
    public List<SysRoom> getAllRooms() {
        return roomMapper.selectAll();
    }

    @Override
    public List<SysRoom> getRoomsByBuildingId(Long buildingId) {
        return roomMapper.selectByBuildingId(buildingId);
    }
}
