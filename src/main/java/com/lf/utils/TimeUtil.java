package com.lf.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 时间处理类
 */
public class TimeUtil {

    /**
     * LocalDateTime转换为Date
     */
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        //获取默认时区  这查询TimeZone.getDefault()找到默认时区并将其转换为ZoneId
        ZoneId zoneId = ZoneId.systemDefault();
        //将此日期时间与时区相结合以创建 ZonedDateTime 。
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        //从 Instant对象获取一个 Date的实例。
        Date date = Date.from(zdt.toInstant());
        return date;
    }
}
