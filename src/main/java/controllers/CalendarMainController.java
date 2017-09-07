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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created By:
 * -Name:   Thanapong Supalak
 * -ID:     5810405029
 * Project Name: Calendar
 */


public class CalendarMainController {

    @FXML
    private Button addBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button editBtn;
    @FXML
    private TextField nameTxtF;
    @FXML
    private TextArea descTxtA;
    @FXML
    private TextField dateTxtF;
    @FXML
    private TableView<DateEvent> eventTable;
    @FXML
    private TableColumn<DateEvent, LocalDate> dateColumn;
    @FXML
    private TableColumn<DateEvent, String> nameColumn;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private EventList eventList;

    @FXML
    private void onAdd() {
        DateEvent event = new DateEvent();
        if (popEventWindow(event)) {
            eventList.addEvent(event);
            changeButtonsState();
        }
    }

    @FXML
    private void onDelete() {
        int removeIndex = eventTable.getSelectionModel().getSelectedIndex();
        eventList.deleteEvent(removeIndex);
        changeButtonsState();
    }

    @FXML
    private void onEdit() {
        DateEvent event = eventList.getCurrentEvent();
        if (event != null) {
            if (popEventWindow(event)) {
                eventList.editEvent(event);
                modifyEventInfo(event);
            }
        }
    }

    private boolean popEventWindow(DateEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/CalendarEventUI.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Event");
            stage.setResizable(false);

            CalendarEventController eventController = loader.getController();
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

    private void modifyEventInfo(DateEvent event) {
        if (event != null) {
            String name = eventTable.getSelectionModel().getSelectedItem().getEventName();
            LocalDate date = eventTable.getSelectionModel().getSelectedItem().getEventDate();
            String description = eventTable.getSelectionModel().getSelectedItem().getEventDescription();
            nameTxtF.setText(name);
            dateTxtF.setText(dateTimeFormatter.format(date));
            descTxtA.setText(description);
        }
    }

    private void setupTableView() {
        eventTable.setItems(eventList.getEventList());
        nameColumn.setCellValueFactory(cell -> cell.getValue().eventNameProperty());
        dateColumn.setCellValueFactory(cell -> cell.getValue().eventDateProperty());
        setDateColumnFormat();
        setupItemListener();
        changeButtonsState();
    }

    private void setDateColumnFormat() {
        dateColumn.setCellFactory(cell -> new TableCell<DateEvent, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(dateTimeFormatter.format(item));
            }
        });
    }

    private void setupItemListener() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            eventList.setCurrentEvent(newSelection);
            modifyEventInfo(newSelection);
        });

        eventList.currentEventProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                eventTable.getSelectionModel().clearSelection();
            } else {
                eventTable.getSelectionModel().select(newSelection);
            }
        });
    }

    public void setEventList(EventList eventList) {
        this.eventList = eventList;
        setupTableView();
    }

    private void changeButtonsState() {
        if (eventList.getEventList().isEmpty()) {
            deleteBtn.setDisable(true);
            editBtn.setDisable(true);
            editBtn.setVisible(false);
            nameTxtF.clear();
            dateTxtF.clear();
            descTxtA.clear();
        } else {
            deleteBtn.setDisable(false);
            editBtn.setDisable(false);
            editBtn.setVisible(true);
        }
    }
}
