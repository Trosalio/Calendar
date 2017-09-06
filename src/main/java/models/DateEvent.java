package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

/**
 * Thanapong Supalak 5810405029
 */

public class DateEvent {

    private static int primaryKeyDateID;

    private int DateID;
    private SimpleStringProperty eventName = new SimpleStringProperty(this, "eventName");
    private ObjectProperty<LocalDate> eventDate = new SimpleObjectProperty<>(this, "eventDate");
    private SimpleStringProperty eventDescription = new SimpleStringProperty(this, "eventDescription");

    public static int getPrimaryKeyDateID() {
        return primaryKeyDateID;
    }

    public static void setPrimaryKeyDateID(int primaryKeyDateID) {
        DateEvent.primaryKeyDateID = primaryKeyDateID;
    }

    public String getEventName() {
        return eventName.get();
    }

    public void setEventName(String eventName) {
        this.eventName.set(eventName);
    }

    public SimpleStringProperty eventNameProperty() {
        return eventName;
    }

    public LocalDate getEventDate() {
        return eventDate.get();
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate.set(eventDate);
    }

    public ObjectProperty<LocalDate> eventDateProperty() {
        return eventDate;
    }

    public String getEventDescription() {
        return eventDescription.get();
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription.set(eventDescription);
    }

    public int getDateID() {
        return DateID;
    }

    public void setDateID(int dateID) {
        DateID = dateID;
    }

    public SimpleStringProperty eventDescriptionProperty() {
        return eventDescription;
    }

    @Override
    public String toString() {
        return "DateEvent{" +
                "eventName=" + eventName +
                ", eventDate=" + eventDate +
                ", eventDescription=" + eventDescription +
                '}';
    }
}

