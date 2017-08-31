package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.DateEvent;
import models.EventListSingleton;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    private TableView<DateEvent> eventTable;
    @FXML
    private TableColumn<DateEvent, LocalDate> dateColumn;
    @FXML
    private TableColumn<DateEvent, String> nameColumn;

    @FXML
    public void initialize() {
        String pattern = "dd MMMM YYYY";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        dateColumn.setCellFactory(c -> new TableCell<DateEvent, LocalDate>(){
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(dateFormatter.format(item));
            }
        });
        nameColumn.setCellValueFactory((new PropertyValueFactory<>("eventName")));
        eventTable.setItems(EventListSingleton.getInstance().getEventList());
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            String name = eventTable.getSelectionModel().getSelectedItem().getEventName();
            LocalDate date = eventTable.getSelectionModel().getSelectedItem().getEventDate();
            String description = eventTable.getSelectionModel().getSelectedItem().getEventDescription();
            nameTxtF.setText(name);
            dateTxtF.setText(dateFormatter.format(date));
            descTxtA.setText(description);
        });
    }

    @FXML
    private void addEvent(ActionEvent event) throws IOException {
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
