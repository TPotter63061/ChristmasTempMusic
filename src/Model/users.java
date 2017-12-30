package Model;

public class users {
    private int userID;
    private String username;
    private String password;
    private String salt;

    public users(int userID, String username, String password, String salt) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public int getUserID(){return userID;}
    public void setUserID(int userID){this.userID = userID;}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    public String getSalt(){return salt;}
    public void setSalt(String salt){this.salt = salt;}
}
