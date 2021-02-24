package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.UserProductMap;

/**
 * @PackageName:com.imooc.o2o.service
 * @NAME:UserProductMapService
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 15:20
 */
public interface UserProductMapService {
    UserProductMapExecution queryUserProductMapList(UserProductMap condition, int pageIndex, int pageSize);

    UserProductMapExecution addUserProductMap(UserProductMap userProductMap);
}
