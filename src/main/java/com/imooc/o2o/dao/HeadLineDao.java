package com.imooc.o2o.dao;

import com.imooc.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeadLineDao {
    /**
     * 根据headLine条件查询头条，例如enableStatus
     *
     * @param headLine
     * @return
     */
    List<HeadLine> queryHeadLineList(@Param("headLineCondition") HeadLine headLine);
}
