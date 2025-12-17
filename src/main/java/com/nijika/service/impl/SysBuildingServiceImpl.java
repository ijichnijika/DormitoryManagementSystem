package com.nijika.service.impl;

import com.nijika.entity.SysBuilding;
import com.nijika.mapper.SysBuildingMapper;
import com.nijika.service.SysBuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysBuildingServiceImpl implements SysBuildingService {

    private final SysBuildingMapper buildingMapper;

    @Override
    @Transactional
    public void createBuilding(SysBuilding building) {
        buildingMapper.insert(building);
    }

    @Override
    @Transactional
    public void updateBuilding(SysBuilding building) {
        buildingMapper.updateById(building);
    }

    @Override
    @Transactional
    public void deleteBuilding(Long id) {
        buildingMapper.deleteById(id);
    }

    @Override
    public SysBuilding getBuildingById(Long id) {
        return buildingMapper.selectById(id);
    }

    @Override
    public List<SysBuilding> getAllBuildings() {
        return buildingMapper.selectAll();
    }
}
