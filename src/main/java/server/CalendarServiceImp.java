package server;

import common.CalendarService;
import common.DateEvent;
import server.persistences.DBManager;

import java.util.ArrayList;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class CalendarServiceImp implements CalendarService {

    private final ArrayList<DateEvent> events = new ArrayList<>();

    private DBManager dataSource;

    public void addEvent(DateEvent addedEvent) {
        if (dataSource != null) dataSource.insertEventRecord(addedEvent);
    }

    public void deleteEvent(DateEvent removedEvent) {
        if (dataSource != null) dataSource.deleteEventRecord(removedEvent.getID(), removedEvent.isRecurred());
    }

    public void editEvent(DateEvent event) {
        if (dataSource != null) dataSource.modifyEventRecord(event);
    }

    public ArrayList<DateEvent> getEvents() {
        return events;
    }

    public void setDataSource(DBManager dataSource) {
        this.dataSource = dataSource;
    }
}
