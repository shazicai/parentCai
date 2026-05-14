package cn.com.springCai.consumer;

import cn.com.springCai.consumer.entity.ErpTrainPersonInfo;
import cn.com.springCai.consumer.entity.User;
import cn.com.springCai.consumer.entity.UserStr;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author caiJH
 * @Date 2025/6/27 3:50 PM
 * @Version 1.0
 */
public class UserToUserStr {

    private static List<User> userList = new ArrayList<>();

    private List<UserStr> userStrList = new ArrayList<>();

    // 转换函数：将用户名转为大写
    Function<User, UserStr> userStrFunction = user -> {
        UserStr userStr = new UserStr();
        if(user.getIntName().equals(1)){
            userStr.setName("蔡进辉");
        }
        if(user.getIntName().equals(2)){
            userStr.setName("蔡赛燕");
        }
        if(user.getIntClass().equals(1)){
            userStr.setClassName("一班");
        }
        if(user.getIntClass().equals(2)){
            userStr.setClassName("二班");
        }
        return userStr;
    };

    static {
        userList = Arrays.asList(new User(1,1),new User(2,2));
    }


    public static void main(String[] args) {

        UserToUserStr userToUserStr = new UserToUserStr();
//        userToUserStr.userToUserStr();

        userToUserStr.getObjectSize();
    }

    public void userToUserStr(){

        List<UserStr> strList = userList.stream()
                .map(userStrFunction)
                .collect(Collectors.toList());

        System.out.println(strList);
    }

    public void getObjectSize(){
        // 查看JVM详细信息
        System.out.println(VM.current().details());

        // 分析对象布局
        ErpTrainPersonInfo obj = new ErpTrainPersonInfo("测试","NP1123s1","蔡进辉","三国保险","职位","部门","身份证","男","银行卡","15201987362","NO12332123123");
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());

        // 获取对象总大小
        long size = ClassLayout.parseInstance(obj).instanceSize();
        System.out.println("Total size: " + size + " bytes");

        // 分析对象图大小
        List<String> list = Arrays.asList("One", "Two", "Three");
        System.out.println("List with elements size: " +
                GraphLayout.parseInstance(list).totalSize() + " bytes");
    }
}
