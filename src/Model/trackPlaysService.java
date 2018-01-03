package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class trackPlaysService {

    public static void selectAll(List<trackPlays> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT userID, trackID, timesPlayed FROM trackPlays ORDER BY trackID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results != null){
                    while(results.next()){
                        targetList.add(new trackPlays(
                                results.getInt("userID"),
                                results.getInt("trackID"),
                                results.getInt("timesPlayed")
                        ));
                    }
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static trackPlays selectById(int id, DatabaseConnection database){
        trackPlays result = null;

        PreparedStatement statement = database.newStatement("Select userID, trackID, timesPlayed FROM trackPlays WHERE id=?");

        try{
            if(statement != null){
                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if(results != null) {
                    result = new trackPlays(
                            results.getInt("userID"),
                            results.getInt("trackID"),
                            results.getInt("timesPlayed"));
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select by id error" + resultsException.getMessage());
        }
        return result;
    }
    public static void save(trackPlays itemToSave, DatabaseConnection database) {

        trackPlays existingItem = null;
        if (itemToSave.getUserID() != 0) existingItem = selectById(itemToSave.getUserID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO trackPlays (userID, trackID, timesPlayed) VALUES (?,?,?)");
                statement.setInt(1, itemToSave.getUserID());
                statement.setInt(2, itemToSave.getTrackID());
                statement.setInt(3, itemToSave.getTimesPlayed());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteById(int id, DatabaseConnection database){
        PreparedStatement statement =  database.newStatement("DELETE FROM trackPlays WHERE id=?");
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
