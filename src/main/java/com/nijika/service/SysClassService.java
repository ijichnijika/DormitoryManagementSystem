package com.nijika.service;

import com.nijika.entity.SysClass;
import java.util.List;

public interface SysClassService {
    void createClass(SysClass sysClass);

    void updateClass(SysClass sysClass);

    void deleteClass(Long id);

    SysClass getClassById(Long id);

    List<SysClass> getAllClasses();
}
