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
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class CalendarEventUIController {

    public CheckBox repeatChoiceBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextArea eventDescTxtA;
    @FXML
    private TextField eventNameTxtF;
    @FXML
    private RadioButton highBtn, normalBtn, lowBtn;

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
        if (isValidDate(startDatePicker.getValue())) {
            if (!eventNameTxtF.getText().isEmpty()) {
                dateEvent.setEventName(eventNameTxtF.getText());
                dateEvent.setEventPriority(getPriorityFromButton());
                dateEvent.setEventStartDate(startDatePicker.getValue());
                dateEvent.setEventDescription(eventDescTxtA.getText());
                dateEvent.setRecurred(repeatChoiceBox.isSelected());
                popDialog(AlertType.INFORMATION, "Success", "Event is saved!");
                saveBool = true;
                closeWindow();
            } else {
                popDialog(AlertType.ERROR, "Error", "Please fill in the name of the event");
                eventNameTxtF.requestFocus();
            }
        } else {
            popDialog(AlertType.ERROR, "Error", "Date must not be in the past");
            startDatePicker.setValue(LocalDate.now());
        }
    }

    @FXML
    private void onRepeat(){
        // pop Recurrence Options Window -- to be implemented after exam week
    }

    private void popDialog(AlertType alertType, String title, String message) {
        Alert alertBox = new Alert(alertType);
        alertBox.setHeaderText(null);
        alertBox.setTitle(title);
        alertBox.setContentText(message);
        alertBox.showAndWait();
    }

    private int getPriorityFromButton(){
        return highBtn.isSelected() ? 1 : normalBtn.isSelected() ? 2 : 3;
    }

    private boolean isValidDate(LocalDate currentDate) {
        return !currentDate.isBefore(LocalDate.now());
    }

    private void setDatePickerFormat(){
        startDatePicker.setConverter(new StringConverter<LocalDate>() {

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
        startDatePicker.setValue(LocalDate.now());
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
        eventNameTxtF.setText(dateEvent.getEventName());
        eventDescTxtA.setText(dateEvent.getEventDescription());
        if(dateEvent.isRecurred()){
            repeatChoiceBox.setSelected(true);
            // additional implement -- to be implemented after exam week
        }
        if (dateEvent.getEventPriority() == 1) {
            highBtn.setSelected(true);
        } else {
            if (dateEvent.getEventPriority() == 2) {
                normalBtn.setSelected(true);
            } else {
                lowBtn.setSelected(true);
            }
        }
    }
}