package common;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class DateEvent implements Serializable {

    private String[] recurrenceType = {"None", "Daily", "Weekly", "Monthly"};
    private static int primaryKeyID;
    private int ID;
    private String eventName;
    private LocalDate eventStartDate;
    private int eventPriority;
    private String eventDescription;
    private int recurrence;

    public DateEvent() {
        eventName = "";
        eventPriority = 1;
        eventStartDate = LocalDate.now();
        eventDescription = "";
        recurrence = 0;
    }

    public static int getPrimaryKeyID() {
        return primaryKeyID;
    }

    public static void setPrimaryKeyID(int primaryKeyID) {
        DateEvent.primaryKeyID = primaryKeyID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(LocalDate eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public int getEventPriority() {
        return eventPriority;
    }

    public void setEventPriority(int eventPriority) {
        this.eventPriority = eventPriority;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public int getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(int recurrence) {
        this.recurrence = recurrence;
    }

    public boolean isEventOccurredAtDate(LocalDate askedDate) {
        if (recurrence > 0) {
            switch (recurrence) {
                case 1: // daily case
                    return true;
                case 2: // weekly case
                    return askedDate.getDayOfWeek().equals(eventStartDate.getDayOfWeek());
                case 3: // monthly case
                    return askedDate.getDayOfMonth() == eventStartDate.getDayOfMonth();
            }
        } else { // one time event
            return askedDate.isEqual(eventStartDate);
        }
        return false;
    }

    public String[] getRecurrenceType() {
        return recurrenceType;
    }
}

