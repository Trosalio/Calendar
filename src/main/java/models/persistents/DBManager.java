package models.persistents;

import javafx.collections.ObservableList;
import models.DateEvent;
import models.DateEventFormatter;
import models.EventManager;

import java.time.format.DateTimeFormatter;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class DBManager {

    private DateTimeFormatter dateTimeFormatter;
    private EventManager eventManager;
    private DBConnector databaseConnector;
    private ObservableList<DateEvent> eventList;

    public DBManager(EventManager eventManager) {
        this.eventManager = eventManager;
        this.eventManager.setDbManager(this);
        this.dateTimeFormatter = new DateEventFormatter().getFormatter();
        this.eventList = eventManager.getEvents();
    }

    public void insertEventRecord(DateEvent event) {
        String eventName = event.getEventName();
        int eventPriority = event.getEventPriority();
        String eventStartDateDate = dateTimeFormatter.format(event.getEventStartDate());
        String eventDescription = event.getEventDescription();
        boolean isRecurred = event.isRecurred();
        boolean isRepeatMonth = event.isRepeatMonth();
        boolean isRepeatWeek = event.isRepeatWeek();
        boolean isRepeatDay = event.isRepeatDay();
        databaseConnector.insertItemToDatabase(eventName, eventPriority, eventStartDateDate, eventDescription,
                isRecurred, isRepeatMonth, isRepeatWeek, isRepeatDay);
    }

    public void deleteEventRecord(int ID, boolean isRecurred) {
        databaseConnector.deleteItemInDatabase(ID, isRecurred);
    }

    public void modifyEventRecord(DateEvent event) {
        String eventName = event.getEventName();
        int eventPriority = event.getEventPriority();
        String eventStartDateDate = dateTimeFormatter.format(event.getEventStartDate());
        String eventDescription = event.getEventDescription();
        int eventID = event.getID();
        boolean isRecurred = event.isRecurred();
        boolean isRepeatMonth = event.isRepeatMonth();
        boolean isRepeatWeek = event.isRepeatWeek();
        boolean isRepeatDay = event.isRepeatDay();
        databaseConnector.modifyItemInDatabase(eventName, eventPriority, eventStartDateDate, eventDescription, eventID,
                isRecurred, isRepeatMonth, isRepeatWeek, isRepeatDay);
    }

    public void loadDatabase() {
        databaseConnector.loadItemsFromDatabase(eventList);
    }


    public void setDatabaseConnector(DBConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }
}
