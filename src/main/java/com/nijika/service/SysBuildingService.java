package com.nijika.service;

import com.nijika.entity.SysBuilding;
import java.util.List;

/**
 * 宿舍楼服务接口
 * 功能：宿舍楼信息的增删改查
 */
public interface SysBuildingService {
    void createBuilding(SysBuilding building);

    void updateBuilding(SysBuilding building);

    void deleteBuilding(Long id);

    SysBuilding getBuildingById(Long id);

    List<SysBuilding> getAllBuildings();
}
