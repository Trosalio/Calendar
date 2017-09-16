package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.DateEvent;
import models.EventList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */
public class DateListUIController {

    @FXML
    private Button deleteBtn;
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

    private DateTimeFormatter dateTimeFormatter;
    private EventList eventList;
    private Button editBtn;

    @FXML
    public void onDelete() {
        int removeIndex = eventTable.getSelectionModel().getSelectedIndex();
        eventList.deleteEvent(removeIndex);
        changeButtonsState();
    }

    void modifyEventInfo(DateEvent event) {
        if (event != null) {
            DateEvent currentEvent = eventTable.getSelectionModel().getSelectedItem();
            eventNameLbl.setText(currentEvent.getEventName());
            eventPriorityLbl.setText(convertPriorityToText(currentEvent.getEventPriority()));
            eventDateLbl.setText(dateTimeFormatter.format(currentEvent.getEventStartDate()));
            eventDescTxtA.setText(currentEvent.getEventDescription());
            recurrenceLbl.setText(convertRecurrenceBooleanToText(currentEvent));
        }
    }

    private void setupTableView() {
        eventTable.setItems(eventList.getEvents());
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

    private String convertRecurrenceBooleanToText(DateEvent currentEvent) {
        String reply = "No";
        if (currentEvent.isRecurred()) {
            reply = "Yes, ";
            reply += currentEvent.isRepeatMonth() ? "Monthly" : currentEvent.isRepeatWeek() ? "Weekly" : "Daily";
        }
        return reply;
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

    void changeButtonsState() {
        if (eventList.getEvents().isEmpty()) {
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

    void setEventList(EventList eventList) {
        this.eventList = eventList;
        setupTableView();
    }

    void setEditBtn(Button editBtn) {
        this.editBtn = editBtn;
    }

    void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }
}
