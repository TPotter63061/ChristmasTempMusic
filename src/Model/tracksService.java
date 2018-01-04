package Model;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class tracksService {

    public static void selectAll(List<tracks> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT trackID, artistID, trackName, length, path FROM tracks ORDER BY artistID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results != null){
                    while(results.next()){
                        targetList.add(new tracks(
                                results.getInt("trackID"),
                                results.getInt("artistID"),
                                results.getString("trackName"),
                                results.getString("length"),
                                results.getString("path")
                        ));
                    }
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static tracks selectById(int id, DatabaseConnection database){
        tracks result = null;
        PreparedStatement statement = database.newStatement("SELECT trackID, artistID, trackName, length, path FROM tracks WHERE id=?");

        try{
            if(statement != null){
                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if(results != null) {
                    result = new tracks(
                            results.getInt("trackID"),
                            results.getInt("artistID"),
                            results.getString("trackName"),
                            results.getString("length"),
                            results.getString("path")
                    );
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select by id error" + resultsException.getMessage());
        }
        return result;
    }
    public static void save(tracks itemToSave, DatabaseConnection database) {
        tracks existingItem = null;
       // if (itemToSave.getTrackID() != 0) existingItem = selectById(itemToSave.getTrackID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO tracks (trackID, artistID, trackName, length, path) VALUES (?,?,?,?,?)");
                statement.setInt(2, itemToSave.getArtistID());
                statement.setString(3, itemToSave.getTrackName());
                statement.setString(4, itemToSave.getLength());
                statement.setString(5,itemToSave.getPath());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE tracks SET trackID, artistID, trackName, length,path= ? WHERE trackID = ?, artistID = ?");
                statement.setString(1, itemToSave.getTrackName());
                statement.setString(2, itemToSave.getLength());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteById(int id, DatabaseConnection database){
        PreparedStatement statement =  database.newStatement("DELETE FROM tracks WHERE id=?");
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
