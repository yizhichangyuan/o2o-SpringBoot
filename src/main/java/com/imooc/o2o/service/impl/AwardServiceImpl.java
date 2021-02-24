package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.AwardDao;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.exceptions.AwardException;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import com.imooc.o2o.util.RowIndexCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.imooc.o2o.service.impl
 * @NAME:AwardServiceImpl
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 12:58
 */
@Service
public class AwardServiceImpl implements AwardService {
    @Autowired
    private AwardDao awardDao;

    @Override
    public AwardExecution queryAwardList(Award award, int pageIndex, int pageSize) {
        if(award.getShopId() == null){
            return new AwardExecution(AwardStateEnum.NULL_SHOP_ID);
        }
        try{
            int rowIndex = RowIndexCalculator.calRowIndex(pageIndex, pageSize);
            List<Award> awardList = awardDao.queryAwardList(award, rowIndex, pageSize);
            int count = awardDao.queryAwardCount(award);
            AwardExecution awardExecution = new AwardExecution(AwardStateEnum.SUCCESS, awardList);
            awardExecution.setCount(count);
            return awardExecution;
        }catch(Exception e){
            throw new AwardException(e.getMessage());
        }
    }

    @Transactional
    public AwardExecution addAward(Award award, ImageHolder imageHolder){
        if(award.getShopId() == null || imageHolder.getImage() == null){
            return new AwardExecution(AwardStateEnum.NULL_AWARD_INFO);
        }
        try{
            award.setEnableStatus(0);
            award.setCreateTime(new Date());
            award.setLastEditTime(new Date());
            int effectNum = awardDao.insertAward(award);
            if(effectNum > -1){
                String targetAddr = saveAwardSimpleImg(award, imageHolder);
                award.setAwardImg(targetAddr);
                effectNum = awardDao.updateAward(award);
                if(effectNum > -1){
                    return new AwardExecution(AwardStateEnum.SUCCESS);
                }
            }
        }catch(Exception e){
            throw new AwardException(e.getMessage());
        }
        return new AwardExecution(AwardStateEnum.INNER_ERROR);
    }

    @Override
    @Transactional
    public AwardExecution modifyAward(Award award, ImageHolder imageHolder) {
        if(award.getAwardId() == null){
            return new AwardExecution(AwardStateEnum.NULL_AWARD_INFO);
        }
        try{
            // 确实不为空，则删除旧的图片
            if(imageHolder != null && imageHolder.getImage() != null) {
                Award temp = awardDao.queryAwardById(award.getAwardId());
                ImageUtil.deleteFileOrPath(temp.getAwardImg());
                String targetAddr = saveAwardSimpleImg(award, imageHolder);
                award.setAwardImg(targetAddr);
            }
            int effectNum = awardDao.updateAward(award);
            if(effectNum > -1){
                return new AwardExecution(AwardStateEnum.SUCCESS);
            }
        }catch(Exception e){
            throw new AwardException(e.getMessage());
        }
        return new AwardExecution(AwardStateEnum.INNER_ERROR);
    }

    public Award queryAwardById(long awardId){
        return awardDao.queryAwardById(awardId);
    }

    private String saveAwardSimpleImg(Award award, ImageHolder holder) throws IOException {
        String relativePath = PathUtil.getAwardSimpleImgPath(award.getShopId(), award.getAwardId());
        String targetAddr = ImageUtil.generateThumbnail(holder, relativePath);
        return targetAddr;
    }
}
