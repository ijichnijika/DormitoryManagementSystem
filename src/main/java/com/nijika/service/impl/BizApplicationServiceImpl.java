package com.nijika.service.impl;

import com.nijika.entity.BizApplication;
import com.nijika.entity.SysUser;
import com.nijika.mapper.BizApplicationMapper;
import com.nijika.mapper.SysUserMapper;
import com.nijika.service.BizApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检查员申请业务实现 - 核心逻辑：状态机流转 + 角色字符串拼接
 */
@Service
@RequiredArgsConstructor
public class BizApplicationServiceImpl implements BizApplicationService {
    private final BizApplicationMapper applicationMapper;
    private final SysUserMapper userMapper;

    @Override
    @Transactional
    public void submitApplication(BizApplication application) {
        SysUser user = userMapper.selectById(application.getApplicantId());
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (user.getRole().contains("INSPECTOR")) { // 防止已是检查员的重复申请
            throw new IllegalArgumentException("您已经是检查员,无需重复申请");
        }
        BizApplication pending = applicationMapper.selectPendingByApplicantId(application.getApplicantId());
        if (pending != null) { // 防止重复提交待审核申请
            throw new IllegalArgumentException("您有待审核的申请,请勿重复提交");
        }
        application.setStatus(0); // 状态机：初始化为待审核
        applicationMapper.insert(application);
    }

    @Override
    @Transactional
    public void approveApplication(Long id, Long reviewerId) { // 审核通过：更新申请状态 + 同步修改用户角色
        BizApplication application = applicationMapper.selectById(id);
        if (application == null) {
            throw new IllegalArgumentException("申请不存在");
        }
        if (application.getStatus() != 0) { // 防止重复审核
            throw new IllegalArgumentException("该申请已处理");
        }
        application.setStatus(1); // 状态机：0待审核 → 1已通过
        application.setReviewerId(reviewerId);
        application.setReviewTime(LocalDateTime.now());
        applicationMapper.updateById(application);
        SysUser user = userMapper.selectById(application.getApplicantId());
        if (!user.getRole().contains("INSPECTOR")) { // 关键逻辑：拼接角色字符串 "STUDENT" → "STUDENT,INSPECTOR"
            user.setRole(user.getRole() + ",INSPECTOR");
            userMapper.updateById(user);
        }
    }

    @Override
    @Transactional
    public void rejectApplication(Long id, Long reviewerId, String reviewComment) { // 审核驳回：只更新申请状态，不修改用户角色
        BizApplication application = applicationMapper.selectById(id);
        if (application == null) {
            throw new IllegalArgumentException("申请不存在");
        }
        if (application.getStatus() != 0) {
            throw new IllegalArgumentException("该申请已处理");
        }
        application.setStatus(2); // 状态机：0待审核 → 2已驳回
        application.setReviewerId(reviewerId);
        application.setReviewComment(reviewComment);
        application.setReviewTime(LocalDateTime.now());
        applicationMapper.updateById(application);
    }

    @Override
    public BizApplication getApplicationById(Long id) {
        return applicationMapper.selectById(id);
    }

    @Override
    public List<BizApplication> getAllApplications() {
        return applicationMapper.selectAll();
    }

    @Override
    public List<BizApplication> getApplicationsByApplicantId(Long applicantId) { // 学生查看自己的申请历史
        return applicationMapper.selectByApplicantId(applicantId);
    }

    @Override
    public List<BizApplication> getPendingApplications() { // 教师查看待审核列表
        return applicationMapper.selectByStatus(0);
    }
}
