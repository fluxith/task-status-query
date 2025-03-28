package com.github.fluxith.taskstatusquery.infrastructure.common.utils;

public class SimpleDBUtil {
    private SimpleDBUtil(){}
    
    private static Long selfIncreasingId = 0L;
    /*
     * 获取自增ID
     */
    public static Long getSelfIncreasingId(){
        return ++selfIncreasingId;
    }
}
