package com.phoenix;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * LocalDate ： 只含年月日的日期对象
 * LocalTime ：只含时分秒的时间对象
 * LocalDateTime ： 同时含有年月日时分秒的日期对象
 *
 * @Author phoenix
 * @Date 2022/11/24 13:21
 * @Version 1.0.0
 */
public class LocalDataTest {

    public static void main(String[] args) {
        //1.获取当前时间
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        //2.根据指定日期/时间创建对象
        LocalDate day1 = LocalDate.of(2018, 1, 13);
        LocalTime time1 = LocalTime.of(9, 43, 20);
        LocalDateTime dayTime1 = LocalDateTime.of(2018, 1, 13, 9, 43, 20);
        System.out.println(day1);
        System.out.println(time1);
        System.out.println(dayTime1);
        //3.日期时间的加减
        /*
           对于LocalDate,只有精度大于或等于日的加减，如年、月、日；
           对于LocalTime,只有精度小于或等于时的加减，如时、分、秒、纳秒；
           对于LocalDateTime,则可以进行任意精度的时间相加减；
         */
        LocalDateTime plusYearsResult = localDateTime.plusYears(2L);
        LocalDateTime plusMonthsResult = localDateTime.plusMonths(3L);
        LocalDateTime plusDaysResult = localDateTime.plusDays(7L);
        LocalDateTime plusHoursResult = localDateTime.plusHours(2L);
        LocalDateTime plusMinutesResult = localDateTime.plusMinutes(10L);
        LocalDateTime plusSecondsResult = localDateTime.plusSeconds(30L);
        System.out.println("当前的时间是： " + localDateTime + "\n"
                + "当前时间加2年后为： " + plusYearsResult + "\n"
                + "当前时间加3个月后为：" + plusMonthsResult + "\n"
                + "当前时间加7日后为：" + plusDaysResult + "\n"
                + "当前时间加2小时后为：" + plusHoursResult + "\n"
                + "当前时间加10分钟后为: " + plusMinutesResult + "\n"
                + "当前时间加30秒后为：" + plusSecondsResult + "\n"
        );
        LocalDateTime nextYear = localDateTime.plus(1, ChronoUnit.YEARS);
        LocalDateTime nextMonth = localDateTime.plus(1, ChronoUnit.MONTHS);
        LocalDateTime nextDay = localDateTime.plus(1, ChronoUnit.DAYS);
        System.out.println("当前的时间是： " + localDateTime + "\n"
                + "nextYear： " + nextYear + "\n"
                + "nextMonth：" + nextMonth + "\n"
                + "nextDay：" + nextDay + "\n"
        );

        //4.将年、月、日等修改为指定的值，并返回新的日期（时间）对象
        //当前时间基础上，指定本年中的第几天，取值范围为1-365,366
        LocalDateTime withDayOfYearResult = localDateTime.withDayOfYear(200);
        //当前时间基础上，指定本月当中的第几天，取值范围为1-29,30,31
        LocalDateTime withDayOfMonthResult = localDateTime.withDayOfMonth(5);
        //当前时间基础上，直接指定年份
        LocalDateTime withYearResult = localDateTime.withYear(2017);
        //当前时间基础上，直接指定月份
        LocalDateTime withMonthResult = localDateTime.withMonth(5);
        System.out.println("当前的时间是： " + localDateTime + "\n"
                + "指定本年中的第200天： " + withDayOfYearResult + "\n"
                + "指定本月当中的第5天：" + withDayOfMonthResult + "\n"
                + "直接指定年份为2017：" + withYearResult + "\n"
                + "直接指定月份为5：" + withMonthResult + "\n"
        );

        //5.获取日期的年月日周时分秒
        int dayOfYear = localDateTime.getDayOfYear();
        int dayOfMonth = localDateTime.getDayOfMonth();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        System.out.println("今天是" + localDateTime + "\n"
                + "本年当中的第" + dayOfYear + "天" + "\n"
                + "本月当中的第" + dayOfMonth + "天" + "\n"
                + "本周中星期" + dayOfWeek.getValue() + "--即 " + dayOfWeek + "\n");

        int year = localDateTime.getYear();
        Month month = localDateTime.getMonth();
        int day = localDateTime.getDayOfMonth();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int second = localDateTime.getSecond();
        System.out.println("今天是 " + localDateTime + "\n"
                + "年： " + year + "\n"
                + "月：" + month.getValue() + "--即 " + month + "\n"
                + "日： " + day + "\n"
                + "时: " + hour + "\n"
                + "分：" + minute + "\n"
                + "秒：" + second + "\n"
        );

        //6.时间日期前后的比较与判断
        LocalDate localDate1 = LocalDate.of(2017, 8, 8);
        LocalDate localDate2 = LocalDate.of(2018, 8, 8);
        boolean date1IsBeforeDate2 = localDate1.isBefore(localDate2);
        System.out.println("date1IsBeforeDate2: " + date1IsBeforeDate2);

        //7.时间戳
        Instant instant = Instant.now();
        System.out.println(instant);
        Date date7 = Date.from(instant);
        System.out.println(date7);
        Instant instant2 = date7.toInstant();
        System.out.println(instant2);

        //8.计算时间、日期间隔
        //计算两个日期的日期间隔-年月日
        LocalDate date1 = LocalDate.of(2018, 2, 13);
        LocalDate date2 = LocalDate.of(2017, 3, 12);
        //内部是用date2-date1，所以得到的结果是负数
        Period period = Period.between(date1, date2);
        System.out.println("相差年数 ： " + period.getYears());
        System.out.println("相差月数 ： " + period.getMonths());
        System.out.println("相差日数 ： " + period.getDays());
        //还可以这样获取相差的年月日
        System.out.println("-------------------------------");
        long years = period.get(ChronoUnit.YEARS);
        long months = period.get(ChronoUnit.MONTHS);
        long days = period.get(ChronoUnit.DAYS);
        System.out.println("相差的年月日分别为 ： " + years + "," + months + "," + days);
        //注意，当获取两个日期的间隔时，并不是单纯的年月日对应的数字相加减，而是会先算出具体差多少天，在折算成相差几年几月几日的

        //计算两个时间的间隔
        System.out.println("-------------------------------");
        LocalDateTime date3 = LocalDateTime.now();
        LocalDateTime date4 = LocalDateTime.of(2018, 1, 13, 22, 30, 10);
        Duration duration = Duration.between(date3, date4);
        System.out.println(date3 + " 与 " + date4 + " 间隔  " + "\n"
                + " 天 :" + duration.toDays() + "\n"
                + " 时 :" + duration.toHours() + "\n"
                + " 分 :" + duration.toMinutes() + "\n"
                + " 毫秒 :" + duration.toMillis() + "\n"
                + " 纳秒 :" + duration.toNanos() + "\n"
        );
        //注意，并没有获得秒差的，但既然可以获得毫秒，秒就可以自行获取了

        //9.计算程序的运行时间，应当使用时间戳Instant
        Instant instant3 = Instant.now();
        for (int i = 0; i < 10000000; i++) {
            //
            int b = i * 10;
        }
        Instant instant4 = Instant.now();
        Duration duration2 = Duration.between(instant3, instant4);
        System.out.println("本次循环运行耗时为： " + duration2.toMillis() + "毫秒");

        //10.使用自定义格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        String dateToString = dateTimeFormatter.format(localDateTime);
        System.out.println(dateToString);

        /*
            自定义转化的格式一定要与日期类型对应
            LocalDate只能设置仅含年月日的格式
            LocalTime只能设置仅含时分秒的格式
            LocalDateTime可以设置含年月日时分秒的格式
         */
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(dateTimeFormatter2.format(LocalDate.now()));
        DateTimeFormatter dateTimeFormatter3 = DateTimeFormatter.ofPattern("HH:mm:ss 哈哈");
        System.out.println(dateTimeFormatter3.format(LocalTime.now()));

        //11.将时间字符串形式转化为日期对象
        String dateTimeString = "2018-01-13 21:23:30";
        DateTimeFormatter dateTimeFormatter4 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTimeFromString = LocalDateTime.parse(dateTimeString, dateTimeFormatter4);
        System.out.println(localDateTimeFromString);

        //12.将时间日期对象转为格式化后的时间日期对象
        DateTimeFormatter dateTimeFormatter5 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String format2 = dateTimeFormatter5.format(localDateTime);
        LocalDateTime newDateTime = LocalDateTime.parse(format2, dateTimeFormatter5);
        System.out.println(newDateTime);

        //13.long毫秒值转换为日期
        System.out.println("-------------long毫秒值转换为日期------------");
        String format4 = dateTimeFormatter4.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.of("Asia/Shanghai")));
        System.out.printf(format4);
    }
}
