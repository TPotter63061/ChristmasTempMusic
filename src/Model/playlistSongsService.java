package Model;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class playlistSongsService {

    public static void selectAll(List<playlistSongs> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT playlistID, trackID FROM playlistSongs ORDER BY playlistID");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results != null){
                    while(results.next()){
                        targetList.add(new playlistSongs(
                                results.getInt("playlistID"),
                                results.getInt("trackID")
                        ));
                    }
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static playlistSongs selectById(int id, DatabaseConnection database){
        playlistSongs result = null;

        PreparedStatement statement = database.newStatement("Select playlistID, trackID, genre FROM artists WHERE id=?");

        try{
            if(statement != null){
                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if(results != null) {
                    result = new playlistSongs(
                            results.getInt("playlistID"),
                            results.getInt("trackID")
                    );
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select by id error" + resultsException.getMessage());
        }
        return result;
    }
    public static void save(playlistSongs itemToSave, DatabaseConnection database) {

        playlistSongs existingItem = null;
        if (itemToSave.getPlaylistID() != 0) existingItem = selectById(itemToSave.getPlaylistID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO playlistSongs (playlistID, trackID) VALUES (?,?)");
                statement.setInt(1, itemToSave.getPlaylistID());
                statement.setInt(2, itemToSave.getTrackID());
                database.executeUpdate(statement);
            }
            else {
                System.out.println("Track already in playlist");
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteById(int id, DatabaseConnection database){
        PreparedStatement statement =  database.newStatement("DELETE FROM playlistSongs WHERE id=?");

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

