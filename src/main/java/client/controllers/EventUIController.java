package client.controllers;

import common.DateEvent;
import common.DateEventFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;

import static javafx.scene.control.Alert.AlertType;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class EventUIController {

    public CheckBox repeatChoiceBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextArea eventDescTxtA;
    @FXML
    private TextField eventNameTxtF;
    @FXML
    private RadioButton dailyBtn, weeklyBtn, monthlyBtn, highBtn, normalBtn, lowBtn;
    @FXML
    private HBox recurBox;

    private Boolean saveBool = false;
    private Stage stage;
    private DateEvent dateEvent;
    private DateEventFormatter dateEventFormatter = new DateEventFormatter();

    @FXML
    public void initialize() {
        dateEventFormatter.formatDatePicker(startDatePicker);
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
                if (!repeatChoiceBox.isSelected()) {
                    dateEvent.setRecurrence(0);
                } else {
                    dateEvent.setRecurrence(dailyBtn.isSelected() ? 1 : weeklyBtn.isSelected() ? 2 : 3);
                }
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
    private void onRepeat() {
        recurBox.setVisible(repeatChoiceBox.isSelected());
    }

    private void popDialog(AlertType alertType, String title, String message) {
        Alert alertBox = new Alert(alertType);
        alertBox.setHeaderText(null);
        alertBox.setTitle(title);
        alertBox.setContentText(message);
        alertBox.showAndWait();
    }

    private int getPriorityFromButton() {
        return highBtn.isSelected() ? 1 : normalBtn.isSelected() ? 2 : 3;
    }

    private boolean isValidDate(LocalDate currentDate) {
        return !currentDate.isBefore(LocalDate.now());
    }

    private void closeWindow() {
        stage.close();
    }

    public boolean isSaved() {
        return saveBool;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentEvent(DateEvent currentEvent) {
        dateEvent = currentEvent;
        eventNameTxtF.setText(dateEvent.getEventName());
        eventDescTxtA.setText(dateEvent.getEventDescription());
        startDatePicker.setValue(dateEvent.getEventStartDate());
        int recurrence = dateEvent.getRecurrence();
        if (recurrence > 0) {
            repeatChoiceBox.setSelected(true);
            recurBox.setVisible(true);
            switch (recurrence) {
                case 1:
                    dailyBtn.setSelected(true);
                    break;
                case 2:
                    weeklyBtn.setSelected(true);
                    break;
                case 3:
                    monthlyBtn.setSelected(true);
            }
        } else {
            repeatChoiceBox.setSelected(false);
            recurBox.setVisible(false);
        }
        if (dateEvent.getEventPriority() == 1) {
            highBtn.setSelected(true);
        } else if (dateEvent.getEventPriority() == 2) {
            normalBtn.setSelected(true);
        } else {
            lowBtn.setSelected(true);
        }
    }
}