package com.nijika.controller;

import com.nijika.common.Result;
import com.nijika.entity.SysUser;
import com.nijika.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理", description = "用户信息的增删改查接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @Operation(summary = "创建用户")
    @PostMapping
    public Result<Void> createUser(@RequestBody SysUser user) {
        userService.createUser(user);
        return Result.success();
    }

    @Operation(summary = "更新用户信息")
    @PutMapping
    public Result<Void> updateUser(@RequestBody SysUser user) {
        userService.updateUser(user);
        return Result.success();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @Operation(summary = "查询用户详情")
    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @Operation(summary = "查询所有用户")
    @GetMapping("/all")
    public Result<List<SysUser>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @Operation(summary = "根据用户名查询")
    @GetMapping("/username/{username}")
    public Result<SysUser> getUserByUsername(@PathVariable String username) {
        return Result.success(userService.getUserByUsername(username));
    }

    @Operation(summary = "查询某宿舍的住户")
    @GetMapping("/room/{roomId}")
    public Result<List<SysUser>> getUsersByRoomId(@PathVariable Long roomId) {
        return Result.success(userService.getUsersByRoomId(roomId));
    }

    @Operation(summary = "查询某班级的学生")
    @GetMapping("/class/{classId}")
    public Result<List<SysUser>> getUsersByClassId(@PathVariable Long classId) {
        return Result.success(userService.getUsersByClassId(classId));
    }

    @Operation(summary = "查询所有检查员")
    @GetMapping("/inspectors")
    public Result<List<SysUser>> getInspectors() {
        return Result.success(userService.getInspectors());
    }
}
