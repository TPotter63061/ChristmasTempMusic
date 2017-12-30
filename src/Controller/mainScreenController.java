package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;
import org.apache.commons.io.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class mainScreenController {

    @FXML TextField searchField;
    @FXML Slider volumeSlider;
    @FXML Label startTime;
    @FXML ProgressBar progBar;
    @FXML Label songTitle;
    @FXML Label endTime;
    File cwd = new File("Songs/").getAbsoluteFile();

    @FXML protected void handleBackButtonPress(ActionEvent event){
        
    }
    @FXML protected void handlePlayButtonPress(ActionEvent event){

    }
    @FXML protected void handleNextButtonPress(ActionEvent event){

    }
    @FXML protected void handleSearchButtonPress(ActionEvent event){

    }
    @FXML protected void handleAddSong(ActionEvent event){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if(list != null){
            for(File f : list){
                getMetaData(f);
            }
            //moveToLibrary(list);
           // addToDatabase(list);

        }
    }

    private void getMetaData(File file){
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
            System.out.println("Title: " + metadata.get("title"));
            System.out.println("Artists: " + metadata.get("xmpDM:artist"));
            System.out.println("Genre: " + metadata.get("xmpDM:genre"));
            System.out.println("Duration(ms): " + metadata.get("xmpDM:duration"));
            System.out.println();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
    }

    private void addToDatabase(List<File> list){

    }

    private void moveToLibrary(List<File> list){
        try {
            for (File f : list) {
                FileUtils.copyFileToDirectory(f, cwd);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
