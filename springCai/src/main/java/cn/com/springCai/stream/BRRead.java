package cn.com.springCai.stream;

import java.io.*;

/**
 * @Author caiJH
 * @Date 2025/6/4 4:29 PM
 * @Version 1.0
 */
public class BRRead implements Serializable {

    public static void main(String[] args) throws IOException {
        BRRead readWrite = new BRRead();
//        readWrite.bufferReaderTest();

//        readWrite.bufferWriterTest();
        readWrite.inputStreamTest();
    }

    public void bufferReaderTest() throws IOException {
        char r ;
        String s;
        // 字符 输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            try {
                // 读取数据 字符
                r = (char)br.read();
                // 读取数据 字符串
//                s = br.readLine();
                System.out.printf("输入数据:%s\n",r);
//                System.out.printf("输入数据:%s\n",s);
                System.out.write(r);
                System.out.write('\n');
            } catch (IOException e) {
                System.out.println("读取失败!");
                br.close();
                throw new RuntimeException(e);
            }
        }while (r != 'q');
        br.close();
    }

    public void bufferWriterTest(){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("/Users/caijinhui/PDF/test.txt"))));
            writer.write("caijinhui");
            writer.write(101);
            writer.flush();
            System.out.printf("写入成功!\n");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输入流（输入到内存中）-- 字节
     */
    public void inputStreamTest(){
        try {
            InputStream stream = new FileInputStream("/Users/caijinhui/PDF/test.txt");
//            int available = stream.available();

            byte[] buffer = new byte[16];
            int read;

            //读取所有字节到字节数组
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((read = stream.read(buffer)) != -1){
                System.out.printf("读取数据:%s\n",new String(buffer,0,read));
                byteArrayOutputStream.write(buffer,0,read);
            }
            System.out.printf("--------数据读取完--------\n");
            // 转换为字节数组
            byte[] fileBytes = byteArrayOutputStream.toByteArray();

            // 关闭流
            stream.close();
            byteArrayOutputStream.close();
            // 转换为字符串
            String content = new String(fileBytes,"UTF-8");
            System.out.printf("读取数据content:%s\n",content);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
