package server;

import common.CalendarService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import common.DateEvent;
import server.persistences.DBManager;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class CalendarServiceImp implements CalendarService {

    private final ObservableList<DateEvent> events = FXCollections.observableArrayList();
    private final ObjectProperty<DateEvent> currentEvent = new SimpleObjectProperty<>(null);

    private DBManager dataSource;

    public void addEvent(DateEvent event) {
        DateEvent.setPrimaryKeyID(DateEvent.getPrimaryKeyID() + 1);
        event.setID(DateEvent.getPrimaryKeyID());
        events.add(event);
        if (dataSource != null) dataSource.insertEventRecord(event);
    }

    public void deleteEvent(int removeIndex) {
        DateEvent removedEvent = events.get(removeIndex);
        events.remove(removeIndex);
        if (dataSource != null) dataSource.deleteEventRecord(removedEvent.getID(), removedEvent.isRecurred());
    }

    public void editEvent(DateEvent event) {
        if (dataSource != null) dataSource.modifyEventRecord(event);
    }

    public DateEvent getCurrentEvent() {
        return currentEvent.get();
    }

    public void setCurrentEvent(DateEvent currentEvent) {
        this.currentEvent.set(currentEvent);
    }

    public ObjectProperty<DateEvent> currentEventProperty() {
        return currentEvent;
    }

    public ObservableList<DateEvent> getEvents() {
        return events;
    }

    public void setDataSource(DBManager dataSource) {
        this.dataSource = dataSource;
    }
}
