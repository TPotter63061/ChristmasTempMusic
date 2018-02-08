package Controller;

import Model.users;
import Model.usersService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.*;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;


public class loginController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    public static users currentAccount = null;

    @FXML
    protected void handleLoginButtonPress(ActionEvent event) throws IOException {
        //gets data from textboxes
        String username = usernameField.getText();
        String password = passwordField.getText();
        System.out.println("USERNAME: " + username);
        System.out.println("PASSWORD: " + password);
        ArrayList<users> list = new ArrayList<>();
        //selects all accounts
        usersService.selectAll(list, loginLaunch.database);
        users account = null;
        for (users p : list) {
            if (username.equals(p.getUsername())) {
                //matching username is set to the account variable
                account = p;
            }
        }
        if (account != null) {
            System.out.println(registerController.generateHash(account.getSalt() + password));
            System.out.println(account.getPassword());
            try {
                //if hashed password in database matches the hash of the users salt and the given password
                if (account.getPassword().equals(registerController.generateHash(account.getSalt() + password))) {
                    System.out.println("Correct Login Details");
                    //currentAccount used for playlists in mainscreen etc
                    currentAccount = account;
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    Parent root;
                    root = FXMLLoader.load(getClass().getResource("/Controller/mainscreen.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("main screen");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                System.out.println("Incorrect Login Details");
            }
        } else {
            //if account = null then no user was found with the same username
            JOptionPane.showMessageDialog(null, "Username does not exist, try creating an account");
        }
    }

    @FXML
    protected void handleRegisterButtonPress(ActionEvent event) {
        //launches the register screen
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

    @FXML
    protected void handleContinueButtonPress(ActionEvent event) {
        //launches mainscreen with no account, playlist cannot be used
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
