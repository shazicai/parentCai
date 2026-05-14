package cn.com.springCai.stream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;
import java.net.Socket;

/**
 * @Author caiJH
 * @Date 2025/6/17 8:31 AM
 * @Version 1.0
 * 高级 使用高效序列化库
 */
public class KryoTest {

    public static void main(String[] args) {
//        Socket socket = null;
//        try {
//            //创建Socket连接
//            socket = new Socket("https://www.baidu.com",80);
//            Kryo kryo = new Kryo();
//            Output output = new Output(new BufferedOutputStream(socket.getOutputStream()));
//            kryo.writeObject(output, new Object, Serializable);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        KryoTest.kryoPersson();
    }

    public static void kryoPersson(){
        Kryo kryo = new Kryo();
        //需要预先注册序列化的类
        kryo.register(cn.com.springCai.stream.Person.class);
        try{
//            Output output = new Output(new FileOutputStream("/Users/caijinhui/PDF/person.txt"));
//            Person personI = new Person("张三", 18);
//            kryo.writeObject(output,personI);
////            output.flush();
//            output.close();

            Input input = new Input(new FileInputStream("/Users/caijinhui/PDF/person.txt"));
            Person personO = kryo.readObject(input,Person.class);
            System.out.printf(personO.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
