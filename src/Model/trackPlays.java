package Model;

public class trackPlays {
    private int userID;
    private int trackID;
    private int timesPlayed;

    public trackPlays(int userID, int trackID, int timesPlayed) {
        this.userID = userID;
        this.trackID = trackID;
        this.timesPlayed = timesPlayed;
    }

    public int getUserID(){return userID;}
    public void setUserID(int userID){this.userID = userID;}

    public int getTrackID(){return trackID;}
    public void setTrackID(int trackID){this.trackID = trackID;}

    public int getTimesPlayed(){return timesPlayed;}
    public void setTimesPlayed(int timesPlayed){this.timesPlayed = timesPlayed;}
}