package com.nijika.service;

import com.nijika.entity.BizApplication;
import java.util.List;

/**
 * 检查员申请服务接口
 * 功能：学生提交申请、教师审核（通过/驳回）、申请记录查询
 */
public interface BizApplicationService {
    void submitApplication(BizApplication application);

    void approveApplication(Long id, Long reviewerId);

    void rejectApplication(Long id, Long reviewerId, String reviewComment);

    BizApplication getApplicationById(Long id);

    List<BizApplication> getAllApplications();

    List<BizApplication> getApplicationsByApplicantId(Long applicantId);

    List<BizApplication> getPendingApplications();
}
