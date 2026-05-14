package cn.com.springCai.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author caiJH
 * @Date 2025/6/4 2:52 PM
 * @Version 1.0
 * stream流的使用步骤 (集合、数组) -> 过滤 -> 排序 -> 去重复 -> 合并
 */
public class StreamTest {
    public static void main(String[] args) {
        List<String> names = new ArrayList<String>();
        Collections.addAll(names,"张三丰","张无忌","周芷若","赵敏","张山");
        StreamTest.streamMethod(names);
    }
    public static void streamMethod(List<String> names){
        String str = "赵云";
        boolean bool = str.startsWith("赵");
        List<String> listZ = names.stream().filter(s -> s.startsWith("张")).filter(e -> e.length() == 3).collect(Collectors.toList());
        listZ.forEach(e -> System.out.printf("返回信息:%s\n",e));
    }

    public static void streamMap(){

    }

}
