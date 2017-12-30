package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

public class mainScreenController {

    @FXML TextField searchField;
    @FXML Slider volumeSlider;
    @FXML Label startTime;
    @FXML ProgressBar progBar;
    @FXML Label songTitle;
    @FXML Label endTime;

    @FXML protected void handleBackButtonPress(ActionEvent event){
        
    }
    @FXML protected void handlePlayButtonPress(ActionEvent event){

    }
    @FXML protected void handleNextButtonPress(ActionEvent event){

    }
    @FXML protected void handleSearchButtonPress(ActionEvent event){

    }
    @FXML protected void handleAddSong(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        //fileChooser.showOpenDialog(stage);
    }

}
