package common;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public interface CalendarService {
    void addEvent(DateEvent event);

    void deleteEvent(int removeIndex);

    void editEvent(DateEvent event);

    DateEvent getCurrentEvent();

    ObservableList<DateEvent> getEvents();

    void setCurrentEvent(DateEvent newSelection);

    ObjectProperty<DateEvent> currentEventProperty();
}
