package controllers;


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
    private DateListUIController dateListUIController = new DateListUIController();
    private MonthUIController monthUIController = new MonthUIController();

    @FXML
    public void initialize() {
        try {
            FXMLLoader dateListUILoader = new FXMLLoader();
            dateListUILoader.setLocation(getClass().getResource("/DateListUI.fxml"));
            dateListUILoader.setController(dateListUIController);
            Parent dateViewScene = dateListUILoader.load();
            dateListUIController.setEditBtn(editBtn);
            dateListUIController.setDateTimeFormatter(dateTimeFormatter);
            dateListViewTab.setContent(dateViewScene);


            FXMLLoader monthUILoader = new FXMLLoader();
            monthUILoader.setLocation(getClass().getResource("/MonthUI.fxml"));
            monthUILoader.setController(monthUIController);
            Parent monthlyViewScene = monthUILoader.load();
            monthlyViewTab.setContent(monthlyViewScene);

            tabPane.getSelectionModel().select(dateListViewTab);
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
            if(newValue.equals(monthlyViewTab)){
                monthUIController.refreshTable();
            }
        });
    }
    private boolean popEventWindow(DateEvent event) {
        try {
            EventUIController eventController = new EventUIController();
            FXMLLoader eventUILoader = new FXMLLoader();
            eventUILoader.setLocation(getClass().getResource("/EventUI.fxml"));
            eventUILoader.setController(eventController);
            Parent root = eventUILoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Event");
            stage.setResizable(false);


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
