package Model;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class genrePlaysService {

    public static void selectAll(List<genrePlays> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT userID, artistID, timesPlayed FROM genrePlays ORDER BY artistID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results != null){
                    while(results.next()){
                        targetList.add(new genrePlays(
                                results.getInt("userID"),
                                results.getInt("artistID"),
                                results.getInt("timesPlayed")
                        ));
                    }
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static genrePlays selectById(int id, DatabaseConnection database){
        genrePlays result = null;

        PreparedStatement statement = database.newStatement("Select userID, artistID, timesPlayed FROM genrePlays WHERE id=?");

        try{
            if(statement != null){
                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if(results != null) {
                    result = new genrePlays(
                            results.getInt("userID"),
                            results.getInt("artistID"),
                            results.getInt("timesPlayed"));
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select by id error" + resultsException.getMessage());
        }
        return result;
    }
    public static void save(genrePlays itemToSave, DatabaseConnection database) {

        genrePlays existingItem = null;
        if (itemToSave.getUserID() != 0) existingItem = selectById(itemToSave.getUserID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO genrePlays (childID, artistID, timesPlayed) VALUES (?,?,?)");
                statement.setInt(0, itemToSave.getUserID());
                statement.setInt(1, itemToSave.getArtistID());
                statement.setInt(2, itemToSave.getTimesPlayed());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE genrePlays SET childID, artistID, timesPlayed = ? WHERE childID = ?, artistID = ?");
                statement.setInt(2, itemToSave.getTimesPlayed());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteById(int id, DatabaseConnection database){
        PreparedStatement statement =  database.newStatement("DELETE FROM genrePlays WHERE id=?");
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
