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
    private SimpleStringProperty eventDescription = new SimpleStringProperty(this, "eventDescription");
    private SimpleBooleanProperty recurred = new SimpleBooleanProperty(this,"recurred");
    private SimpleIntegerProperty repeatInterval = new SimpleIntegerProperty(this, "repeatInterval");
    private SimpleIntegerProperty repeatMonth = new SimpleIntegerProperty(this, "repeatMonth");
    private SimpleIntegerProperty repeatWeek = new SimpleIntegerProperty(this, "repeatWeek");
    private SimpleIntegerProperty repeatDay = new SimpleIntegerProperty(this, "repeatDay");

    public DateEvent(){
        setEventName("");
        setEventPriority(1);
        setEventStartDate(LocalDate.now());
        setEventDescription("");
        setRecurred(false);
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
        return eventDescription.get();
    }

    public SimpleStringProperty eventDescriptionProperty() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription.set(eventDescription);
    }

    public boolean isRecurred() {
        return recurred.get();
    }

    public SimpleBooleanProperty recurredProperty() {
        return recurred;
    }

    public void setRecurred(boolean recurred) {
        this.recurred.set(recurred);
    }

    public int getRepeatInterval() {
        return repeatInterval.get();
    }

    public SimpleIntegerProperty repeatIntervalProperty() {
        return repeatInterval;
    }

    public void setRepeatInterval(int repeatInterval) {
        this.repeatInterval.set(repeatInterval);
    }

    public int getRepeatMonth() {
        return repeatMonth.get();
    }

    public SimpleIntegerProperty repeatMonthProperty() {
        return repeatMonth;
    }

    public void setRepeatMonth(int repeatMonth) {
        this.repeatMonth.set(repeatMonth);
    }

    public int getRepeatWeek() {
        return repeatWeek.get();
    }

    public SimpleIntegerProperty repeatWeekProperty() {
        return repeatWeek;
    }

    public void setRepeatWeek(int repeatWeek) {
        this.repeatWeek.set(repeatWeek);
    }

    public int getRepeatDay() {
        return repeatDay.get();
    }

    public SimpleIntegerProperty repeatDayProperty() {
        return repeatDay;
    }

    public void setRepeatDay(int repeatDay) {
        this.repeatDay.set(repeatDay);
    }
}

