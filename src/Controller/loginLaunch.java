package Controller;

import Model.DatabaseConnection;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.application.Application;

public class loginLaunch extends Application{
    public static DatabaseConnection database;
    @Override
    public void start(Stage stage) throws Exception {
        //this will be accessed throughout the application
        database = new DatabaseConnection("musicPlayer.db");
        Parent root = FXMLLoader.load(getClass().getResource("loginscreen.fxml"));
        Scene scene = new Scene(root, 600, 332);
        stage.setTitle("LOGIN");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> System.exit(0));

        stage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}

