package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class InfoController {
    @FXML
    Button nextButton;
    @FXML
    CheckBox header;


    public void closeWindow(ActionEvent event){
        Stage stage = (Stage) nextButton.getScene().getWindow();
        // do what you have to do
        stage.close();


    }
}
