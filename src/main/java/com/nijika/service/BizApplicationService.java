package com.nijika.service;

import com.nijika.entity.BizApplication;
import java.util.List;

public interface BizApplicationService {
    void submitApplication(BizApplication application);

    void approveApplication(Long id, Long reviewerId);

    void rejectApplication(Long id, Long reviewerId, String reviewComment);

    BizApplication getApplicationById(Long id);

    List<BizApplication> getAllApplications();

    List<BizApplication> getApplicationsByApplicantId(Long applicantId);

    List<BizApplication> getPendingApplications();
}
