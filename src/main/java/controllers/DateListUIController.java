package controllers;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
    private EventManager eventManager;
    private HBox hBoxState;
    private FilteredList<DateEvent> filteredDateEvent;
    private SortedList<DateEvent> searchedList;
//    might come in handy in the future
//    private MainUIController mainUIController;

    @FXML
    private void onClearSearch() {
        searchedDatePicker.setValue(null);
        changeButtonsState();
    }

    @FXML
    private void onDatePicked() {
        displayEvents(searchedDatePicker.getValue());
    }

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

            searchedEventNameLbl.setText(currentEvent.getEventName());
            searchedEventPriorityLbl.setText(convertPriorityToText(currentEvent.getEventPriority()));
            searchedEventDateLbl.setText(dateEventFormatter.getFormatter().format(currentEvent.getEventStartDate()));
            searchedEventDescTxtA.setText(currentEvent.getEventDescription());
            searchedRecurrenceLbl.setText(convertRecurrenceBooleanToText(currentEvent));
        }
    }

    private void setupTableView() {
        eventTable.setItems(eventManager.getEvents());
        nameColumn.setCellValueFactory(cell -> cell.getValue().eventNameProperty());
        priorityColumn.setCellValueFactory(cell -> cell.getValue().eventPriorityProperty());
        setPriorityColumnFormatOf(priorityColumn);
        dateColumn.setCellValueFactory(cell -> cell.getValue().eventStartDateProperty());
        dateEventFormatter.formatDateColumn(dateColumn);
        setupItemListenerOf(eventTable);
    }

    private void setupSearchedTableView() {
        filteredDateEvent = new FilteredList<>(eventManager.getEvents(), dateEvent -> true);
        searchedList = new SortedList<>(filteredDateEvent);
        searchedEventTable.setItems(searchedList);
        searchedNameColumn.setCellValueFactory(cell -> cell.getValue().eventNameProperty());
        searchedPriorityColumn.setCellValueFactory(cell -> cell.getValue().eventPriorityProperty());
        setPriorityColumnFormatOf(searchedPriorityColumn);
        setupItemListenerOf(searchedEventTable);
    }

    private void setPriorityColumnFormatOf(TableColumn<DateEvent, Number> column) {
        column.setCellFactory(cell -> new TableCell<DateEvent, Number>() {
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
            eventManager.setCurrentEvent(newSelection);
            modifyEventInfo(newSelection);
        });

        eventManager.currentEventProperty().addListener((obs, oldSelection, newSelection) -> {
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
        if (searchedList.isEmpty()) {
            searchedEventNameLbl.setText("<Name>");
            searchedEventPriorityLbl.setText("<Priority>");
            searchedEventDateLbl.setText("<Date>");
            searchedRecurrenceLbl.setText("<IsRecurred, [<Recurrences>]>");
            searchedEventDescTxtA.clear();
        }
    }

    public void attachHBoxState(HBox hBoxState) {
        this.hBoxState = hBoxState;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
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

//    Might come handy in the future
//    public void bindMainUIController(MainUIController mainUIController) {
//        this.mainUIController = mainUIController;
//    }
}
