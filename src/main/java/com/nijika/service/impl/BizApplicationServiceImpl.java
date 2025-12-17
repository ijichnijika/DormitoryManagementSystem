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

        if (user.getRole().contains("INSPECTOR")) {
            throw new IllegalArgumentException("您已经是检查员,无需重复申请");
        }

        BizApplication pending = applicationMapper.selectPendingByApplicantId(application.getApplicantId());
        if (pending != null) {
            throw new IllegalArgumentException("您有待审核的申请,请勿重复提交");
        }

        application.setStatus(0);
        applicationMapper.insert(application);
    }

    @Override
    @Transactional
    public void approveApplication(Long id, Long reviewerId) {
        BizApplication application = applicationMapper.selectById(id);
        if (application == null) {
            throw new IllegalArgumentException("申请不存在");
        }
        if (application.getStatus() != 0) {
            throw new IllegalArgumentException("该申请已处理");
        }

        application.setStatus(1);
        application.setReviewerId(reviewerId);
        application.setReviewTime(LocalDateTime.now());
        applicationMapper.updateById(application);

        SysUser user = userMapper.selectById(application.getApplicantId());
        if (!user.getRole().contains("INSPECTOR")) {
            user.setRole(user.getRole() + ",INSPECTOR");
            userMapper.updateById(user);
        }
    }

    @Override
    @Transactional
    public void rejectApplication(Long id, Long reviewerId, String reviewComment) {
        BizApplication application = applicationMapper.selectById(id);
        if (application == null) {
            throw new IllegalArgumentException("申请不存在");
        }
        if (application.getStatus() != 0) {
            throw new IllegalArgumentException("该申请已处理");
        }

        application.setStatus(2);
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
    public List<BizApplication> getApplicationsByApplicantId(Long applicantId) {
        return applicationMapper.selectByApplicantId(applicantId);
    }

    @Override
    public List<BizApplication> getPendingApplications() {
        return applicationMapper.selectByStatus(0);
    }
}
