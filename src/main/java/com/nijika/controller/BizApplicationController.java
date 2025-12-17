package com.nijika.controller;

import com.nijika.common.Result;
import com.nijika.entity.BizApplication;
import com.nijika.service.BizApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "检查员申请管理", description = "学生申请检查员权限及教师审核接口")
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class BizApplicationController {

    private final BizApplicationService applicationService;

    @Operation(summary = "提交检查员申请", description = "学生提交检查员权限申请")
    @PostMapping
    public Result<Void> submitApplication(@RequestBody BizApplication application) {
        applicationService.submitApplication(application);
        return Result.success();
    }

    @Operation(summary = "通过申请", description = "教师审核通过,自动赋予检查员权限")
    @PutMapping("/{id}/approve")
    public Result<Void> approveApplication(
            @Parameter(description = "申请ID") @PathVariable Long id,
            @Parameter(description = "审核人ID") @RequestParam Long reviewerId) {
        applicationService.approveApplication(id, reviewerId);
        return Result.success();
    }

    @Operation(summary = "驳回申请", description = "教师驳回申请并填写驳回理由")
    @PutMapping("/{id}/reject")
    public Result<Void> rejectApplication(
            @PathVariable Long id,
            @RequestParam Long reviewerId,
            @Parameter(description = "驳回理由") @RequestParam String reviewComment) {
        applicationService.rejectApplication(id, reviewerId, reviewComment);
        return Result.success();
    }

    @Operation(summary = "查询申请详情")
    @GetMapping("/{id}")
    public Result<BizApplication> getApplicationById(@PathVariable Long id) {
        return Result.success(applicationService.getApplicationById(id));
    }

    @Operation(summary = "查询所有申请记录")
    @GetMapping("/all")
    public Result<List<BizApplication>> getAllApplications() {
        return Result.success(applicationService.getAllApplications());
    }

    @Operation(summary = "查询某用户的申请记录")
    @GetMapping("/applicant/{applicantId}")
    public Result<List<BizApplication>> getApplicationsByApplicantId(@PathVariable Long applicantId) {
        return Result.success(applicationService.getApplicationsByApplicantId(applicantId));
    }

    @Operation(summary = "查询待审核申请列表", description = "教师查看所有待审核的申请")
    @GetMapping("/pending")
    public Result<List<BizApplication>> getPendingApplications() {
        return Result.success(applicationService.getPendingApplications());
    }
}
