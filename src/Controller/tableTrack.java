package Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class tableTrack {
    private SimpleStringProperty name;
    private SimpleStringProperty artist;
    private SimpleStringProperty genre;
    private SimpleStringProperty duration;
    private SimpleIntegerProperty plays;
    private String path;

    public tableTrack(String name, String artist, String genre, String duration, int plays, String path) {
        this.name = new SimpleStringProperty(name);
        this.artist = new SimpleStringProperty(artist);
        this.genre = new SimpleStringProperty(genre);
        this.duration = new SimpleStringProperty(duration);
        this.plays = new SimpleIntegerProperty(plays);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getArtist() {
        return artist.get();
    }

    public SimpleStringProperty artistProperty() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public String getGenre() {
        return genre.get();
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public String getDuration() {
        return duration.get();
    }

    public SimpleStringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }
    public int getPlays() {
        return plays.get();
    }

    public SimpleIntegerProperty playsProperty() {
        return plays;
    }

    public void setPlays(int plays) {
        this.plays.set(plays);
    }
}
