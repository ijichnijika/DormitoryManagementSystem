package com.nijika.service.impl;

import com.nijika.entity.SysClass;
import com.nijika.mapper.SysClassMapper;
import com.nijika.service.SysClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysClassServiceImpl implements SysClassService {

    private final SysClassMapper classMapper;

    @Override
    @Transactional
    public void createClass(SysClass sysClass) {
        if (classMapper.selectByClassName(sysClass.getClassName()) != null) {
            throw new IllegalArgumentException("班级名称已存在");
        }
        classMapper.insert(sysClass);
    }

    @Override
    @Transactional
    public void updateClass(SysClass sysClass) {
        classMapper.updateById(sysClass);
    }

    @Override
    @Transactional
    public void deleteClass(Long id) {
        classMapper.deleteById(id);
    }

    @Override
    public SysClass getClassById(Long id) {
        return classMapper.selectById(id);
    }

    @Override
    public List<SysClass> getAllClasses() {
        return classMapper.selectAll();
    }
}
