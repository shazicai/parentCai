package cn.com.springCai.util;

/**
 * @Author caiJH
 * @Date 2025/3/3 9:56 AM
 * @Version 1.0
 */
public class SnowflakeTest {

    public static void main(String[] args) {
        SnowflakeWorkId snowflake = new SnowflakeWorkId(4,7);
        for (int i = 0; i < 100; i++) {
            System.out.println(snowflake.nextWorkId());
        }
    }
}
