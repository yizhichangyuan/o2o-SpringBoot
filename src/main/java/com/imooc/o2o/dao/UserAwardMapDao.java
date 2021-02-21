package com.imooc.o2o.dao;

import com.imooc.o2o.entity.UserAwardMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserAwardMapDao {
    int insertUserAward(UserAwardMap userAwardMap);

    int deleteUserAward(long userAwardId);

    int updateUserAward(@Param("userId") long userId, @Param("userAwardId") long userAwardId,
                        @Param("lastEditTime") Date lastEditTime, @Param("usedStatus") int usedStatus);

    UserAwardMap queryUserAwardById(long userAwardId);

    List<UserAwardMap> queryUserAwardList(@Param("userAward") UserAwardMap userAwardMap, @Param("rowIndex") int rowIndex,
                                          @Param("pageSize") int pageSize);

    int queryUserAwardCount(@Param("userAward") UserAwardMap userAwardMap);
}
