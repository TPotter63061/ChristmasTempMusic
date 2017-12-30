package Controller;

import Model.DatabaseConnection;
import Model.users;
import Model.usersService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;

import java.util.ArrayList;


public class loginController {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    public static DatabaseConnection database;
    public users currentAccount = null;

    @FXML protected void handleLoginButtonPress(ActionEvent event) {
        String username = usernameField.getText();
        String password = usernameField.getText();
        ArrayList<users> list = new ArrayList<>();
        database = new DatabaseConnection("musicPlayer.db");
        usersService.selectAll(list, database);
        users account = null;
        for(users p : list){
            if(username.equals(p.getUsername())){
                account = p;
            }
        }
        if(account != null) {
            System.out.println(registerController.generateHash(account.getSalt() + password));
            System.out.println(account.getPassword());
            try {
                if (account.getPassword().equals(registerController.generateHash(account.getSalt() + password))) {
                    System.out.println("Correct Login Details");
                    currentAccount = account;
                    loadMainScreen(event);
                }
            } catch (Exception e) {
                System.out.println("Incorrect Login Details");
            }
        }else{
            System.out.println("Username does not exist, try creating an account");
        }
    }
    @FXML protected void handleRegisterButtonPress(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Controller/registerscreen.fxml"));
        } catch (Exception e) {
            System.out.println(e);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    @FXML protected void handleContinueButtonPress(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Controller/mainscreen.fxml"));
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
    private void loadMainScreen(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Controller/mainscreen.fxml"));
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
