package com.nijika.service;

import com.nijika.entity.SysUser;
import java.util.List;

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
}
