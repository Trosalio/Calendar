package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import models.DateEvent;
import models.EventList;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */


public class MainUIController {

    @FXML
    private Button editBtn;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab dateListViewTab, monthlyViewTab;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private EventList eventList;
    private DateListUIController dateListUIController;
    private MonthUIController monthUIController;

    @FXML
    public void initialize() {
        try {
            FXMLLoader Loader = new FXMLLoader();
            Loader.setLocation(getClass().getResource("../DateListUI.fxml"));
            Parent dateViewScene = Loader.load();
            dateListUIController = Loader.getController();
            dateListUIController.setEditBtn(editBtn);
            dateListUIController.setDateTimeFormatter(dateTimeFormatter);
            dateListViewTab.setContent(dateViewScene);
            tabPane.getSelectionModel().select(dateListViewTab);
            FXMLLoader Loader2 = new FXMLLoader();
            Loader2.setLocation(getClass().getResource("../MonthUI.fxml"));
            Parent monthlyViewScene = Loader2.load();
            monthUIController = Loader2.getController();
            monthlyViewTab.setContent(monthlyViewScene);
            setUpTabListener();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onAdd() {
        DateEvent event = new DateEvent();
        if (popEventWindow(event)) {
            eventList.addEvent(event);
            dateListUIController.changeButtonsState();
        }
    }

    @FXML
    private void onEdit() {
        DateEvent event = eventList.getCurrentEvent();
        if (event != null) {
            if (popEventWindow(event)) {
                eventList.editEvent(event);
                dateListUIController.modifyEventInfo(event);
            }
        }
    }

    private void setUpTabListener(){
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            oldValue.getContent().setVisible(false);
            newValue.getContent().setVisible(true);
        });
    }
    private boolean popEventWindow(DateEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../EventUI.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Event");
            stage.setResizable(false);

            EventUIController eventController = loader.getController();
            eventController.setCurrentEvent(event);
            eventController.setDateTimeFormatter(dateTimeFormatter);
            eventController.setStage(stage);

            stage.showAndWait();
            return eventController.isSaved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setEventList(EventList eventList) {
        this.eventList = eventList;
        this.dateListUIController.setEventList(eventList);
        this.monthUIController.setEventList(eventList);
    }

}
