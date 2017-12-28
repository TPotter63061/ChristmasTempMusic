package Model;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class playlistsService {

    public static void selectAll(List<playlists> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT playlistID, userID, playlistName, timesPlayed FROM playlists ORDER BY playlistID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results != null){
                    while(results.next()){
                        targetList.add(new playlists(
                                results.getInt("playlistID"),
                                results.getInt("userID"),
                                results.getString("playlistName"),
                                results.getInt("timesPlayed")
                        ));
                    }
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static playlists selectById(int id, DatabaseConnection database){
        playlists result = null;
        PreparedStatement statement = database.newStatement("SELECT playlistID, userID, playlistName, timesPlayed FROM playlists WHERE id=?");

        try{
            if(statement != null){
                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if(results != null) {
                    result = new playlists(
                            results.getInt("playlistID"),
                            results.getInt("userID"),
                            results.getString("playlistName"),
                            results.getInt("timesPlayed")
                    );
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select by id error" + resultsException.getMessage());
        }
        return result;
    }
    public static void save(playlists itemToSave, DatabaseConnection database) {

        playlists existingItem = null;
        if (itemToSave.getUserID() != 0) existingItem = selectById(itemToSave.getUserID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO playlist (playlistID, userID, playlistName, timesPlayed) VALUES (?,?,?,?)");
                statement.setInt(0, itemToSave.getPlaylistID());
                statement.setInt(1, itemToSave.getUserID());
                statement.setString(2, itemToSave.getPlaylistName());
                statement.setInt(3, itemToSave.getTimesPlayed());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE playlists SET playlistID, userID, playlistName, timesPlayed= ? WHERE playlistID = ?, userID = ?");
                statement.setString(1, itemToSave.getPlaylistName());
                statement.setInt(2, itemToSave.getTimesPlayed());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteById(int id, DatabaseConnection database){
        PreparedStatement statement =  database.newStatement("DELETE FROM playlists WHERE id=?");
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
