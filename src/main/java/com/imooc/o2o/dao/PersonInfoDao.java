package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonInfoDao {
    PersonInfo queryPersonInfoById(long userId);

    int insertPersonInfo(PersonInfo personInfo);
}
