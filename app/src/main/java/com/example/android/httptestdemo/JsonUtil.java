package com.example.android.httptestdemo;


import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2015/1/9.
 * 统一的JSON与对象互转帮组类
 */
public class JsonUtil {
    /**
     *
     * 将构造方法私有化，不允许实例化该类
     */
    private JsonUtil() {

    }

    /**
     * 将对象转为JSON
     * @param obj 需要转换成JSON的类
     * @return JSON数据
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * 将JSON字符串转成对象
     * @see {ZJResponse zjResponse = JsonUtil.fromJson(jsonStr, ZJResponse.class);}
     * @param str-要转换的JSON字符串
     * @param   -要转换的对象类型
     * @return 对象
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        try {//扑捉一下  如果返回的json格式是错误的
         T t = gson.fromJson(str, type);
            return  t;

        }catch (Exception e){
            return  null;
        }
    }


}
