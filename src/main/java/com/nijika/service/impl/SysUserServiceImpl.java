package com.nijika.service.impl;

import com.nijika.entity.SysUser;
import com.nijika.mapper.SysUserMapper;
import com.nijika.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(SysUser user) {
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("STUDENT");
        }
        userMapper.insert(user);
    }

    @Override
    @Transactional
    public void updateUser(SysUser user) {
        SysUser existing = userMapper.selectById(user.getId());
        if (existing == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (!user.getPassword().equals(existing.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public SysUser getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<SysUser> getAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public List<SysUser> getUsersByRoomId(Long roomId) {
        return userMapper.selectByRoomId(roomId);
    }

    @Override
    public List<SysUser> getUsersByClassId(Long classId) {
        return userMapper.selectByClassId(classId);
    }

    @Override
    public List<SysUser> getInspectors() {
        return userMapper.selectByRoleLike("INSPECTOR");
    }

    @Override
    public SysUser login(String username, String password) {
        SysUser user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("用户名不存在");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }
        return user;
    }
}
