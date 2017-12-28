package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;


public class loginController {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;

    @FXML protected void handleLoginButtonPress(ActionEvent event) {
        System.out.println("LOGIN PRESSED");
        System.out.println("Username: " + usernameField.getText());
        System.out.println("Password: " + passwordField.getText());
            }
    @FXML protected void handleRegisterButtonPress(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/registerscreen.fxml"));
        } catch (Exception e) {
            System.out.println(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }
    @FXML protected void handleContinueButtonPress(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/mainscreen.fxml"));
        } catch (Exception e) {
            System.out.println(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("APP_NAME");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }
}
