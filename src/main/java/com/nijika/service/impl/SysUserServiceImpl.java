package com.nijika.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nijika.common.PageResult;
import com.nijika.entity.SysUser;
import com.nijika.mapper.SysUserMapper;
import com.nijika.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户业务实现 - 核心逻辑：BCrypt密码加密 + LIKE查询多角色
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // BCrypt单向加密

    @Override
    @Transactional
    public void createUser(SysUser user) {
        if (userMapper.selectByUsername(user.getUsername()) != null) { // 校验用户名唯一性
            throw new IllegalArgumentException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // BCrypt加密密码
        if (user.getStatus() == null) { // 默认正常状态
            user.setStatus(1);
        }
        if (user.getRole() == null || user.getRole().isEmpty()) { // 默认学生角色
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
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) { // 空密码=不修改
            user.setPassword(existing.getPassword());
        } else if (!user.getPassword().equals(existing.getPassword())) { // 密码变化才重新加密
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) { // 软删除：设置deleted_at
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
    public List<SysUser> getUsersByRoomId(Long roomId) { // 查询某宿舍住户
        return userMapper.selectByRoomId(roomId);
    }

    @Override
    public List<SysUser> getUsersByClassId(Long classId) { // 查询某班级学生
        return userMapper.selectByClassId(classId);
    }

    @Override
    public List<SysUser> getInspectors() { // LIKE '%INSPECTOR%' 查询所有检查员（含多角色）
        return userMapper.selectByRoleLike("INSPECTOR");
    }

    @Override
    public SysUser login(String username, String password) {
        SysUser user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("用户名不存在");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) { // BCrypt验证密码
            throw new IllegalArgumentException("密码错误");
        }
        return user; // 返回用户信息（前端需要role字段生成Token）
    }

    @Override
    public PageResult<SysUser> getUsersWithPage(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = userMapper.selectWithSearch(keyword);
        return PageResult.of(new PageInfo<>(list));
    }
}
