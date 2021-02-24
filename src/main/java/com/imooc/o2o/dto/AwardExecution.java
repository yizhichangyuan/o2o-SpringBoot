package com.imooc.o2o.dto;

import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.enums.AwardStateEnum;

import java.util.List;

/**
 * @PackageName:com.imooc.o2o.dto
 * @NAME:AwardExecution
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 12:54
 */
public class AwardExecution {
    private int count;
    private Award award;
    private List<Award> awardList;
    private String stateInfo;
    private int state;

    public AwardExecution() {
    }

    public AwardExecution(AwardStateEnum awardStateEnum) {
        this.state = awardStateEnum.getState();
        this.stateInfo = awardStateEnum.getStateInfo();
    }

    public AwardExecution(AwardStateEnum awardStateEnum, Award award) {
        this.state = awardStateEnum.getState();
        this.stateInfo = awardStateEnum.getStateInfo();
        this.award = award;
    }

    public AwardExecution(AwardStateEnum awardStateEnum, List<Award> awardList) {
        this.state = awardStateEnum.getState();
        this.stateInfo = awardStateEnum.getStateInfo();
        this.awardList = awardList;
        this.count = awardList.size();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public List<Award> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<Award> awardList) {
        this.awardList = awardList;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
