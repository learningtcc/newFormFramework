package com.drore.cloud.util;

import com.drore.cloud.exception.MacroApiException;

import java.util.List;
import java.util.Map;

/**
 * List集合相关工具类
 * Created by 仁杰 on 2017/9/14
 */
public class ListsUtils {

    /**
     * 过滤集合中指定字段重复的数据
     * @param list 过滤的集合
     * @param filedName 过滤的字段
     * @return
     */
    public static List<Map> deleteRepetition(List<Map> list, String filedName){
        if(list != null && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                for(int j = i+1; j < list.size(); j++){
                    if(filedName.length() > 0){
                        String[] newFileName = filedName.split(",");
                        boolean flag = false;
                        int flag2 = 0;
                        for (String name : newFileName){
                            try {
                                if(list.get(i).get(name).equals(list.get(j).get(name))){
                                    flag = true;
                                }else{
                                    flag = false;
                                    flag2++;
                                }
                            } catch (NullPointerException e){
                                throw new MacroApiException("该集合中的第 "+i+" 个Map中不存在字段:"+name+"   或者其Value为null");
                            }
                        }
                        if(flag && flag2 == 0){  //重复数据过滤
                            list.remove(j);
                            j--;
                        }
                    }else throw new MacroApiException("请指定用来过滤重复数据的字段");
                }
            }
            return list;
        }else	throw new MacroApiException("过滤重复数据的集合不能为null");
    }


}
