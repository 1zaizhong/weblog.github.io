package com.zifengliu.weblog.common.constant;

import java.time.format.DateTimeFormatter;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/13 下午10:23
 * @description
 **/

public interface Constants {
    /**
     * 月-日 格式
     */
    DateTimeFormatter MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");
    /**
     * 年-月-日 小时-分钟-秒
     */
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
