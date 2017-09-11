package models;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

class DateEventTest {

    private static DateEvent mockedEvent;
    private static LocalDate mockedDate;

    @BeforeAll
    static void initAll(){
        mockedEvent = new DateEvent();
        mockedEvent.setEventName("Exam");
        mockedEvent.setEventDescription("18/9/2015 - 12:00 to 15:00");

        mockedDate = LocalDate.now().plusDays(7);
        mockedEvent.setEventDate(mockedDate);
    }

    @AfterAll
    static void tearDownAll(){
        mockedEvent = null;
        mockedDate = null;
    }
    @Test
    void getEventName() {
        assertEquals(mockedEvent.getEventName(), "Exam");
    }

    @Test
    void getEventDate() {
        assertEquals(mockedEvent.getEventDate(), mockedDate);
    }

    @Test
    void getEventDescription() {
        assertEquals(mockedEvent.getEventDescription(), "18/9/2015 - 12:00 to 15:00");
    }
}