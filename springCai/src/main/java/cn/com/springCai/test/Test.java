package cn.com.springCai.test;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.sound.midi.Soundbank;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author caiJH
 * @Date 2025/3/27 8:42 AM
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
//        List<String> list = Arrays.asList("mmc","tmc","345");
//        System.out.println(StringUtils.join(list, ","));
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String s = "1";
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        cal.add(Calendar.HOUR, -Integer.parseInt(s));
//        System.out.println(format.format(cal.getTime()));

//        Test.test();
//        Test test = new Test();


//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH,1);
//        calendar.add(Calendar.DATE,-1);
//        System.out.println("-----" + format.format(new Date(1761900693587L)));

//        String str = "[{\"activationDate\":1761609600000,\"applicableToThree\":0,\"applicableToTwoThree\":0,\"billServiceFee\":33.66,\"companyId\":3,\"companyName\":\"易行商旅有限公司（三级）\",\"createTime\":1760693518000,\"feeType\":1,\"firstOrgId\":1,\"firstOrgName\":\"国网易行测试公司\",\"hotelType\":0,\"id\":130,\"refundServiceFee\":0.00,\"secondaryOrgId\":1,\"secondaryOrgName\":\"易行有限公司（二级）\",\"serviceFee\":0.00,\"updateTime\":1760693518000,\"userId\":471,\"userName\":\"plane\"},{\"activationDate\":1759795200000,\"applicableToThree\":0,\"applicableToTwoThree\":1,\"createTime\":1760693494000,\"feeType\":0,\"firstOrgId\":1,\"firstOrgName\":\"国网易行测试公司\",\"hotelType\":0,\"id\":129,\"orderType\":1,\"refundServiceFee\":34.77,\"serviceFee\":22.00,\"updateTime\":1760693494000,\"userId\":471,\"userName\":\"plane\"},{\"activationDate\":1760745600000,\"applicableToThree\":0,\"applicableToTwoThree\":0,\"billServiceFee\":66.00,\"companyId\":1,\"companyName\":\"易行有限公司（二级）\",\"createTime\":1760692143000,\"feeType\":1,\"firstOrgId\":1,\"firstOrgName\":\"国网易行测试公司\",\"hotelType\":0,\"id\":125,\"refundServiceFee\":0.00,\"secondaryOrgId\":1,\"secondaryOrgName\":\"易行有限公司（二级）\",\"serviceFee\":0.00,\"updateTime\":1760692143000,\"userId\":1,\"userName\":\"admin超管\"},{\"activationDate\":1760572800000,\"applicableToThree\":0,\"applicableToTwoThree\":0,\"companyId\":1,\"companyName\":\"易行有限公司（二级）\",\"createTime\":1760691823000,\"feeType\":0,\"firstOrgId\":1,\"firstOrgName\":\"国网易行测试公司\",\"hotelType\":0,\"id\":124,\"orderType\":1,\"refundServiceFee\":66.00,\"secondaryOrgId\":1,\"secondaryOrgName\":\"易行有限公司（二级）\",\"serviceFee\":55.00,\"updateTime\":1760691823000,\"userId\":1,\"userName\":\"admin超管\"},{\"activationDate\":1760745600000,\"applicableToThree\":0,\"applicableToTwoThree\":0,\"billServiceFee\":55.00,\"companyId\":1,\"companyName\":\"易行有限公司（二级）\",\"createTime\":1760691570000,\"feeType\":1,\"firstOrgId\":1,\"firstOrgName\":\"国网易行测试公司\",\"hotelType\":0,\"id\":123,\"refundServiceFee\":0.00,\"secondaryOrgId\":1,\"secondaryOrgName\":\"易行有限公司（二级）\",\"updateTime\":1760691570000,\"userId\":1,\"userName\":\"admin超管\"}]";
//        CompanyServiceFeeVo vo = new CompanyServiceFeeVo();
//        List<CompanyServiceFee> listN = JSON.parseArray(str,CompanyServiceFee.class);
//        listN.stream().sorted(Comparator.comparing(CompanyServiceFee::getCreateTime).reversed())
//                .findFirst().ifPresent(latestData -> BeanUtils.copyProperties(latestData, vo));
//        System.out.println(vo);

//        List<Integer> array = Arrays.asList(8, 2, 3, 80, 5, 65, 9, 7, 1, 6, 4, 11);
//        Integer max = Test.getMax(array, 0, array.size() - 1);
//        System.out.println(max);

//        String str = "invoiceDescription,\n" +
//                "\t\tproductType,\n" +
//                "\t\tfirstId,\n" +
//                "\t\tfirstName,\n" +
//                "\t\tsecondaryId,\n" +
//                "\t\tsecondaryName,\n" +
//                "\t\tinvoiceSerialNum";
//        String[] split = str.split(",");
//        String mm = "<if test=\"@ != null and @ != ''\">\n" +
//                "\t\t\t\t#{@,jdbcType=VARCHAR },\n" +
//                "\t\t\t</if>";
//        StringBuilder builder = new StringBuilder();
//        for (String s : split) {
//            String trim = s.trim();
//            String s1 = mm.replaceAll("@", trim);
//            builder.append(s1);
//            builder.append("\n");
//        }
//        System.out.println(builder.toString());



        String str = "\"paymentDays\\\":\\\"2026-01\\\",\\\"profitCenter\\\":\\\"准能公司\\\",\\\"profitCenterNo\\\":\\\"PM20009000\\\",\\\"day\\\":1,\\\"operatorName\\\":\\\"mmc\\\"}";
        System.out.println(str.replaceAll("\\\\", ""));
    }

    public static Integer getMax(List<Integer> list, int left, int right) {
        int max_left = 0, max_right = 0, middle = 0;
        // 集合空
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        //如果查找范围中仅有一个数字
        if(right - left == 0){
            return list.get(left);
        }
        //如果查找范围中有 2 个数字，直接比较即可
        if(right - left <= 1){
            if(list.get(left) >= list.get(right)){
                return list.get(left);
            }else {
                return list.get(right);
            }
        }
        //等量划分成 2 个区域
        middle = (right - left) / 2 + left;
        System.out.printf("middle:%d, left:%d, right:%d\n", middle, left, right);
        //得到左侧区域中的最大值
        max_left = getMax(list, left, middle);
        //得到右侧区域中的最大值
        max_right = getMax(list, middle + 1, right);
        //比较左、右两侧的最大值，找到 [left,right] 整个区域的最大值
        if (max_left >= max_right) {
            return  max_left;
        }
        else {
            return max_right;
        }
    }


    public static void strSimpleFormat(){
        String str = "{\\\"list\\\":[{\\\"add1\\\":\\\"\\\",\\\"add2\\\":\\\"\\\",\\\"add3\\\":\\\"\\\",\\\"add4\\\":\\\"\\\",\\\"add5\\\":\\\"\\\",\\\"costCenter\\\":\\\"\\\",\\\"createVoucherNo\\\":\\\"2025-10-28 17:08:58\\\",\\\"writeOffNo\\\":\\\"\\\"}]}";
        System.out.println(str.replaceAll("\\\\", ""));

        BigDecimal num = new BigDecimal(12.3);
        BigDecimal nums = new BigDecimal(-12.3);
        System.out.println(num.setScale(0, RoundingMode.UP));
        System.out.println(nums.setScale(0, RoundingMode.UP));
    }

    public static void test(){
        int[] nums = {-3,4,3,90};
        int target = 0;
        int[] ints = Test.twoSum(nums, target);
        System.out.println(ints[0] + "," + ints[1]);
    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> hashMap = new HashMap<>(nums.length - 1);
        hashMap.put(nums[0],0);
        for(int i = 1; i < nums.length; i++){
            if(hashMap.containsKey(target - nums[i])){
                return new int[]{hashMap.get(target - nums[i]),i};
            }
            hashMap.put(nums[i],i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public void TestHashMap(){
        Map map = new HashMap();
        map.put("1","1");
    }
}
