package models;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class DateEvent {

    private static int primaryKeyID;

    private int ID;
    private SimpleStringProperty eventName = new SimpleStringProperty(this, "eventName");
    private ObjectProperty<LocalDate> eventStartDate = new SimpleObjectProperty<>(this, "eventStartDate");
    private SimpleIntegerProperty eventPriority = new SimpleIntegerProperty(this, "eventPriority");
    private String eventDescription;
    private boolean recurred;
    private int repeatInterval;
    private boolean repeatMonth;
    private boolean repeatWeek;
    private boolean repeatDay;

    public DateEvent(){
        setEventName("");
        setEventPriority(1);
        setEventStartDate(LocalDate.now());
        eventDescription = "";
        repeatInterval = 1;
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
        return eventName.get();
    }

    public SimpleStringProperty eventNameProperty() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName.set(eventName);
    }

    public LocalDate getEventStartDate() {
        return eventStartDate.get();
    }

    public ObjectProperty<LocalDate> eventStartDateProperty() {
        return eventStartDate;
    }

    public void setEventStartDate(LocalDate eventStartDate) {
        this.eventStartDate.set(eventStartDate);
    }

    public int getEventPriority() {
        return eventPriority.get();
    }

    public SimpleIntegerProperty eventPriorityProperty() {
        return eventPriority;
    }

    public void setEventPriority(int eventPriority) {
        this.eventPriority.set(eventPriority);
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

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
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
}

