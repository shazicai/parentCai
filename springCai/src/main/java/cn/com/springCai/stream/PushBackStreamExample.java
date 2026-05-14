package cn.com.springCai.stream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author caiJH
 * @Date 2025/6/13 11:03 AM
 * @Version 1.0
 */
public class PushBackStreamExample {

    public static void main(String[] args) {
        // 回退流示例
        try (PushbackInputStream pbis = new PushbackInputStream(new FileInputStream("/Users/caijinhui/PDF/test.txt"), 8)) {
            byte [] buf = new byte[8];
            int data;
            while ((data = pbis.read(buf)) != -1) {
                String str = new String(buf, 0, data, StandardCharsets.UTF_8);
//                if (str.contains("#")) {
//                    System.out.println("\n#数据放回流中");
////                    pbis.unread(data);
////                    Thread.sleep(2000);
////                    pbis.skip(skipData);
//                } else {
                    // 将数据推回流中
                    System.out.printf("%s\n", str);
//                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
