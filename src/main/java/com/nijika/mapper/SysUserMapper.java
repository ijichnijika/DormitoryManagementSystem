package com.nijika.mapper;

import com.nijika.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysUserMapper {
    int insert(SysUser sysUser);

    int deleteById(Long id);

    int updateById(SysUser sysUser);

    SysUser selectById(Long id);

    List<SysUser> selectAll();

    SysUser selectByUsername(@Param("username") String username);

    List<SysUser> selectByRoomId(@Param("roomId") Long roomId);

    List<SysUser> selectByClassId(@Param("classId") Long classId);

    List<SysUser> selectByRoleLike(@Param("role") String role);
}
