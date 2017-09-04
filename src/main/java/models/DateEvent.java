package models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

/**
 * Thanapong Supalak 5810405029
 */

public class DateEvent{

    private SimpleStringProperty eventName;
    private ObjectProperty<LocalDate> eventDate;
    private SimpleStringProperty eventDescription;

    public DateEvent() {
        this.eventName = new SimpleStringProperty();
        this.eventDate = new SimpleObjectProperty<>();
        this.eventDescription = new SimpleStringProperty();
    }

    public DateEvent(String eventName, LocalDate eventDate, String eventDescription) {
        this.eventName = new SimpleStringProperty(eventName);
        this.eventDate = new SimpleObjectProperty<>(eventDate);
        this.eventDescription = new SimpleStringProperty(eventDescription);
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

//    public SimpleStringProperty eventDescriptionProperty() {
//        return eventDescription;
//    }

    @Override
    public String toString() {
        return "DateEvent{" +
                "eventName=" + eventName +
                ", eventDate=" + eventDate +
                ", eventDescription=" + eventDescription +
                '}';
    }
}

