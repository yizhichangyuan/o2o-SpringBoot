package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.UserAwardMap;

public interface UserAwardMapService {
    UserAwardMapExecution queryUserAwardMapList(UserAwardMap userAwardMap, int pageIndex, int pageSize);

    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap);

    UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap);

    UserAwardMap queryUserAwardMapById(Long userAwardId);
}
