package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import models.DateEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    private DateEvent dateEvent;

    @FXML
    public void initialize() {
        dateEvent = new DateEvent();

        datePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd MMMM YYYY";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    @FXML
    private void onSave() throws IOException {
        if (isValidDate(datePicker.getValue())) {
            if (!eventNameTxtF.getText().isEmpty()) {
                dateEvent.setEventName(eventNameTxtF.getText());
                dateEvent.setEventDate(datePicker.getValue());
                dateEvent.setEventDescription(eventDescTxtA.getText());
                System.out.println("Date Event: " + dateEvent);
                displayAlertBox(Alert.AlertType.INFORMATION, "Success", "Event is saved!");
                closeWindow();
            } else {
                displayAlertBox(Alert.AlertType.ERROR, "Error", "Please fill in the name of the event");
            }
        } else {
            displayAlertBox(Alert.AlertType.ERROR, "Error", "Date must not be in the past");
            datePicker.setValue(LocalDate.now());
        }

    }

    private void displayAlertBox(Alert.AlertType alertType, String title, String message) {
        Alert alertBox = new Alert(alertType);
        alertBox.setHeaderText(null);
        alertBox.setTitle(title);
        alertBox.setContentText(message);
        alertBox.showAndWait();
    }

    private boolean isValidDate(LocalDate pickedDate) {
        return pickedDate.compareTo(LocalDate.now()) >= 0;
    }

    private void closeWindow() {
        System.exit(0);
    }
}