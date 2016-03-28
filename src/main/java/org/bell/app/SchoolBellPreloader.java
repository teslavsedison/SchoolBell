package org.bell.app;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * Created by hkn on 28.03.2016.
 */


public class SchoolBellPreloader extends Preloader {
    private static final double WIDTH = 100;
    private static final double HEIGHT = 100;
    Label progress;
    Scene scene;
    private Stage preloaderStage;

    @Override
    public void init() throws Exception {
//        System.out.println(MyApplication.STEP() + "MyPreloader#init (could be used to initialize preloader view), thread: " + Thread.currentThread().getName());

        // If preloader has complex UI it's initialization can be done in MyPreloader#init
        Platform.runLater(() -> {
            Label title = new Label("Yükleniyor lütfen bekleyin.");
            title.setTextAlignment(TextAlignment.CENTER);
            progress = new Label("Yükleniyor...");

            VBox root = new VBox(title, progress);
            root.setAlignment(Pos.CENTER);

            scene = new Scene(root, WIDTH, HEIGHT);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.preloaderStage = primaryStage;

        // Set preloader scene and show stage.
        preloaderStage.setScene(scene);
        preloaderStage.show();
    }
}
