package com.nijika.service;

import com.nijika.entity.SysClass;
import java.util.List;

/**
 * 班级服务接口
 * 功能：班级信息的增删改查
 */
public interface SysClassService {
    void createClass(SysClass sysClass);

    void updateClass(SysClass sysClass);

    void deleteClass(Long id);

    SysClass getClassById(Long id);

    List<SysClass> getAllClasses();
}
