package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.entity.UserShopMap;

public interface UserShopMapService {
    UserShopMapExecution updateUserShopMap(UserShopMap userShopMap);

    UserShopMapExecution addUserShopMap(UserShopMap userShopMap);

    UserShopMapExecution queryUserShopMapList(UserShopMap userShopMap, int pageIndex, int pageSize);

    UserShopMapExecution queryUserShopMapById(long userId, long shopId);
}
