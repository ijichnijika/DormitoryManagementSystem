package com.nijika.controller;

import com.nijika.common.PageResult;
import com.nijika.common.Result;
import com.nijika.dto.InspectionQuery;
import com.nijika.entity.BizInspection;
import com.nijika.service.BizInspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "卫生检查管理", description = "卫生检查记录的增删改查接口")
@RestController
@RequestMapping("/api/inspection")
@RequiredArgsConstructor
public class BizInspectionController {

    private final BizInspectionService inspectionService;

    @Operation(summary = "创建检查记录", description = "检查员录入卫生检查记录")
    @PostMapping
    public Result<Void> createInspection(@RequestBody BizInspection inspection) {
        inspectionService.createInspection(inspection);
        return Result.success();
    }

    @Operation(summary = "更新检查记录", description = "教师修改检查记录")
    @PutMapping
    public Result<Void> updateInspection(@RequestBody BizInspection inspection) {
        inspectionService.updateInspection(inspection);
        return Result.success();
    }

    @Operation(summary = "删除检查记录", description = "教师删除错误的检查记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteInspection(@Parameter(description = "检查记录ID") @PathVariable Long id) {
        inspectionService.deleteInspection(id);
        return Result.success();
    }

    @Operation(summary = "查询检查记录详情")
    @GetMapping("/{id}")
    public Result<BizInspection> getInspectionById(@PathVariable Long id) {
        return Result.success(inspectionService.getInspectionById(id));
    }

    @Operation(summary = "查询所有检查记录")
    @GetMapping("/all")
    public Result<List<BizInspection>> getAllInspections() {
        return Result.success(inspectionService.getAllInspections());
    }

    @Operation(summary = "查询某宿舍的检查记录")
    @GetMapping("/room/{roomId}")
    public Result<List<BizInspection>> getInspectionsByRoomId(@PathVariable Long roomId) {
        return Result.success(inspectionService.getInspectionsByRoomId(roomId));
    }

    @Operation(summary = "按日期范围查询宿舍检查记录", description = "学生查询本宿舍某时间段的卫生记录")
    @GetMapping("/room/{roomId}/date-range")
    public Result<List<BizInspection>> getInspectionsByDateRange(
            @PathVariable Long roomId,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return Result.success(inspectionService.getInspectionsByRoomIdAndDateRange(roomId, startDate, endDate));
    }

    @Operation(summary = "查询某检查员的工作记录")
    @GetMapping("/inspector/{inspectorId}")
    public Result<List<BizInspection>> getInspectionsByInspectorId(@PathVariable Long inspectorId) {
        return Result.success(inspectionService.getInspectionsByInspectorId(inspectorId));
    }

    @Operation(summary = "分页查询检查记录", description = "支持楼栋/宿舍/检查员/分数/日期范围多条件组合搜索")
    @PostMapping("/page")
    public Result<PageResult<BizInspection>> pageInspections(@RequestBody InspectionQuery query) {
        return Result.success(inspectionService.pageInspections(query));
    }
}
