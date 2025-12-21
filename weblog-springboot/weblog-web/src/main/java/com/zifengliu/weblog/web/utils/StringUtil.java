package com.zifengliu.weblog.web.utils;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/21 下午9:36
 * @description
 **/

public class StringUtil {

    /**
     * 判断字符串是否是纯数字
     * @param str
     * @return
     */
    public static boolean isPureNumber(String str) {
        return str.matches("\\d+");
    }

}

