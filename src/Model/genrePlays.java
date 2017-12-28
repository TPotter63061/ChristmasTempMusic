package Model;

public class genrePlays {
    private int userID;
    private int artistID;
    private int timesPlayed;

    public genrePlays(int userID, int artistID, int timesPlayed) {
        this.userID = userID;
        this.artistID = artistID;
        this.timesPlayed = timesPlayed;
    }

    public int getUserID(){return userID;}
    public void setUserID(int userID){this.userID = userID;}

    public int getArtistID(){return artistID;}
    public void setArtistID(int artistID){this.artistID = artistID;}

    public int getTimesPlayed(){return timesPlayed;}
    public void setTimesPlayed(int timesPlayed){this.timesPlayed = timesPlayed;}
}
