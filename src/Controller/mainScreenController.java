package Controller;

import Model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.crypto.Data;


public class mainScreenController implements Initializable {

    @FXML
    TextField searchField;
    @FXML
    Slider volumeSlider;
    @FXML
    Label startTime;
    @FXML
    ProgressBar progBar;
    @FXML
    Label songTitle;
    @FXML
    Label endTime;
    @FXML
    private TableView<tableTrack> tableView;
    @FXML
    private TableColumn<tableTrack, String> nameCol;
    @FXML
    private TableColumn<tableTrack, String> durationCol;
    @FXML
    private TableColumn<tableTrack, String> artistCol;
    @FXML
    private TableColumn<tableTrack, String> genreCol;
    @FXML
    private TableColumn<tableTrack, Integer> playsCol;
    File cwd = new File("Songs/").getAbsoluteFile();
    ObservableList<tableTrack> songsToAdd = FXCollections.observableArrayList();

    @FXML
    protected void handleBackButtonPress(ActionEvent event) {

    }

    @FXML
    protected void handlePlayButtonPress(ActionEvent event) {

    }

    @FXML
    protected void handleNextButtonPress(ActionEvent event) {

    }

    @FXML
    protected void handleSearchButtonPress(ActionEvent event) {

    }

    @FXML
    protected void handleAddSong(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        List<File> fileList = fileChooser.showOpenMultipleDialog(stage);
        if (fileList != null) {
            for (File f : fileList) {
                addToDatabase(f);
                getMetaDataTracks(f);
            }
            moveToLibrary(fileList);
        }
    }

    private void addToDatabase(File file) {
        artists toSave = getMetaDataArtists(file);
        ArrayList<artists> artistList = new ArrayList<>();
        artistsService.selectAll(artistList, loginLaunch.database);
        Boolean inDatabase = false;
        String artistID = null;
        artists artist = null;
        for (artists a : artistList) {
            try {
                if (a.getArtistName().equals(toSave.getArtistName())) {
                    inDatabase = true;
                    artist = a;
                }
            } catch (Exception e) {
            }
        }
        tracks track = getMetaDataTracks(file);
        if (inDatabase == true) {
            track.setArtistID(artist.getArtistID());
            tracksService.save(track, loginLaunch.database);
        } else {
            artist = getMetaDataArtists(file);
            artistsService.save(artist, loginLaunch.database);
            track.setArtistID(artist.getArtistID());
            tracksService.save(track, loginLaunch.database);
        }
        ArrayList<users> uList = new ArrayList<>();
        usersService.selectAll(uList, loginLaunch.database);
        for(users u : uList){
            trackPlaysService.save(new trackPlays(u.getUserID(), track.getTrackID(), 0), loginLaunch.database);
        }
    }

    private tracks getMetaDataTracks(File file) {
        try {
            InputStream input = new FileInputStream(new File(file.getAbsolutePath()));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();
            /*/String[] metadataNames = metadata.names();
            for(String name : metadataNames){
                System.out.println(name + ": " + metadata.get(name));
            }/*/
            System.out.println(file.getAbsolutePath());
            String path = file.getAbsolutePath();
            String[] items = path.split("\\\\");
            path = items[items.length - 1];
            String s = metadata.get("xmpDM:duration");
            return new tracks(0, 0, metadata.get("title"), s, "/Songs/" + path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (TikaException e) {
            e.printStackTrace();
            return null;
        }
    }

    private artists getMetaDataArtists(File file) {
        try {
            InputStream input = new FileInputStream(new File(file.getAbsolutePath()));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();
            return new artists(0, metadata.get("xmpDM:artist"), metadata.get("xmpDM:genre"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (TikaException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void moveToLibrary(List<File> list) {
        try {
            for (File f : list) {
                FileUtils.copyFileToDirectory(f, cwd);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set up columns
        nameCol.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("name"));
        artistCol.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("artist"));
        genreCol.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("genre"));
        durationCol.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("duration"));
        playsCol.setCellValueFactory(new PropertyValueFactory<tableTrack, Integer>("plays"));
        //load data/*/
        System.out.println("Loading table...");
        tableView.setItems(getTracks());
        System.out.println("Success!");

    }

    private ObservableList<tableTrack> getTracks() {
        ArrayList<tracks> trackList = new ArrayList<>();
        ArrayList<artists> artistList = new ArrayList<>();
        tracksService.selectAll(trackList, loginLaunch.database);
        artistsService.selectAll(artistList, loginLaunch.database);
        for (tracks t : trackList) {
            for (artists a : artistList) {
                if (a.getArtistID() == t.getArtistID()) {
                    System.out.println("adding track");
                    tableTrack newRow = new tableTrack(t.getTrackName(), a.getArtistName(), checkGenre(a.getGenre()), getDuration(t.getLength()), 0);
                    System.out.println("created");
                    songsToAdd.add(newRow);
                }
            }
            //artists a = artistsService.selectById(t.getArtistID(), loginLaunch.database); giving closed result set so will be doing it inefficiently temporarily
        }
        return songsToAdd;
    }

    private String getDuration(String s) {
        double d = Double.parseDouble(s);
        int i = (int) d;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(i);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(i);
        return minutes + ":" + seconds % 60;
    }

    private String checkGenre(String a) {
        if (a.equals("")) {
            return "n/a";
        } else {
            return a;
        }
    }
}
