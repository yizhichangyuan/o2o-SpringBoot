package com.imooc.o2o.util;

public class RowIndexCalculator {
    public static int calRowIndex(int pageIndex, int pageSize) {
        return pageIndex > 0 ? (pageIndex - 1) * pageSize : 0;
    }
}
