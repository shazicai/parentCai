package com.spc.springweb.jframe;

import org.springframework.util.ObjectUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * @Author caiJH
 * @Date 2025/12/19 9:15 AM
 * @Version 1.0
 */
public class ClientChat extends JFrame implements ActionListener, KeyListener {
    //文本域
    private JTextArea jTextArea;;
    //滚动条
    private JScrollPane jScrollPane;
    //面板
    private JPanel jPanel;
    //文本框
    private JTextField jTextField;
    //按钮
    private JButton jButton;

    //输出流
    private BufferedWriter writer;

    // 客户端的IP地址
    private static String clientIp;

    // 客户端的端口号
    private static int clientPort;

    static {
        Properties prop = new Properties();
        try {
            prop.load(ServerChat.class.getClassLoader().getResourceAsStream("chat.properties"));
            String clientIpStr = prop.getProperty("clientIp");
            String clientPortStr = prop.getProperty("clientPort");
            if(!ObjectUtils.isEmpty(clientPortStr)){
                clientIp = clientIpStr;
            }else {
                clientIp = "localhost";
            }
            if(!ObjectUtils.isEmpty(clientPortStr)){
                clientPort = Integer.parseInt(clientPortStr);
            }else {
                clientPort = 8080;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientChat() {
        //文本域
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);

        //文本域添加到滚动条中,实现滚动效果
        jScrollPane = new JScrollPane(jTextArea);

        //面板
        jPanel = new JPanel();
        jTextField = new JTextField(10);
        jButton = new JButton("发送");

        //文本与按钮添加到面板中
        jPanel.add(jTextField);
        jPanel.add(jButton);

        //将滚动条与面板全部添加到窗体中
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(jPanel, BorderLayout.SOUTH);

        //设置标题、大小、位置、关闭、是否可见等属性
        this.setTitle("客户端聊天窗口");
        this.setSize(400, 300);
        this.setLocation(200, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //开启线程，监听客户端发送的数据(this当前对象监听)
        jButton.addActionListener(this);
        //回车键发送信息
        jTextField.addKeyListener(this);

        /********** TCP通信 *************/
        try {
            // 创建一个套接字 socket（同时尝试连接服务器）
            Socket socket = new Socket(clientIp, clientPort);

            //获取socket的输入流，读取服务端端发送的数据
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //获取socket的输出流，向服务端发送数据
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //循环读取数据，拼接到文本域中
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                //将读取的数据显示在文本域中
                jTextArea.append(line + System.lineSeparator());
            }

            //关闭socket通道
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        send("发送按钮发送信息");
    }

    //行为

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //回车键按下是发送 (判断e是回车键的时候)
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            send("回车键发送信息");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * 信息发送
     * @param message
     */
    private void send(String message) {
        System.out.println(message);

        //获取文本框中的数据
        String text = jTextField.getText();
        //标记发送用户
        text = "客户端：" + text;
        //将文本框中的数据拼接到文本域中（自己的文本框中显示）
        jTextArea.append(text + System.lineSeparator());

        try {
            //将文本框中的数据发送给客户端
            writer.write(text);
            writer.newLine();
            writer.flush();
            //清空输入文本框
            jTextField.setText("");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
