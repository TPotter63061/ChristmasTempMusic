package Model;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class usersService {

    public static void selectAll(List<users> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT userID, username, password, salt FROM users ORDER BY userID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results != null){
                    while(results.next()){
                        targetList.add(new users(
                                results.getInt("userID"),
                                results.getString("username"),
                                results.getString("password"),
                                results.getString("salt")
                        ));
                    }
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static users selectById(int id, DatabaseConnection database){
        users result = null;
        PreparedStatement statement = database.newStatement("SELECT userID, username, password, salt FROM users WHERE userID=?");

        try{
            if(statement != null){
                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if(results != null) {
                    result = new users(
                            results.getInt("userID"),
                            results.getString("username"),
                            results.getString("password"),
                            results.getString("salt")
                    );
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select by id error" + resultsException.getMessage());
        }
        return result;
    }
    public static void save(users itemToSave, DatabaseConnection database) {

        users existingItem = null;
        if (itemToSave.getUserID() != 0) existingItem = selectById(itemToSave.getUserID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO users (userID, username, password, salt) VALUES (?,?,?,?)");
                //statement.setInt(0, itemToSave.getUserID());
                statement.setString(2, itemToSave.getUsername());
                statement.setString(3, itemToSave.getPassword());
                statement.setString(4, itemToSave.getSalt());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE users SET userID, username, password, salt= ? WHERE username = ?");
                statement.setString(2, itemToSave.getUsername());
                statement.setString(3, itemToSave.getPassword());
                statement.setString(4, itemToSave.getSalt());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteById(int id, DatabaseConnection database){
        PreparedStatement statement =  database.newStatement("DELETE FROM users WHERE userID=?");
        try{
            if(statement != null){
                statement.setInt(1, id);
                database.executeUpdate(statement);
            }
        }catch (SQLException resultsException){
            System.out.println("Database deletion error: " + resultsException.getMessage());
        }
    }
}
