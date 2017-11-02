package client.controllers;

import common.DateEvent;
import common.DateEventFormatter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

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
            searchedEventTable;
    @FXML
    private TableColumn<DateEvent, String> nameColumn,
            searchedNameColumn;
    @FXML
    private TableColumn<DateEvent, Number> priorityColumn,
            searchedPriorityColumn;
    @FXML
    private TableColumn<DateEvent, LocalDate> dateColumn;
    @FXML
    private TabPane dateListTabPane;
    @FXML
    private Tab allEventTab,
            searchedEventTab;
    @FXML
    private DatePicker searchedDatePicker;

    private DateEventFormatter dateEventFormatter = new DateEventFormatter();

    private HBox hBoxState;

    private ObjectProperty<DateEvent> currentEvent = new SimpleObjectProperty<>(null);
    private ObservableList<DateEvent> eventsView;
    private FilteredList<DateEvent> filteredDateEvent;
    private SortedList<DateEvent> searchedList;

    @FXML
    private void onClearSearch() {
        searchedDatePicker.setValue(null);
        changeButtonsState();
    }

    @FXML
    private void onDatePicked() {
        displayEvents(searchedDatePicker.getValue());
    }

    public void addEvent(DateEvent event) {
        DateEvent.setPrimaryKeyID(DateEvent.getPrimaryKeyID() + 1);
        int eventID = DateEvent.getPrimaryKeyID();
        event.setID(eventID);
        eventsView.add(event);
        changeButtonsState();
    }

    public DateEvent deleteEvent() {
        int removedIndex = eventTable.getSelectionModel().getSelectedIndex();
        DateEvent removedEvent = eventsView.remove(removedIndex);
        changeButtonsState();
        return removedEvent;
    }

    public void modifyEventInfo(DateEvent event) {
        DateEvent currentEvent = eventTable.getSelectionModel().getSelectedItem();
        if (event != null && currentEvent.equals(event)) {
            eventNameLbl.setText(currentEvent.getEventName());
            eventPriorityLbl.setText(convertPriorityToText(currentEvent.getEventPriority()));
            eventDateLbl.setText(dateEventFormatter.getFormatter().format(currentEvent.getEventStartDate()));
            eventDescTxtA.setText(currentEvent.getEventDescription());
            recurrenceLbl.setText(convertRecurrenceBooleanToText(currentEvent));

            searchedEventNameLbl.setText(currentEvent.getEventName());
            searchedEventPriorityLbl.setText(convertPriorityToText(currentEvent.getEventPriority()));
            searchedEventDateLbl.setText(dateEventFormatter.getFormatter().format(currentEvent.getEventStartDate()));
            searchedEventDescTxtA.setText(currentEvent.getEventDescription());
            searchedRecurrenceLbl.setText(convertRecurrenceBooleanToText(currentEvent));
        }
    }

    private void setupTableView() {
        eventTable.setItems(eventsView);
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEventName()));
        priorityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEventPriority()));
        setPriorityColumnFormatOf(priorityColumn);
        dateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEventStartDate()));
        dateEventFormatter.formatDateColumn(dateColumn);
        setupItemListenerOf(eventTable);
    }

    private void setupSearchedTableView() {
        filteredDateEvent = new FilteredList<>(eventsView, dateEvent -> true);
        searchedList = new SortedList<>(filteredDateEvent);
        searchedEventTable.setItems(searchedList);
        searchedNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEventName()));
        searchedPriorityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEventPriority()));
        setPriorityColumnFormatOf(searchedPriorityColumn);
        setupItemListenerOf(searchedEventTable);
    }

    private void setPriorityColumnFormatOf(TableColumn<DateEvent, Number> column) {
        column.setCellFactory(cell -> new TableCell<>() {
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

    private void setupItemListenerOf(TableView<DateEvent> tableView) {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            currentEvent.set(newSelection);
            modifyEventInfo(newSelection);
        });

        currentEvent.addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                tableView.getSelectionModel().clearSelection();
            } else {
                tableView.getSelectionModel().select(newSelection);
            }
        });
    }

    private void setUpTabListener() {
        dateListTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> swapTablesFocus(newValue));
    }

    private void swapTablesFocus(Tab newTab) {
        if (newTab.equals(allEventTab)) {
            searchedEventTable.getSelectionModel().clearSelection();
            if (!eventTable.getSelectionModel().isEmpty()) {
                eventTable.getSelectionModel().select(0);
            }
        } else if (newTab.equals(searchedEventTab)) {
            eventTable.getSelectionModel().clearSelection();
            if (!searchedEventTable.getSelectionModel().isEmpty()) {
                searchedEventTable.getSelectionModel().select(0);
            }
        }
    }

    public void changeButtonsState() {
        if (eventsView.isEmpty()) {
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
        if (searchedList.isEmpty()) {
            searchedEventNameLbl.setText("<Name>");
            searchedEventPriorityLbl.setText("<Priority>");
            searchedEventDateLbl.setText("<Date>");
            searchedRecurrenceLbl.setText("<IsRecurred, [<Recurrences>]>");
            searchedEventDescTxtA.clear();
        }
    }

    public void setEventsView(ObservableList<DateEvent> eventsView) {
        this.eventsView = eventsView;
    }

    public DateEvent getCurrentEvent() {
        return currentEvent.get();
    }

    public void attachHBoxState(HBox hBoxState) {
        this.hBoxState = hBoxState;
    }

    public void initDateListUI() {
        initAllEventTab();
        initSearchEventTab();
        dateEventFormatter.formatDatePicker(searchedDatePicker);
        changeButtonsState();
        dateListTabPane.getSelectionModel().select(allEventTab);
        setUpTabListener();
    }

    private void initAllEventTab() {
        setupTableView();
    }

    private void initSearchEventTab() {
        setupSearchedTableView();
    }

    public boolean isAnyItemSelected() {
        return eventTable.getSelectionModel().getSelectedItem() != null || searchedEventTable.getSelectionModel().getSelectedItem() != null;
    }

    public void clearTablesFocus() {
        eventTable.getSelectionModel().clearSelection();
        searchedEventTable.getSelectionModel().clearSelection();
    }

    private void displayEvents(LocalDate date) {
        filteredDateEvent.setPredicate(dateEvent -> (date != null) && dateEvent.isEventOccurredAtDate(date));
        dateListTabPane.getSelectionModel().select(searchedEventTab);
    }

    public void displayEventsOfDate(LocalDate currentDate) {
        searchedDatePicker.setValue(currentDate);
    }
}
