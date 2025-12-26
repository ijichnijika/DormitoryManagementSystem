package com.nijika.service;

import com.nijika.common.PageResult;
import com.nijika.entity.SysUser;
import java.util.List;

/**
 * 用户服务接口
 * 功能：用户增删改查、登录认证、角色筛选、分页查询
 */
public interface SysUserService {
    void createUser(SysUser user);

    void updateUser(SysUser user);

    void deleteUser(Long id);

    SysUser getUserById(Long id);

    List<SysUser> getAllUsers();

    SysUser getUserByUsername(String username);

    List<SysUser> getUsersByRoomId(Long roomId);

    List<SysUser> getUsersByClassId(Long classId);

    List<SysUser> getInspectors();

    SysUser login(String username, String password);

    PageResult<SysUser> getUsersWithPage(Integer pageNum, Integer pageSize, String keyword);
}
