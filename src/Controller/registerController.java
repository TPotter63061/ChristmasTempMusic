package Controller;

import Model.users;
import Model.usersService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class registerController {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField1;
    @FXML private TextField passwordField2;

    @FXML protected void handleBackButtonPress(ActionEvent event){
        loadLoginScreen(event);
    }
    @FXML protected void handleRegisterButtonPress(ActionEvent event) {
        String password1 = passwordField1.getText();
        String password2 = passwordField2.getText();
        String username = usernameField.getText();
        String salt = generateSalt();
        System.out.println(salt);
        if(password2.equals(password2)){
            System.out.println("passwords match");
            if(checkUsername(username) == false){
                System.out.println("username available");
                createAccount(event, username, password1, salt);
            }else{
                System.out.println("Username taken");
            }

        }else{
            System.out.println("passwords do not match");
        }
    }
    private String generateSalt(){
        String charList = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<10; i++){
            int number = getRandomNumber();
            char ch = charList.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }
    private int getRandomNumber(){
        Random rand = new Random();
        return(rand.nextInt(62));
    }


    private void createAccount(ActionEvent event, String username, String password, String salt){
        String hashedPass = generateHash(salt + password);
        System.out.println("password hashed");
        users p = new users(0, username, hashedPass,salt);
        usersService.save(p, loginLaunch.database);
        System.out.println("save successful");
        loadLoginScreen(event);
    }

    public static String generateHash(String text)
    {
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");
            hasher.update(text.getBytes());
            return DatatypeConverter.printHexBinary(hasher.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException nsae) {
            return nsae.getMessage();
        }
    }

    private Boolean checkUsername(String username){
        ArrayList<users> list = new ArrayList<>();
        usersService.selectAll(list, loginLaunch.database);
        Boolean taken = false;
        for(users p : list){
            if(username.equals(p.getUsername())){
                taken = true;
            }
        }
        return taken;
    }

    private void loadLoginScreen(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Controller/loginScreen.fxml"));
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
}
