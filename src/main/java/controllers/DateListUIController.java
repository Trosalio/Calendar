package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.DateEvent;
import models.DateEventFormatter;
import models.EventManager;

import java.time.LocalDate;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class DateListUIController {


    @FXML
    private Label eventNameLbl, eventPriorityLbl, eventDateLbl, recurrenceLbl,
            searchedEventNameLbl, searchedEventPriorityLbl, searchedEventDateLbl, searchedRecurrenceLbl;
    @FXML
    private TextArea eventDescTxtA,
            searchedEventDescTxtA;
    @FXML
    private TableView<DateEvent> eventTable,
            seachedEventTable;
    @FXML
    private TableColumn<DateEvent, String> nameColumn,
            searchedNameColumn;
    @FXML
    private TableColumn<DateEvent, Number> priorityColumn,
            searchedPriorityColumn;
    @FXML
    private TableColumn<DateEvent, LocalDate> dateColumn;
    @FXML
    private Button clearSearchBtn;
    @FXML
    private Tab allEventTab,
            searchedEventTab;
    @FXML
    private DatePicker searchedDatePicker;

    private DateEventFormatter dateEventFormatter = new DateEventFormatter();
    private EventManager eventManager;
    private HBox hBoxState;


    public void deleteEvent() {
        int removeIndex = eventTable.getSelectionModel().getSelectedIndex();
        eventManager.deleteEvent(removeIndex);
        changeButtonsState();
    }

    void modifyEventInfo(DateEvent event) {
        if (event != null) {
            DateEvent currentEvent = eventTable.getSelectionModel().getSelectedItem();
            eventNameLbl.setText(currentEvent.getEventName());
            eventPriorityLbl.setText(convertPriorityToText(currentEvent.getEventPriority()));
            eventDateLbl.setText(dateEventFormatter.getFormatter().format(currentEvent.getEventStartDate()));
            eventDescTxtA.setText(currentEvent.getEventDescription());
            recurrenceLbl.setText(convertRecurrenceBooleanToText(currentEvent));
        }
    }

    private void setupTableView() {
        eventTable.setItems(eventManager.getEvents());
        nameColumn.setCellValueFactory(cell -> cell.getValue().eventNameProperty());
        priorityColumn.setCellValueFactory(cell -> cell.getValue().eventPriorityProperty());
        setPriorityColumnFormat();
        dateColumn.setCellValueFactory(cell -> cell.getValue().eventStartDateProperty());
        dateEventFormatter.formatDateColumn(dateColumn);
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

    private void setupItemListener() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            eventManager.setCurrentEvent(newSelection);
            modifyEventInfo(newSelection);
        });

        eventManager.currentEventProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                eventTable.getSelectionModel().clearSelection();
            } else {
                eventTable.getSelectionModel().select(newSelection);
            }
        });
    }

    void changeButtonsState() {
        if (eventManager.getEvents().isEmpty()) {
            hBoxState.setDisable(true);
            hBoxState.setVisible(false);
            eventNameLbl.setText("<Name>");
            eventPriorityLbl.setText("<Priority>");
            eventDateLbl.setText("<Date>");
            recurrenceLbl.setText("<IsRecurred, [<Recurrences>]>");
            eventDescTxtA.clear();
        } else {
            hBoxState.setDisable(false);
            hBoxState.setVisible(true);
        }
    }

    public void attachHBoxState(HBox hBoxState){
        this.hBoxState = hBoxState;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        setupTableView();
    }

    public boolean isAnyItemSelected(){
        return eventTable.getSelectionModel().getSelectedItem() != null;
    }

    public void clearTablesFocus(){
        eventTable.getSelectionModel().clearSelection();
    }

    public void displayEventOfDate(LocalDate currentDate) {
        System.out.println("Hi");
    }
}
