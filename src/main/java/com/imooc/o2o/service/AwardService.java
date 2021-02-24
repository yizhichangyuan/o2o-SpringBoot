package com.imooc.o2o.service;

import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;

/**
 * @PackageName:com.imooc.o2o.service
 * @NAME:AwardService
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 12:50
 */
public interface AwardService {
    AwardExecution queryAwardList(Award award, int pageIndex, int pageSize);

    AwardExecution addAward(Award award, ImageHolder imageHolder);

    AwardExecution modifyAward(Award award, ImageHolder imageHolder);

    Award queryAwardById(long awardId);
}
