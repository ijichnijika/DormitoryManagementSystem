package com.nijika.controller;

import com.nijika.common.Result;
import com.nijika.entity.SysClass;
import com.nijika.service.SysClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "班级管理", description = "班级信息的增删改查接口")
@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class SysClassController {

    private final SysClassService classService;

    @Operation(summary = "创建班级")
    @PostMapping
    public Result<Void> createClass(@RequestBody SysClass sysClass) {
        classService.createClass(sysClass);
        return Result.success();
    }

    @Operation(summary = "更新班级信息")
    @PutMapping
    public Result<Void> updateClass(@RequestBody SysClass sysClass) {
        classService.updateClass(sysClass);
        return Result.success();
    }

    @Operation(summary = "删除班级")
    @DeleteMapping("/{id}")
    public Result<Void> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return Result.success();
    }

    @Operation(summary = "查询班级详情")
    @GetMapping("/{id}")
    public Result<SysClass> getClassById(@PathVariable Long id) {
        return Result.success(classService.getClassById(id));
    }

    @Operation(summary = "查询所有班级")
    @GetMapping("/all")
    public Result<List<SysClass>> getAllClasses() {
        return Result.success(classService.getAllClasses());
    }
}
