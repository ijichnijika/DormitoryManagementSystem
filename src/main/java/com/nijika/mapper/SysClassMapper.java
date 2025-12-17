package com.nijika.mapper;

import com.nijika.entity.SysClass;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysClassMapper {
    int insert(SysClass sysClass);

    int deleteById(Long id);

    int updateById(SysClass sysClass);

    SysClass selectById(Long id);

    List<SysClass> selectAll();

    SysClass selectByClassName(@Param("className") String className);
}
