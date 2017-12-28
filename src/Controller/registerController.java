package Controller;

import Model.DatabaseConnection;
import Model.users;
import Model.usersService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class registerController {
    public static DatabaseConnection database;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField1;
    @FXML private TextField passwordField2;

    @FXML protected void handleRegisterButtonPress(ActionEvent event) {
        String password1 = passwordField1.getText();
        String password2 = passwordField2.getText();
        String username = usernameField.getText();
        if(password2.equals(password2)){
            System.out.println("passwords match");
            if(checkUsername(username) == false){
                System.out.println("username available");
                createAccount(username, password1);
            }

        }else{
            System.out.println("passwords dont match");
        }
    }

    private void createAccount(String username, String password){
        String hashedPass = generateHash(password);
        System.out.println("password hashed");
        users p = new users(0, username, hashedPass);
        usersService.save(p, database);
        System.out.println("save successful");
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
        database = new DatabaseConnection("musicPlayer.db");
        ArrayList<users> list = new ArrayList<>();
        usersService.selectAll(list, database);
        Boolean taken = false;
        for(users p : list){
            if(username.equals(p.getUsername())){
                taken = true;
            }
        }
        return taken;
    }
}
