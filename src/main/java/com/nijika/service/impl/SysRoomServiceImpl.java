package com.nijika.service.impl;

import com.nijika.entity.SysRoom;
import com.nijika.mapper.SysRoomMapper;
import com.nijika.mapper.SysUserMapper;
import com.nijika.service.SysRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 宿舍房间业务实现
 * 核心逻辑：房间号唯一性校验（同一楼栋内）+ 删除前级联检查（防止误删有住户的宿舍）
 */
@Service
@RequiredArgsConstructor
public class SysRoomServiceImpl implements SysRoomService {

    private final SysRoomMapper roomMapper;
    private final SysUserMapper userMapper; // 用于检查宿舍是否有住户

    @Override
    @Transactional
    public void createRoom(SysRoom room) {
        // 校验房间号唯一性（同一楼栋内）
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
        // 级联检查：防止删除有住户的宿舍
        int count = userMapper.selectByRoomId(id).size();
        if (count > 0) {
            throw new IllegalArgumentException("该宿舍还有住户,无法删除");
        }
        roomMapper.deleteById(id); // 软删除
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
    public List<SysRoom> getRoomsByBuildingId(Long buildingId) { // 按楼栋查询房间列表
        return roomMapper.selectByBuildingId(buildingId);
    }
}
