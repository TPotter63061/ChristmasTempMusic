package Model;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class artistsService {

    public static void selectAll(List<artists> targetList, DatabaseConnection database){
        PreparedStatement statement = database.newStatement("SELECT artistID, artistName, genre FROM artists ORDER BY artistName");
        try{
            if(statement != null){
                ResultSet results = database.executeQuery(statement);

                if(results != null){
                    while(results.next()){
                        targetList.add(new artists(
                                results.getInt("artistID"),
                                results.getString("artistName"),
                                results.getString("genre")
                        ));
                    }
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select all error: " + resultsException.getMessage());
        }
    }

    public static artists selectById(int id, DatabaseConnection database){
        artists result = null;

        PreparedStatement statement = database.newStatement("Select artistID, artistName, genre FROM artists WHERE id=?");

        try{
            if(statement != null){
                statement.setInt(1, id);
                ResultSet results = database.executeQuery(statement);

                if(results != null) {
                    result = new artists(
                            results.getInt("artistID"),
                            results.getString("artistName"),
                            results.getString("genre"));
                }
            }
        }catch (SQLException resultsException){
            System.out.println("Database select by id error" + resultsException.getMessage());
        }
        return result;
    }
    public static void save(artists itemToSave, DatabaseConnection database) {

        artists existingItem = null;
        if (itemToSave.getArtistID() != 0) existingItem = selectById(itemToSave.getArtistID(), database);

        try {
            if (existingItem == null) {
                PreparedStatement statement = database.newStatement("INSERT INTO artists (artistName, genre) VALUES (?,?)");
                statement.setString(1, itemToSave.getArtistName());
                statement.setString(2, itemToSave.getGenre());
                database.executeUpdate(statement);
            }
            else {
                PreparedStatement statement = database.newStatement("UPDATE artists SET artistName = ?, genre = ? WHERE artistID = ?");
                statement.setString(1, itemToSave.getArtistName());
                statement.setString(2, itemToSave.getGenre());
                database.executeUpdate(statement);
            }
        } catch (SQLException resultsException) {
            System.out.println("Database saving error: " + resultsException.getMessage());
        }
    }
    public static void deleteById(int id, DatabaseConnection database){
        PreparedStatement statement =  database.newStatement("DELETE FROM artists WHERE id=?");

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
