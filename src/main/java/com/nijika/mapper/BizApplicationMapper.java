package com.nijika.mapper;

import com.nijika.entity.BizApplication;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 检查员申请数据访问层
 * SQL映射文件：resources/mapper/BizApplicationMapper.xml
 */
public interface BizApplicationMapper {
    int insert(BizApplication bizApplication);

    int deleteById(Long id);

    int updateById(BizApplication bizApplication);

    BizApplication selectById(Long id);

    List<BizApplication> selectAll();

    List<BizApplication> selectByApplicantId(@Param("applicantId") Long applicantId);

    List<BizApplication> selectByStatus(@Param("status") Integer status);

    BizApplication selectPendingByApplicantId(@Param("applicantId") Long applicantId);
}
