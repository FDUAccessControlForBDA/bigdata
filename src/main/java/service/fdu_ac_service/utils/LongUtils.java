package service.fdu_ac_service.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LongUtils {
    //交集(注意结果集中若使用LinkedList添加，则需要判断是否包含该元素，否则其中会包含重复的元素)
    public static Long[] intersect(Long[] arr1, Long[] arr2){
        List<Long> l = new LinkedList<Long>();
        Set<Long> common = new HashSet<Long>();
        for(Long str:arr1){
            if(!l.contains(str)){
                l.add(str);
            }
        }
        for(Long str:arr2){
            if(l.contains(str)){
                common.add(str);
            }
        }
        Long[] result={};
        return common.toArray(result);
    }

    //并集（set唯一性）
    public static Long[] union (Long[] arr1, Long[] arr2){
        Set<Long> hs = new HashSet<Long>();
        for(Long str:arr1){
            hs.add(str);
        }
        for(Long str:arr2){
            hs.add(str);
        }
        Long[] result={};
        return hs.toArray(result);
    }

    //求两个数组的差集   
    public static Long[] substract(Long[] arr1, Long[] arr2) {
        LinkedList<Long> list = new LinkedList<Long>();
        for (Long str : arr1) {
            if(!list.contains(str)) {
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


}
