package cn.com.springCai.stream;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @Author caiJH
 * @Date 2025/6/16 3:42 PM
 * @Version 1.0
 */
public class PrintWriterTest {

    public static void main(String[] args) {
        //PrintWriter 输出流
        try (PrintWriter pw = new PrintWriter("/Users/caijinhui/PDF/test.txt")) {
            pw.println("hello world");
            pw.printf("日期:%tF%n",new Date());
            pw.printf("总金额:%,.2f 元%n",1234567.89);
            pw.println("-----------------------");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
