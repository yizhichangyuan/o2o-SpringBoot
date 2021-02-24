package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.enums.UserProductMapStateEnum;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName:com.imooc.o2o.web.frontend
 * @NAME:UserProductController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/23 17:29
 */
@RestController
@RequestMapping(value="/frontend")
public class UserProductController {
    @Autowired
    private UserProductMapService userProductMapService;

    @RequestMapping(value="listuserproductbyuser")
    public Map<Object, Object> listUserProduct(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        String productName = HttpServletRequestUtil.getString(request, "productName");
        if(pageIndex > -1 && pageIndex > -1){
            if(user == null){
                modelMap.put("success", false);
                modelMap.put("errMsg", "未登录");
            }else{
                UserProductMap userProductMap = new UserProductMap();
                userProductMap.setUser(user);
                if(productName != null){
                    Product product = new Product();
                    product.setProductName(productName);
                    userProductMap.setProduct(product);
                }
                try{
                    UserProductMapExecution userProductMapExecution =
                            userProductMapService.queryUserProductMapList(userProductMap, pageIndex, pageSize);
                    if(userProductMapExecution.getState() == UserProductMapStateEnum.SUCCESS.getState()){
                        modelMap.put("success", true);
                        modelMap.put("userProductMapList", userProductMapExecution.getList());
                        modelMap.put("count", userProductMapExecution.getCount());
                    }else{
                        modelMap.put("success", false);
                        modelMap.put("errMsg", userProductMapExecution.getStateInfo());
                    }
                }catch (Exception e){
                    modelMap.put("success", false);
                    modelMap.put("errMsg", e.getMessage());
                }
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageIndex or pageSize");
        }
        return modelMap;
    }
}
