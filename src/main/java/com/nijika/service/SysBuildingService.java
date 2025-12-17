package com.nijika.service;

import com.nijika.entity.SysBuilding;
import java.util.List;

public interface SysBuildingService {
    void createBuilding(SysBuilding building);

    void updateBuilding(SysBuilding building);

    void deleteBuilding(Long id);

    SysBuilding getBuildingById(Long id);

    List<SysBuilding> getAllBuildings();
}
