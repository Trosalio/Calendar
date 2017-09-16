package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class EventList {

    private final ObservableList<DateEvent> events = FXCollections.observableArrayList();
    private final ObjectProperty<DateEvent> currentEvent = new SimpleObjectProperty<>(null);

    private DBManager dbManager;

    public void addEvent(DateEvent event) {
        DateEvent.setPrimaryKeyID(DateEvent.getPrimaryKeyID() + 1);
        event.setID(DateEvent.getPrimaryKeyID());
        events.add(event);
        if (dbManager != null) dbManager.insertEventRecord(event);
    }

    public void deleteEvent(int removeIndex) {
        int removedDateIDKey = events.get(removeIndex).getID();
        events.remove(removeIndex);
        if (dbManager != null) dbManager.deleteEventRecord(removedDateIDKey);
    }

    public void editEvent(DateEvent event) {
        if (dbManager != null) dbManager.modifyEventRecord(event);
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

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public ObservableList<DateEvent> getEvents() {
        return events;
    }
}
