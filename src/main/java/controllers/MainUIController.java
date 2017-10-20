package controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.DateEvent;
import models.EventManager;

import java.io.IOException;
import java.time.LocalDate;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */


public class MainUIController {

    @FXML
    private HBox stateBox;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab dateListViewTab, monthlyViewTab;
    private EventManager eventManager;
    private DateListUIController dateListUIController;
    private MonthUIController monthUIController;

    @FXML
    private void onAdd() {
        DateEvent event = new DateEvent();
        if (popEventWindow(event)) {
            eventManager.addEvent(event);
            dateListUIController.changeButtonsState();
            monthUIController.refreshTable();
        }
    }

    @FXML
    public void onDelete() {
        if (dateListUIController.isAnyItemSelected()) {
            dateListUIController.deleteEvent();
            dateListUIController.changeButtonsState();
            monthUIController.refreshTable();
        }
    }

    @FXML
    private void onEdit() {
        DateEvent event = eventManager.getCurrentEvent();
        if (event != null) {
            if (popEventWindow(event)) {
                eventManager.editEvent(event);
                dateListUIController.modifyEventInfo(event);
                monthUIController.refreshTable();
            }
        }
    }

    private boolean popEventWindow(DateEvent event) {
        try {
            FXMLLoader eventUILoader = new FXMLLoader(getClass().getResource("/EventUI.fxml"));
            Parent root = eventUILoader.load();
            EventUIController eventController = eventUILoader.getController();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Event");
            stage.setResizable(false);

            eventController.setCurrentEvent(event);
            eventController.setStage(stage);

            stage.showAndWait();
            return eventController.isSaved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void initUITabs() {
        try {
            createDateListUITab();
            createMonthUITab();
            bindControllers();
            tabPane.getSelectionModel().select(monthlyViewTab);
            setUpTabListener();
        } catch (IOException e) {
            System.err.println("Exception Caught: " + e);
            System.err.println("Possibility: Tabs aren't initialize properly");
        }
    }

    private void bindControllers() {
//        Might come handy in the future
//        dateListUIController.bindMainUIController(this);
        monthUIController.bindMainUIController(this);
    }

    private void createDateListUITab() throws IOException {
        FXMLLoader dateListUILoader = new FXMLLoader(getClass().getResource("/DateListUI.fxml"));
        Parent dateViewScene = dateListUILoader.load();
        dateListUIController = dateListUILoader.getController();
        dateListUIController.attachHBoxState(stateBox);
        dateListUIController.setEventManager(eventManager);
        dateListUIController.initDateListUI();
        dateListViewTab.setContent(dateViewScene);
    }

    private void createMonthUITab() throws IOException {
        FXMLLoader monthUILoader = new FXMLLoader(getClass().getResource("/MonthUI.fxml"));
        Parent monthlyViewScene = monthUILoader.load();
        monthUIController = monthUILoader.getController();
        monthUIController.setEventManager(eventManager);
        monthlyViewTab.setContent(monthlyViewScene);
    }

    private void setUpTabListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(monthlyViewTab)) {
                monthUIController.refreshTable();
                dateListUIController.clearTablesFocus();
            }
        });
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;

    }

    public void displayEventsOfDate(LocalDate date) {
        dateListUIController.displayEventsOfDate(date);
        tabPane.getSelectionModel().select(dateListViewTab);
    }
}
