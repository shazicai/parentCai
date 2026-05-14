package cn.com.springCai.video;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

/**
 * @Author caiJH
 * @Date 2025/6/5 10:56 AM
 * @Version 1.0
 *
 * 使用JavaFX MediaPlayer
 * 如果你需要更复杂的视频播放功能（例如播放控制、视频处理等），你可以使用JavaFX的MediaPlayer
 */
public class MediaPlayerFX extends Application {

    public static void main(String[] args) {
        launch(args);
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
}
