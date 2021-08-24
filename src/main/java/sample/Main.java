package sample;

import helpers.HTTPRequest.DefaultRequests;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        DefaultRequests.setConnect("http://192.168.1.9:8081");
        Parent root = FXMLLoader.load(getClass().getResource("/LoginLogoutScreen.fxml"));
        primaryStage.setTitle("LOGIN - GRAMARI");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
