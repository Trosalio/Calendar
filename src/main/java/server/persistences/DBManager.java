package server.persistences;

import common.DateEvent;
import common.DateEventFormatter;
import server.CalendarServiceImp;

import java.time.format.DateTimeFormatter;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class DBManager {

    private DateTimeFormatter dateTimeFormatter;
    private CalendarServiceImp serviceImp;
    private DBConnector databaseConnector;

    public DBManager(CalendarServiceImp serviceImp) {
        this.serviceImp = serviceImp;
        this.serviceImp.setDataSource(this);
        this.dateTimeFormatter = new DateEventFormatter().getFormatter();
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
        databaseConnector.loadItemsFromDatabase(serviceImp.getEvents());
    }

    public void setDatabaseConnector(DBConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }
}
