package Model;

public class playlists {
    private int playlistID;
    private int userID;
    private String playlistName;
    private int timesPlayed;

    public playlists(int playlistID, int userID, String playlistName, int timesPlayed) {
        this.playlistID = playlistID;
        this.userID = userID;
        this.playlistName = playlistName;
        this.timesPlayed = timesPlayed;
    }

    public int getPlaylistID(){return playlistID;}
    public void setPlaylistID(int playlistID){this.playlistID = playlistID;}

    public int getUserID(){return userID;}
    public void setUserID(int userID){this.userID = userID;}

    public String getPlaylistName(){return playlistName;}
    public void setPlaylistName(String playlistName){this.playlistName = playlistName;}

    public int getTimesPlayed(){return timesPlayed;}
    public void setTimesPlayed(int timesPlayed){this.timesPlayed = timesPlayed;}
}
