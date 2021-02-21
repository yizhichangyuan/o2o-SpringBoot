package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Award;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwardDao {
    int insertAward(Award award);

    int deleteAward(@Param("awardId") long awardId, @Param("shopId") long shopId);

    int updateAward(Award award);

    List<Award> queryAwardList(@Param("awardCondition") Award award, @Param("rowIndex") int rowIndex,
                               @Param("pageSize") int pageSize);

    Award queryAwardById(long awardId);

    int queryAwardCount(@Param("awardCondition") Award award);
}
