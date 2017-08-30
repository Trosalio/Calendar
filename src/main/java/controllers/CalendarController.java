package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.DateEvent;
import views.AlertBox;

import java.io.IOException;
import java.time.LocalDate;

public class CalendarController{

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea eventDescTxtA;
    @FXML
    private TextField eventNameTxtF;

    private DateEvent dateEvent;

    @FXML
    public void initialize(){

        dateEvent = new DateEvent();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    @FXML
    private void onSave() throws IOException {
        if (isValidDate(datePicker.getValue())){
            if(!eventNameTxtF.getText().isEmpty()){
                dateEvent.setEventName(eventNameTxtF.getText());
                dateEvent.setEventDate(datePicker.getValue());
                dateEvent.setEventDescription(eventDescTxtA.getText());
                System.out.println("Date Event: " + dateEvent);
                AlertBox.display("Success","Event is saved!");
                closeWindow();
            } else {
                AlertBox.display("Error","Please fill in the name of the event");
            }
        } else {
            AlertBox.display("Error","Date must not be in the past nor left blanked");
            datePicker.setValue(LocalDate.now());
        }

    }

    private boolean isValidDate(LocalDate pickedDate){
        return pickedDate != null && pickedDate.compareTo(LocalDate.now()) >= 0;
    }

    private void closeWindow(){
        System.exit(0);
    }
}
