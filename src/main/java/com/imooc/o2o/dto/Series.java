package com.imooc.o2o.dto;

import java.util.List;

/**
 * @PackageName:com.imooc.o2o.dto
 * @NAME:Series
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 20:09
 */
public class Series {
    private String name;
    private String type = "bar";
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
