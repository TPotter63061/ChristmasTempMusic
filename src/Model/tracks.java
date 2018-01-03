package Model;

public class tracks {
    private int trackID;
    private int artistID;
    private String trackName;
    private String length;
    private String path;

    public tracks(int trackID, int artistID, String trackName, String length, String path) {
        this.trackID = trackID;
        this.artistID = artistID;
        this.trackName = trackName;
        this.length = length;
        this.path = path;
    }

    public int getTrackID(){return trackID;}
    public void setTrackID(int trackID){this.trackID = trackID;}

    public int getArtistID(){return artistID;}
    public void setArtistID(int artistID){this.artistID = artistID;}

    public String getTrackName(){return trackName;}
    public void setTrackName(String trackName){this.trackName = trackName;}

    public String getLength(){return length;}
    public void setLength(String length){this.length = length;}

    public String getPath(){return path;}
    public void setPath(String path){this.path = path;}
}
