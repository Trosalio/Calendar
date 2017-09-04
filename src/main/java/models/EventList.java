package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Thanapong Supalak 5810405029
 */

public class EventList {

    private final ObservableList<DateEvent> eventList = FXCollections.observableArrayList();
    private final ObjectProperty<DateEvent> currentEvent = new SimpleObjectProperty<>(null);

    public ObservableList<DateEvent> getEventList() {
        return eventList;
    }

    public void addEventList(DateEvent event) {
        eventList.add(event);
    }

    public void removeEvent(int removeIndex) {
        eventList.remove(removeIndex);
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
}
