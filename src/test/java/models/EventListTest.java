package models;

import common.DateEvent;
import org.junit.jupiter.api.*;
import server.CalendarServiceImp;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

class EventListTest {

    private CalendarServiceImp mockedEventManager;
    private DateEvent mockedEvent;

    @BeforeEach
    void init(){
        mockedEventManager = new CalendarServiceImp();
        mockedEvent = new DateEvent();
        mockedEventManager.addEvent(mockedEvent);
    }

    @AfterEach
    void tearDown(){
        mockedEventManager = null;
        mockedEvent = null;
    }

    @Test
    void addEvent() {
        DateEvent mockedEvent2 = new DateEvent();
        mockedEventManager.addEvent(mockedEvent2);
        assertEquals(mockedEventManager.getEvents().size(), 2);
    }

    @Test
    void deleteEvent() {
        mockedEventManager.deleteEvent(0);
        assertEquals(mockedEventManager.getEvents().size(), 0);
        assertTrue(mockedEventManager.getEvents().isEmpty());
    }

    @Test
    void getEventList() {
        assertNotNull(mockedEventManager.getEvents());
    }

}