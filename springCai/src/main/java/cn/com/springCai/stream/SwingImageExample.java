package cn.com.springCai.stream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
//import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.File;

import static javafx.application.Application.launch;

/**
 * @Author caiJH
 * @Date 2025/6/10 7:47 PM
 * @Version 1.0
 *
 */
public class SwingImageExample extends Application {
    public static void main(String[] args) {
        SwingImageExample example = new SwingImageExample();
        launch();

//        example.showPicture();
//
//        SwingImageExample.SwingDrawImageExample swingDrawImageExample = example.new SwingDrawImageExample();
//        swingDrawImageExample.showPicture();
    }

    /**
     * JavaFX
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            // 读取图片
            Image image = new Image("file:/Users/caijinhui/PDF/111.jpg");

            // 创建一个ImageView来显示图片
            ImageView imageView = new ImageView(image);

            // 创建一个布局并添加ImageView
            StackPane root = new StackPane();
            root.getChildren().add(imageView);

            // 创建一个场景并设置到窗口
            Scene scene = new Scene(root, 500, 500);

            // 设置窗口标题并显示
            primaryStage.setTitle("JavaFX Image Example");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showPicture() {
        // 创建一个JFrame窗口
        JFrame frame = new JFrame("Swing Image Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        try {
            // 读取图片
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("/Users/caijinhui/PDF/111.jpg")));
            // 创建一个JLabel来显示图片
            JLabel label = new JLabel(imageIcon);
            // 将JLabel添加到JFrame中
            frame.add(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 显示窗口
        frame.setVisible(true);
    }

    /**
     * Swing
     */
    class SwingDrawImageExample extends JPanel{
//        private Image image;
        private ImageIcon imageIcon;

        public SwingDrawImageExample() {
            try {
                // 读取图片
//                image = ImageIO.read(new File("/Users/caijinhui/PDF/111.jpg"));
                // 读取图片
                imageIcon = new ImageIcon("/Users/caijinhui/PDF/111.jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void showPicture(){
            JFrame frame = new JFrame("Swing Draw Image Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.add(new SwingDrawImageExample());
            frame.setVisible(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 绘制图片
            g.drawImage(imageIcon.getImage(), 0, 0, this);
        }
    }

}
