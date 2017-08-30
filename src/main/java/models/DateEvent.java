package models;

import java.time.LocalDate;

/**
 * Thanapong Supalak 5810405029
 */

public class DateEvent {

    private String eventName;
    private LocalDate eventDate;
    private String eventDescription;

    public DateEvent() {
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }


    @Override
    public String toString() {
        return "DateEvent{" +
                "eventName='" + eventName + '\'' +
                ", eventDate=" + eventDate +
                ", eventDescription='" + eventDescription + '\'' +
                '}';
    }


}
