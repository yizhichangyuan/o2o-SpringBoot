package com.imooc.o2o.dto;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @PackageName:com.imooc.o2o.dto
 * @NAME:XSeries
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 20:15
 */
public class XSeries {
    private String type = "category";
    private Set<String> data = new LinkedHashSet<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<String> getData() {
        return data;
    }

    public void setData(Set<String> data) {
        this.data = data;
    }
}
