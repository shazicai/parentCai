package cn.com.springCai.jdk8;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

/**
 * @Author caiJH
 * @Date 2025/6/23 10:04 AM
 * @Version 1.0
 */
public class CollectorsTest {
    public static void main(String[] args) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatSimple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        // 获取当前日期时间
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        LocalDateTime previousYearDateTime = currentDateTime.minus(1, ChronoUnit.MONTHS);
//
//        // 打印结果，注意这里时分秒会保持不变。
//        System.out.println("当前日期时间: " + format.format(currentDateTime));
//        System.out.println("前一年的日期时间（精确到年月日时分秒）:" + format.format(previousYearDateTime));



//        // 获取当前日期
//        LocalDate today = LocalDate.now();
//        // 获取上个月的第一天
//        LocalDateTime localDateTimeStart = today.minusMonths(1).withDayOfMonth(1).atStartOfDay();
//        // 获取上个月最后一天
//        LocalDateTime localDateTimeEnd = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
//        // 打印结果
//        System.out.println("上个月的第一天: " + format.format(localDateTimeStart));
//        System.out.println("上个月的最后一天: " + format.format(localDateTimeEnd));



        Calendar calendar = Calendar.getInstance();
        // 设置日期为上个月
        calendar.add(Calendar.MONTH, -1);
        // 设置日期为上个月的第一天（通常是月初，即1号）
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 设置时间为当天的开始时间（即00:00:00）
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        System.out.println("上个月的第一天: " + formatSimple.format(calendar.getTime()));

        // 获取上个月的第一天，然后设置为下一个月的第一天的前一天，即上个月的最后一天
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        System.out.println("上个月的最后一天: " + formatSimple.format(calendar.getTime()));
    }

}

class CollectorUser implements Serializable {

    private static final long serialVersionUID = -3946644027224491416L;

    private String name;
    private Integer age;
    private String address;
    private String className;


    public CollectorUser() {}

    public CollectorUser(String name, Integer age, String address, String className) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.className = className;
    }
}