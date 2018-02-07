package Controller;

import Model.*;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Duration;
import org.apache.commons.io.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.tukaani.xz.check.Check;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;

import static Controller.loginController.currentAccount;

public class mainScreenController implements Initializable {

    @FXML
    TextField searchField;
    @FXML
    Slider volumeSlider;
    @FXML
    Label playlistSelectLabel;
    @FXML
    Label startTime;
    @FXML
    Slider progBar;
    @FXML
    Label songTitle;
    @FXML
    Label endTime;
    @FXML
    Tab playlistTab;
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
    private TableView<tableTrack> tableViewPlaylist;
    @FXML
    private TableColumn<tableTrack, String> nameColPlaylist;
    @FXML
    private TableColumn<tableTrack, String> durationColPlaylist;
    @FXML
    private TableColumn<tableTrack, String> artistColPlaylist;
    @FXML
    private TableColumn<tableTrack, String> genreColPlaylist;
    @FXML
    private TableView<tableTrack> tableViewQueue;
    @FXML
    private TableColumn<tableTrack, String> nameColQueue;
    @FXML
    private TableColumn<tableTrack, String> durationColQueue;
    @FXML
    private TableColumn<tableTrack, String> artistColQueue;
    @FXML
    private TableColumn<tableTrack, String> genreColQueue;
    @FXML TextField playlistTextBox;
    @FXML ChoiceBox choiceBox;
    @FXML TabPane tabPane;
    @FXML
    private TableColumn<tableTrack, Integer> playsCol;
    File cwd = new File("Songs/").getAbsoluteFile();
    ObservableList<tableTrack> songsToAdd = FXCollections.observableArrayList();
    private tableTrack selected;
    private MediaPlayer mediaPlayer;
    private Media media;
    private Boolean paused;
    private Boolean repeat = false;
    private tableTrack currentSong;
    private Boolean shuffle = false;
    private Boolean playlist = false;
    private playlists currentPlaylist;
    private ArrayList<playlists> usersPlaylists = new ArrayList<>();
    ObservableList<tableTrack> queue = FXCollections.observableArrayList();
    ObservableList<String> usersPlaylistsNames = FXCollections.observableArrayList();

    @FXML
    protected void handleBackButtonPress(ActionEvent event) {
        if(tabPane.getSelectionModel().getSelectedItem().getId().equals("playlistTab")){
            int currentId = tableViewPlaylist.getSelectionModel().getSelectedIndex();
            if(currentId -1 >= 0){
                //check it fits
                tableViewPlaylist.getSelectionModel().select(currentId-1);
                playSong(tableViewPlaylist.getSelectionModel().getSelectedItem());
            }else {
                tableViewPlaylist.getSelectionModel().select(0);
                playSong(tableViewPlaylist.getSelectionModel().getSelectedItem());
            }
        }else if(tabPane.getSelectionModel().getSelectedItem().getId().equals("library")){
            int currentId = tableView.getSelectionModel().getSelectedIndex();
            if(currentId -1 >= 0){
                //check it fits
                tableView.getSelectionModel().select(currentId-1);
                playSong(tableView.getSelectionModel().getSelectedItem());
            }else {
                tableView.getSelectionModel().select(0);
                playSong(tableView.getSelectionModel().getSelectedItem());
            }
        }else{
            int currentId = tableViewQueue.getSelectionModel().getSelectedIndex();
            if(currentId -1 >= 0){
                //check it fits
                tableViewQueue.getSelectionModel().select(currentId-1);
                playSong(tableViewQueue.getSelectionModel().getSelectedItem());
            }else {
                tableViewQueue.getSelectionModel().select(0);
                playSong(tableViewQueue.getSelectionModel().getSelectedItem());
            }
        }

    }
    @FXML
    protected  void handleSongClickedInTable(ActionEvent event){

    }
    @FXML
    protected void handlePlayButtonPress(ActionEvent event) {
        if ((mediaPlayer != null) && (paused == false)) {
            mediaPlayer.pause();
            paused = true;
        }else if((mediaPlayer != null) && (paused = true)){
            mediaPlayer.play();
            paused = false;
        }

    }


    @FXML
    protected void handleCreatePlaylistButton(ActionEvent event) {
        System.out.println("creating playlist: " + playlistTextBox.getText());
        System.out.println(currentAccount.getUserID());
        if (currentAccount!=null) {
            playlistsService.save(new playlists(0, currentAccount.getUserID(), playlistTextBox.getText(), 0), loginLaunch.database);
        }else{
            System.out.println("Create an account to unlock this feature");
        }
    }

    @FXML
    protected void addSongToPlaylist(ActionEvent event){
        tableTrack selectedSong = tableView.getSelectionModel().getSelectedItem();
        int trackID = getTrackID(selectedSong.getName());
        playlistSongsService.save(new playlistSongs(currentPlaylist.getPlaylistID(),trackID), loginLaunch.database );
    }

    @FXML
    protected void handleNextButtonPress(ActionEvent event) {
        if(tabPane.getSelectionModel().getSelectedItem().getId().equals("playlistTab")){
            if(shuffle) {
                int x = tableViewPlaylist.getItems().size();
                Random rand = new Random();
                tableViewPlaylist.getSelectionModel().select(rand.nextInt(x));
                playSong(tableViewPlaylist.getSelectionModel().getSelectedItem());
            }else {
                int currentId = tableViewPlaylist.getSelectionModel().getSelectedIndex();
                if (currentId + 1 < tableViewPlaylist.getItems().size()) {
                    //check it fits
                    tableViewPlaylist.getSelectionModel().select(currentId + 1);
                    playSong(tableViewPlaylist.getSelectionModel().getSelectedItem());
                } else {
                    //loops back around
                    tableViewPlaylist.getSelectionModel().select(0);
                    playSong(tableViewPlaylist.getSelectionModel().getSelectedItem());
                }
            }
        }else if(tabPane.getSelectionModel().getSelectedItem().getId().equals("library")){
            if(shuffle){
                int x = tableView.getItems().size();
                Random rand = new Random();
                tableView.getSelectionModel().select(rand.nextInt(x));
                playSong(tableView.getSelectionModel().getSelectedItem());
            }else {
                int currentId = tableView.getSelectionModel().getSelectedIndex();
                if (currentId + 1 < tableView.getItems().size()) {
                    //check it fits
                    tableView.getSelectionModel().select(currentId + 1);
                    playSong(tableView.getSelectionModel().getSelectedItem());
                } else {
                    //loops back around
                    tableView.getSelectionModel().select(0);
                    playSong(tableView.getSelectionModel().getSelectedItem());
                }
            }
        }else{
            int currentId = tableViewQueue.getSelectionModel().getSelectedIndex();
            if(currentId +1 < tableViewQueue.getItems().size()){
                //check it fits
                tableViewQueue.getSelectionModel().select(currentId+1);
                playSong(tableViewQueue.getSelectionModel().getSelectedItem());
            }else{
                //loops back around
                tableViewQueue.getSelectionModel().select(0);
                playSong(tableViewQueue.getSelectionModel().getSelectedItem());
            }
        }

    }
    private int getTrackID(String selected){
        ArrayList<tracks> trackMatch = new ArrayList<>();
        tracksService.selectAll(trackMatch,loginLaunch.database);
        for(tracks x : trackMatch){
            System.out.println(selected);
            if(x.getTrackName().equals(selected)){
                return x.getTrackID();
            }
        }
        return 0;
    }

    @FXML
    protected void handleDeleteSong(ActionEvent event){
        tracksService.deleteById(getTrackID(tableView.getSelectionModel().getSelectedItem().getName()), loginLaunch.database);
        try {
            FileUtils.forceDelete(FileUtils.getFile("C:/Users/choco/Desktop/FinalPhase/final/" + selected.getPath()));
        }catch (Exception i){
            System.out.println(i.getMessage());
        }
        if(songsToAdd.size() ==1 ){
            songsToAdd.remove(0);
        }else {
            songsToAdd.remove(songsToAdd.indexOf(selected));
            tableView.refresh();
        }

    }

    @FXML
    protected void handleClearButtonPress(ActionEvent event){
        tableView.setItems(songsToAdd);
    }
    @FXML
    protected void handleRepeatAction(ActionEvent event){
        repeat = !repeat;
    }
    @FXML
    protected void handleShuffleAction(ActionEvent event){
        shuffle = !shuffle;
    }
    @FXML
    protected void handleSearchButtonPress(ActionEvent event) {
        ObservableList<tableTrack> searchList = FXCollections.observableArrayList();
        for(tableTrack x : songsToAdd){
            if(x.getArtist().contains(searchField.getText()) || (x.getName().contains(searchField.getText()))){
                searchList.add(x);
            }
        }
        tableView.setItems(searchList);
    }
    @FXML
    protected void handleAddToQueuePress(ActionEvent event){
        if(!playlist){
            queue.add(tableView.getSelectionModel().getSelectedItem());
        }else{
            System.out.println("Deselect playlist to create queue");
        }
        tableViewQueue.refresh();
    }

    @FXML
    protected void handleClearQueuePress(ActionEvent event){
        queue.remove(queue.size());
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
                addToTable(getMetaDataTracks(f), getMetaDataArtists(f));
                tableView.refresh();
            }
            moveToLibrary(fileList);
        }
    }
    private void addToTable(tracks t, artists a){
        tableTrack newRow = new tableTrack(t.getTrackName(),a.getArtistName(),a.getGenre(),getDuration(t.getLength()),0,t.getPath());
        songsToAdd.add(newRow);
    }
    private double toSeconds(String s){
        String[] split = s.split(":");
        double seconds = (Double.parseDouble(split[0])) * 60;
        return seconds + Double.parseDouble(split[1]);
    }

    private void songEnd(){
        if(playlist == false && queue.size() == 0) {
            if (repeat) {
                playSong(currentSong);
            } else if (shuffle) {
                Random rand = new Random();
                playSong(songsToAdd.get(rand.nextInt(songsToAdd.size())));
            }else{
                int currentId = tableView.getSelectionModel().getSelectedIndex();
                if(currentId +1 < tableView.getItems().size()){
                    //check it fits
                    tableView.getSelectionModel().select(currentId+1);
                    playSong(tableView.getSelectionModel().getSelectedItem());
                }else{
                    //loops back around
                    tableView.getSelectionModel().select(0);
                    playSong(tableView.getSelectionModel().getSelectedItem());
                }
            }
        }else if(playlist == false){
            playSong(queue.get(0));
            queue.remove(0);
        }else if(playlist){
            if (repeat) {
                playSong(currentSong);
            } else if (shuffle) {
                int x = tableViewPlaylist.getItems().size();
                Random rand = new Random();
                tableViewPlaylist.getSelectionModel().select(rand.nextInt(x));
                playSong(tableViewPlaylist.getSelectionModel().getSelectedItem());
            }else{
                int currentId = tableViewPlaylist.getSelectionModel().getSelectedIndex();
                if(currentId +1 < tableViewPlaylist.getItems().size()){
                    //check it fits
                    tableViewPlaylist.getSelectionModel().select(currentId+1);
                    playSong(tableViewPlaylist.getSelectionModel().getSelectedItem());
                }else{
                    //loops back around
                    tableViewPlaylist.getSelectionModel().select(0);
                    playSong(tableViewPlaylist.getSelectionModel().getSelectedItem());
                }
            }

        }
    }
    private void playSong(tableTrack toPlay) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        currentSong = toPlay;
        String path = "C:/Users/choco/Desktop/FinalPhase/final/" + toPlay.getPath();
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        endTime.setText(toPlay.getDuration() + "    ");
        songTitle.setText(toPlay.getName() + "-" + toPlay.getArtist());
        startTime.setText("00:00");
        progBar.setMin(0);
        progBar.setMax(toSeconds(toPlay.getDuration()));
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mediaPlayer.setVolume(newValue.doubleValue() / 100);
        });
        progBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            //I am not proud of this
            if (((oldValue.intValue() + 1) < newValue.intValue()) || ((oldValue.intValue() - 1) > newValue.intValue())) {
                mediaPlayer.seek(Duration.seconds(progBar.getValue()));
            }
        });
        mediaPlayer.currentTimeProperty().addListener(((observable, oldValue, newValue) -> {
            progBar.setValue(newValue.toSeconds());
            startTime.setText(getDuration(String.valueOf(newValue.toMillis())));
        }));
        mediaPlayer.setOnEndOfMedia(() -> songEnd());
        paused = false;
    }

    private void addToDatabase(File file) {
        artists toSave = getMetaDataArtists(file);
        ArrayList<artists> artistList = new ArrayList<>();
        artistsService.selectAll(artistList, loginLaunch.database);
        Boolean inDatabase = false;
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
            artistsService.save(toSave, loginLaunch.database);
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

    private ObservableList<tableTrack> getPlaylistItems(){
        ArrayList<playlistSongs> tracksFromAllPlaylist = new ArrayList<>();
        playlistSongsService.selectAll(tracksFromAllPlaylist, loginLaunch.database);
        ArrayList<Integer> trackIDSFromPlaylist = new ArrayList<>();
        for(playlistSongs x : tracksFromAllPlaylist){
            if(x.getPlaylistID() == currentPlaylist.getPlaylistID()){
                trackIDSFromPlaylist.add(x.getTrackID());
                System.out.println(x.getTrackID());
            }
        }
        ArrayList<tracks> tracksFromSelectedPlaylist = new ArrayList<>();
        for(Integer a : trackIDSFromPlaylist){
            tracksFromSelectedPlaylist.add(tracksService.selectById(a, loginLaunch.database));
        }
        ObservableList<tableTrack> toAddToPlaylistView = FXCollections.observableArrayList();
        for(tracks t : tracksFromSelectedPlaylist){
            artists a = artistsService.selectById(t.getArtistID(), loginLaunch.database);
            toAddToPlaylistView.add(new tableTrack(t.getTrackName(), a.getArtistName(), checkGenre(a.getGenre()), getDuration(t.getLength()), 0, t.getPath()));
        }

        return toAddToPlaylistView;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<playlists> allPlaylists = new ArrayList<>();
        playlistsService.selectAll(allPlaylists, loginLaunch.database);
        usersPlaylistsNames.add("No Playlist");
        if(currentAccount!=null){
            for(playlists p : allPlaylists){
                if(p.getUserID() == currentAccount.getUserID()){
                    usersPlaylistsNames.add(p.getPlaylistName());
                    usersPlaylists.add(p);
                }
            }
        }
        choiceBox.setItems(usersPlaylistsNames);
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Boolean found = false;
                for(playlists x : usersPlaylists){
                    if(x.getPlaylistName().equals(usersPlaylistsNames.get(newValue.intValue()))){
                        currentPlaylist = x;
                        playlistSelectLabel.setText(currentPlaylist.getPlaylistName());
                        playlistTab.setText("Playlist: " + currentPlaylist.getPlaylistName());
                        found = true;
                        playlist = true;
                        tableViewPlaylist.setItems(getPlaylistItems());
                    }
                }
                if (found == false) {
                    playlistTab.setText("No Playlist Selected");
                    currentPlaylist = null;
                    playlist = false;
                    tableViewPlaylist.setItems(null);
                }
            }
        });
        endTime.setText("00:00" + "    ");
        songTitle.setText("");
        startTime.setText("00:00");
        volumeSlider.setValue(50);
        tableView.setRowFactory(tv -> {
            TableRow<tableTrack> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(playlist){
                    System.out.println("Playlist Active close playlist to listen to songs");
                }else {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        tableTrack rowData = row.getItem();
                        selected = rowData;
                        playSong(selected);
                    }
                }
            });
            return row ;
        });
        tableViewPlaylist.setRowFactory(tv -> {
            TableRow<tableTrack> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    tableTrack rowData = row.getItem();
                    selected = rowData;
                    playSong(selected);
                }
            });
            return row ;
        });
        tableViewQueue.setRowFactory(tv -> {
            TableRow<tableTrack> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    tableTrack rowData = row.getItem();
                    selected = rowData;
                    playSong(selected);
                }
            });
            return row ;
        });
        nameColPlaylist.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("name"));
        artistColPlaylist.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("artist"));
        genreColPlaylist.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("genre"));
        durationColPlaylist.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("duration"));

        nameColQueue.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("name"));
        artistColQueue.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("artist"));
        genreColQueue.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("genre"));
        durationColQueue.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("duration"));

        nameCol.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("name"));
        artistCol.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("artist"));
        genreCol.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("genre"));
        durationCol.setCellValueFactory(new PropertyValueFactory<tableTrack, String>("duration"));
        playsCol.setCellValueFactory(new PropertyValueFactory<tableTrack, Integer>("plays"));

        //load data/*/
        System.out.println("Loading table...");
        tableView.setItems(getTracks());
        tableViewQueue.setItems(queue);
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
                    tableTrack newRow = new tableTrack(t.getTrackName(), a.getArtistName(), checkGenre(a.getGenre()), getDuration(t.getLength()), 0, t.getPath());
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
        if((seconds % 60) < 10){
            return minutes + ":0" + seconds %60;
        }
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
