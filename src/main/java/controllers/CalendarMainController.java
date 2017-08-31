package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Thanapong Supalak 5810405029
 */


public class CalendarMainController {

    @FXML
    private Button addBtn;
    @FXML
    private TextField nameTxtF;
    @FXML
    private TextArea descTxtA;
    @FXML
    private TextField dateTxtF;


    @FXML
    void addEvent(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("CalendarEventUI.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Event");
        stage.setResizable(false);
        stage.show();
    }
}
