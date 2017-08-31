package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.DateEvent;
import models.EventListSingleton;

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
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;

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
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onSave() {
        if (isValidDate(datePicker.getValue())) {
            if (!eventNameTxtF.getText().isEmpty()) {
                dateEvent.setEventName(eventNameTxtF.getText());
                dateEvent.setEventDate(datePicker.getValue());
                dateEvent.setEventDescription(eventDescTxtA.getText());
                EventListSingleton.getInstance().addEventList(dateEvent);
                displayAlertBox(Alert.AlertType.INFORMATION, "Success", "Event is saved!");
                Stage stage = (Stage) saveBtn.getScene().getWindow();
                stage.close();
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
}