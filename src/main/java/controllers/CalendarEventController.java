package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.DateEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static javafx.scene.control.Alert.*;

/**
 * Thanapong Supalak 5810405029
 */

public class CalendarEventController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea eventDescTxtA;
    @FXML
    private TextField eventNameTxtF;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;

    private Boolean saveBool = false;
    private Stage stage;
    private DateEvent dateEvent;
    private DateTimeFormatter dateTimeFormatter;

    @FXML
    public void initialize() {
        setDatePickerFormat();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    @FXML
    private void onSave() {
        if (isValidDate()) {
            if (!eventNameTxtF.getText().isEmpty()) {
                dateEvent.setEventName(eventNameTxtF.getText());
                dateEvent.setEventDate(datePicker.getValue());
                dateEvent.setEventDescription(eventDescTxtA.getText());
                popDialog(AlertType.INFORMATION, "Success", "Event is saved!");

                saveBool = true;
                closeWindow();
            } else {
                popDialog(AlertType.ERROR, "Error", "Please fill in the name of the event");
            }
        } else {
            popDialog(AlertType.ERROR, "Error", "Date must not be in the past");
            datePicker.setValue(LocalDate.now());
        }
    }

    private void popDialog(AlertType alertType, String title, String message) {
        Alert alertBox = new Alert(alertType);
        alertBox.setHeaderText(null);
        alertBox.setTitle(title);
        alertBox.setContentText(message);
        alertBox.showAndWait();
    }

    private boolean isValidDate() {
        return !datePicker.getValue().isBefore(LocalDate.now());
    }

    private void setDatePickerFormat(){
        datePicker.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateTimeFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateTimeFormatter);
                } else {
                    return null;
                }
            }
        });
        datePicker.setValue(LocalDate.now());
    }
    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    private void closeWindow(){
        stage.close();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public boolean isSaved(){
        return saveBool;
    }

    public void setCurrentEvent(DateEvent currentEvent) {
        dateEvent = currentEvent;
    }
}