package service.fdu_ac_service.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class UtilsHelper {
    //交集(注意结果集中若使用LinkedList添加，则需要判断是否包含该元素，否则其中会包含重复的元素)
    public static Long[] intersect(Long[] arr1, Long[] arr2) {
        List<Long> l = new LinkedList<Long>();
        Set<Long> common = new HashSet<Long>();
        for (Long str : arr1) {
            if (!l.contains(str)) {
                l.add(str);
            }
        }
        for (Long str : arr2) {
            if (l.contains(str)) {
                common.add(str);
            }
        }
        Long[] result = {};
        return common.toArray(result);
    }

    //并集（set唯一性）
    public static Long[] union(Long[] arr1, Long[] arr2) {
        Set<Long> hs = new HashSet<Long>();
        for (Long str : arr1) {
            hs.add(str);
        }
        for (Long str : arr2) {
            hs.add(str);
        }
        Long[] result = {};
        return hs.toArray(result);
    }

    //求两个数组的差集   
    public static Long[] substract(Long[] arr1, Long[] arr2) {
        LinkedList<Long> list = new LinkedList<Long>();
        for (Long str : arr1) {
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        for (Long str : arr2) {
            if (list.contains(str)) {
                list.remove(str);
            }
        }
        Long[] result = {};
        return list.toArray(result);
    }

    //获取系统当前时间
    public static Timestamp getCurrentTime(){
        Date currentDate = new Date();//获得系统时间.
        String currentTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate);//将时间格式转换成符合Timestamp要求的格式.
        Timestamp currentTime = Timestamp.valueOf(currentTimeString);//把时间转换
        return currentTime;
    }

}
