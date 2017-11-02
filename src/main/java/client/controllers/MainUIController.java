package client.controllers;

import common.CalendarService;
import common.DateEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private CalendarService calendarService;
    private DateListUIController dateListUIController;
    private MonthUIController monthUIController;
    private ObservableList<DateEvent> eventsView;

    @FXML
    private void onAdd() {
        DateEvent event = new DateEvent();
        if (popEventWindow(event)) {
            dateListUIController.addEvent(event);
            calendarService.addEvent(event);
            monthUIController.refreshTable();
        }
    }

    @FXML
    public void onDelete() {
        if (dateListUIController.isAnyItemSelected()) {
            DateEvent removedEvent = dateListUIController.deleteEvent();
            calendarService.deleteEvent(removedEvent);
            monthUIController.refreshTable();
        }
    }

    @FXML
    private void onEdit() {
        DateEvent event = dateListUIController.getCurrentEvent();
        if (event != null) {
            if (popEventWindow(event)) {
                dateListUIController.modifyEventInfo(event);
                calendarService.editEvent(event);
                monthUIController.refreshTable();
            }
        }
    }

    private boolean popEventWindow(DateEvent event) {
        try {
            FXMLLoader eventUILoader = new FXMLLoader(getClass().getResource("/fxml/EventUI.fxml"));
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
        FXMLLoader dateListUILoader = new FXMLLoader(getClass().getResource("/fxml/DateListUI.fxml"));
        Parent dateViewScene = dateListUILoader.load();
        dateListUIController = dateListUILoader.getController();
        dateListUIController.attachHBoxState(stateBox);
        dateListUIController.setEventsView(eventsView);
        dateListUIController.initDateListUI();
        dateListViewTab.setContent(dateViewScene);
    }

    private void createMonthUITab() throws IOException {
        FXMLLoader monthUILoader = new FXMLLoader(getClass().getResource("/fxml/MonthUI.fxml"));
        Parent monthlyViewScene = monthUILoader.load();
        monthUIController = monthUILoader.getController();
        monthUIController.setEventsView(eventsView);
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

    public void setCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
        setEventsView();
    }

    private void setEventsView(){
        this.eventsView = FXCollections.observableList(calendarService.getEvents());
    }

    public void displayEventsOfDate(LocalDate date) {
        dateListUIController.displayEventsOfDate(date);
        tabPane.getSelectionModel().select(dateListViewTab);
    }
}
