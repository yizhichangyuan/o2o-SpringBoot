package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("frontend")
public class ProductDetailController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/getproductdetail", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductDetail(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        try {
            ProductExecution pe = productService.queryProductById(productId);
            // 判断商品详情页中是否请求展示购买二维码，根据用户状态是否登录保证，如果不需要<img>标签隐藏
            PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
            // 用户消费记录里面不需要有购买二维码，因为已经购买了
            int fromUserProduct = HttpServletRequestUtil.getInt(request, "fromUserProduct");
            if(user == null || fromUserProduct == 1){
                modelMap.put("neeQRcode", false);
            }else{
                modelMap.put("needQRcode", true);
            }

            if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                modelMap.put("product", pe.getProduct());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", pe.getStateInfo());
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }
}
