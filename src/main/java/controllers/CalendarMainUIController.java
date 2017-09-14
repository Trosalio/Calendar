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
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */


public class CalendarMainUIController {

    @FXML
    private Button deleteBtn, editBtn;
    @FXML
    private Label eventNameLbl, eventPriorityLbl, eventDateLbl, recurrenceLbl;
    @FXML
    private TextArea eventDescTxtA;
    @FXML
    private TableView<DateEvent> eventTable;
    @FXML
    private TableColumn<DateEvent, String> nameColumn;
    @FXML
    private TableColumn<DateEvent, Number> priorityColumn;
    @FXML
    private TableColumn<DateEvent, LocalDate> dateColumn;

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

            CalendarEventUIController eventController = loader.getController();
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
            int priority = eventTable.getSelectionModel().getSelectedItem().getEventPriority();
            LocalDate date = eventTable.getSelectionModel().getSelectedItem().getEventStartDate();
            String description = eventTable.getSelectionModel().getSelectedItem().getEventDescription();
            Boolean isRecurred = eventTable.getSelectionModel().getSelectedItem().isRecurred();
            eventNameLbl.setText(name);
            eventPriorityLbl.setText(convertPriorityToText(priority));
            eventDateLbl.setText(dateTimeFormatter.format(date));
            eventDescTxtA.setText(description);
            recurrenceLbl.setText(convertRecurrenceBooleanToText(isRecurred));
        }
    }

    private void setupTableView() {
        eventTable.setItems(eventList.getEventList());
        nameColumn.setCellValueFactory(cell -> cell.getValue().eventNameProperty());
        priorityColumn.setCellValueFactory(cell -> cell.getValue().eventPriorityProperty());
        setPriorityColumnFormat();
        dateColumn.setCellValueFactory(cell -> cell.getValue().eventStartDateProperty());
        setDateColumnFormat();
        setupItemListener();
        changeButtonsState();
    }

    private void setPriorityColumnFormat() {
        priorityColumn.setCellFactory(cell -> new TableCell<DateEvent, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(convertPriorityToText(item));
            }
        });
    }

    private String convertPriorityToText(Number priority) {
        return priority.equals(1) ? "High" : priority.equals(2) ? "Normal" : "Low";
    }

    private String convertRecurrenceBooleanToText(Boolean isRecurred){
        return isRecurred ? "Yes" : "No";
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
            eventNameLbl.setText("<Name>");
            eventPriorityLbl.setText("<Priority>");
            eventDateLbl.setText("<Date>");
            recurrenceLbl.setText("<IsRecurred, [<Recurrences>]>");
            eventDescTxtA.clear();
        } else {
            deleteBtn.setDisable(false);
            editBtn.setDisable(false);
            editBtn.setVisible(true);
        }
    }
}
