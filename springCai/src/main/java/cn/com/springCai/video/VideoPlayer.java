package cn.com.springCai.video;

import com.alibaba.fastjson2.JSON;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static javafx.application.Application.launch;

/**
 * @Author caiJH
 * @Date 2025/6/4 7:36 PM
 * @Version 1.0
 * 播放视频
 */
public class VideoPlayer extends Application {
    public static void main(String[] args) {
        VideoPlayer player = new VideoPlayer();
        launch();

//        VideoPlayer.PlayerDesktop playerDesktop = player.new PlayerDesktop();
//        playerDesktop.player("/Users/caijinhui/PDF/110.mp4");

//        VideoPlayer.MediaPlayerFX fx = player.new MediaPlayerFX();
//        launch();

//        VideoPlayer.VLCJPlayer vlcjPlayer = player.new VLCJPlayer();
//        vlcjPlayer.player();

        //创建集合
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String videoPath = "/Users/caijinhui/PDF/110.mp4";
        Media media = new Media(new File(videoPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        StackPane root = new StackPane();
        root.getChildren().add(mediaView);
        Scene scene = new Scene(root, 800, 600); // 设置舞台大小
        primaryStage.setTitle("Video Player");
        primaryStage.setScene(scene);
        primaryStage.show();
        mediaPlayer.play(); // 开始播放视频
    }

    /**
     * java内置的Desktop类
     */
    class PlayerDesktop{
        protected void player(String videoPath){
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    File videoFile = new File(videoPath);
                    if (videoFile.exists()) {
                        desktop.open(videoFile);
                    } else {
                        System.out.println("文件不存在: " + videoPath);
                    }
                } catch (IOException e) {
                    System.out.println("无法打开视频文件: " + JSON.toJSONString(e));
                }
            } else {
                System.out.println("当前环境不支持Desktop API");
            }
        }
    }


    /**
     * 使用JavaFX MediaPlayer
     * 如果你需要更复杂的视频播放功能（例如播放控制、视频处理等），你可以使用JavaFX的MediaPlayer
     */
    class MediaPlayerFX extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            String videoPath = "/Users/caijinhui/PDF/110.mp4";
            Media media = new Media(new File(videoPath).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            StackPane root = new StackPane();
            root.getChildren().add(mediaView);
            Scene scene = new Scene(root, 800, 600); // 设置舞台大小
            primaryStage.setTitle("Video Player");
            primaryStage.setScene(scene);
            primaryStage.show();
            mediaPlayer.play(); // 开始播放视频
        }
    }

    /**
     * VLCJ集成方案
     */
    class VLCJPlayer{
        public void player(){
            EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();
            player.mediaPlayer().media().play("/Users/caijinhui/PDF/110.mp4");
        }
    }
}




