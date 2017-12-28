package Model;

public class tracks {
    private int trackID;
    private int artistID;
    private String trackName;
    private int length;

    public tracks(int trackID, int artistID, String trackName, int length) {
        this.trackID = trackID;
        this.artistID = artistID;
        this.trackName = trackName;
        this.length = length;
    }

    public int getTrackID(){return trackID;}
    public void setTrackID(int trackID){this.trackID = trackID;}

    public int getArtistID(){return artistID;}
    public void setArtistID(int artistID){this.artistID = artistID;}

    public String getTrackName(){return trackName;}
    public void setTrackName(String trackName){this.trackName = trackName;}

    public int getLength(){return length;}
    public void setLength(int length){this.length = length;}
}
