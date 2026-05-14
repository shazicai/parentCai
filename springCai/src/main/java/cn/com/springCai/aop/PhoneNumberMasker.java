package cn.com.springCai.aop;

/**
 * @Author caiJH
 * @Date 2025/10/23 5:04 PM
 * @Version 1.0
 */
public class PhoneNumberMasker {
    public static String maskPhoneNumberWithRegex(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 7) {
            return phoneNumber; // 如果号码太短或者为空，直接返回原号码
        }
        return phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public static void main(String[] args) {
        String phone = "15201607539";
        System.out.println(maskPhoneNumberWithRegex(phone)); // 输出: 138&zwnj;*****&zwnj;5678
    }
}
