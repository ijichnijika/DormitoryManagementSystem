package com.nijika.controller;

import com.nijika.common.Result;
import com.nijika.entity.SysBuilding;
import com.nijika.service.SysBuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "宿舍楼管理", description = "宿舍楼信息的增删改查接口")
@RestController
@RequestMapping("/api/building")
@RequiredArgsConstructor
public class SysBuildingController {

    private final SysBuildingService buildingService;

    @Operation(summary = "创建宿舍楼")
    @PostMapping
    public Result<Void> createBuilding(@RequestBody SysBuilding building) {
        buildingService.createBuilding(building);
        return Result.success();
    }

    @Operation(summary = "更新宿舍楼信息")
    @PutMapping
    public Result<Void> updateBuilding(@RequestBody SysBuilding building) {
        buildingService.updateBuilding(building);
        return Result.success();
    }

    @Operation(summary = "删除宿舍楼")
    @DeleteMapping("/{id}")
    public Result<Void> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return Result.success();
    }

    @Operation(summary = "查询宿舍楼详情")
    @GetMapping("/{id}")
    public Result<SysBuilding> getBuildingById(@PathVariable Long id) {
        return Result.success(buildingService.getBuildingById(id));
    }

    @Operation(summary = "查询所有宿舍楼")
    @GetMapping("/all")
    public Result<List<SysBuilding>> getAllBuildings() {
        return Result.success(buildingService.getAllBuildings());
    }
}
