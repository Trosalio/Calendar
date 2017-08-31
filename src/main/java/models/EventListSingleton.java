package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Thanapong Supalak 5810405029
 */

public class EventListSingleton {
    private static EventListSingleton ourInstance = new EventListSingleton();

    public static EventListSingleton getInstance() {
        return ourInstance == null ? ourInstance = new EventListSingleton() : ourInstance;
    }

    private ObservableList<DateEvent> eventList;

    private EventListSingleton() {
        eventList = FXCollections.observableArrayList();
    }

    public ObservableList<DateEvent> getEventList() {
        return eventList;
    }

    public void addEventList(DateEvent event) {
        eventList.add(event);
    }
}
