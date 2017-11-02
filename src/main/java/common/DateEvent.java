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

    private static int primaryKeyID;

    private int ID;
    private String eventName;
    private LocalDate eventStartDate;
    private int eventPriority;
    private String eventDescription;
    private boolean recurred;
    private boolean repeatMonth;
    private boolean repeatWeek;
    private boolean repeatDay;

    public DateEvent() {
        eventName ="";
        eventPriority = 1;
        eventStartDate = LocalDate.now();
        eventDescription = "";
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

    public boolean isRecurred() {
        return recurred;
    }

    public void setRecurred(boolean recurred) {
        this.recurred = recurred;
    }

    public boolean isRepeatMonth() {
        return repeatMonth;
    }

    public void setRepeatMonth(boolean repeatMonth) {
        this.repeatMonth = repeatMonth;
    }

    public boolean isRepeatWeek() {
        return repeatWeek;
    }

    public void setRepeatWeek(boolean repeatWeek) {
        this.repeatWeek = repeatWeek;
    }

    public boolean isRepeatDay() {
        return repeatDay;
    }

    public void setRepeatDay(boolean repeatDay) {
        this.repeatDay = repeatDay;
    }

    public void clearRepeatOptions() {
        setRecurred(false);
        repeatDay = false;
        repeatWeek = false;
        repeatMonth = false;
    }

    public boolean isEventOccurredAtDate(LocalDate askedDate) {
        if (isRecurred()) {
            if (isRepeatMonth()) {
                return askedDate.getDayOfMonth() == eventStartDate.getDayOfMonth();
            } else if (isRepeatWeek()) {
                return askedDate.getDayOfWeek().equals(eventStartDate.getDayOfWeek());
            } else {
                return isRepeatDay();
            }
        } else { // one time event
            return askedDate.isEqual(eventStartDate);
        }
    }
}

