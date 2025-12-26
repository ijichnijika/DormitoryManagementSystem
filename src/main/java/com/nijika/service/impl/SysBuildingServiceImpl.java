package com.nijika.service.impl;

import com.nijika.entity.SysBuilding;
import com.nijika.mapper.SysBuildingMapper;
import com.nijika.service.SysBuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 宿舍楼业务实现
 * 核心特性：Redis缓存优化（@Cacheable/@CacheEvict）
 * 缓存策略：查询方法缓存，增删改方法清空缓存
 */
@Service
@RequiredArgsConstructor
public class SysBuildingServiceImpl implements SysBuildingService {

    private final SysBuildingMapper buildingMapper;

    @Override
    @Transactional
    @CacheEvict(value = "buildings", allEntries = true) // 创建后清空所有缓存
    public void createBuilding(SysBuilding building) {
        buildingMapper.insert(building);
    }

    @Override
    @Transactional
    @CacheEvict(value = "buildings", allEntries = true) // 更新后清空所有缓存
    public void updateBuilding(SysBuilding building) {
        buildingMapper.updateById(building);
    }

    @Override
    @Transactional
    @CacheEvict(value = "buildings", allEntries = true) // 删除后清空所有缓存
    public void deleteBuilding(Long id) {
        buildingMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = "buildings", key = "'building:' + #id") // 缓存单个楼栋信息
    public SysBuilding getBuildingById(Long id) {
        return buildingMapper.selectById(id);
    }

    @Override
    @Cacheable(value = "buildings", key = "'all'") // 缓存所有楼栋列表
    public List<SysBuilding> getAllBuildings() {
        return buildingMapper.selectAll();
    }
}
