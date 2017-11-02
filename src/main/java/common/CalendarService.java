package common;

import java.util.ArrayList;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public interface CalendarService {
    void addEvent(DateEvent addedEvent);

    void deleteEvent(DateEvent removedEvent);

    void editEvent(DateEvent event);

    ArrayList<DateEvent> getEvents();
}
