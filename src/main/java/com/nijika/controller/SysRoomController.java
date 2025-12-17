package com.nijika.controller;

import com.nijika.common.Result;
import com.nijika.entity.SysRoom;
import com.nijika.service.SysRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "宿舍房间管理", description = "宿舍房间信息的增删改查接口")
@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class SysRoomController {

    private final SysRoomService roomService;

    @Operation(summary = "创建宿舍房间")
    @PostMapping
    public Result<Void> createRoom(@RequestBody SysRoom room) {
        roomService.createRoom(room);
        return Result.success();
    }

    @Operation(summary = "更新宿舍房间信息")
    @PutMapping
    public Result<Void> updateRoom(@RequestBody SysRoom room) {
        roomService.updateRoom(room);
        return Result.success();
    }

    @Operation(summary = "删除宿舍房间")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return Result.success();
    }

    @Operation(summary = "查询宿舍房间详情")
    @GetMapping("/{id}")
    public Result<SysRoom> getRoomById(@PathVariable Long id) {
        return Result.success(roomService.getRoomById(id));
    }

    @Operation(summary = "查询所有宿舍房间")
    @GetMapping("/all")
    public Result<List<SysRoom>> getAllRooms() {
        return Result.success(roomService.getAllRooms());
    }

    @Operation(summary = "查询某楼栋的所有房间")
    @GetMapping("/building/{buildingId}")
    public Result<List<SysRoom>> getRoomsByBuildingId(@PathVariable Long buildingId) {
        return Result.success(roomService.getRoomsByBuildingId(buildingId));
    }
}
