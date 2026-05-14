package cn.com.springCai.stream;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author caiJH
 * @Date 2025/6/10 5:16 PM
 * @Version 1.0
 */
public class ReadNetworkDataExample {
    public static void main(String[] args) {
        // 读取网络数据
        try {
            //创建URL对象
            URL url = new URL("https://www.iana.org/help/example-domains");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();

            // 使用 BufferedReader 读取输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // 读取并输出数据
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            // 关闭资源
            reader.close();
            connection.disconnect();
            // 输出读取到的内容
            System.out.println("读取到的内容:");
            System.out.println(response.toString());
        }catch (Exception e){
            System.err.println("读取网络数据时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
