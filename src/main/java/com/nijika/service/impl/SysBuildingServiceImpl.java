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

@Service
@RequiredArgsConstructor
public class SysBuildingServiceImpl implements SysBuildingService {

    private final SysBuildingMapper buildingMapper;

    @Override
    @Transactional
    @CacheEvict(value = "buildings", allEntries = true)
    public void createBuilding(SysBuilding building) {
        buildingMapper.insert(building);
    }

    @Override
    @Transactional
    @CacheEvict(value = "buildings", allEntries = true)
    public void updateBuilding(SysBuilding building) {
        buildingMapper.updateById(building);
    }

    @Override
    @Transactional
    @CacheEvict(value = "buildings", allEntries = true)
    public void deleteBuilding(Long id) {
        buildingMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = "buildings", key = "'building:' + #id")
    public SysBuilding getBuildingById(Long id) {
        return buildingMapper.selectById(id);
    }

    @Override
    @Cacheable(value = "buildings", key = "'all'")
    public List<SysBuilding> getAllBuildings() {
        return buildingMapper.selectAll();
    }
}
