package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Thanapong Supalak 5810405029
 */


public class CalendarMainController {

    @FXML
    private Button addBtn;

    @FXML
    void addEvent(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarEventUI.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Calendar Event");
        stage.setResizable(false);
        stage.show();
    }

}
