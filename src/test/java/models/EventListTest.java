package models;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created By:
 * -Name:   Thanapong Supalak
 * -ID:     5810405029
 * Project Name: Calendar
 */

class EventListTest {

    private EventList mockedEventList;
    private DateEvent mockedEvent;

    @BeforeEach
    void init(){
        mockedEventList = new EventList();
        mockedEvent = new DateEvent();
        mockedEventList.addEvent(mockedEvent);
    }

    @AfterEach
    void tearDown(){
        mockedEventList = null;
        mockedEvent = null;
    }

    @Test
    void addEvent() {
        DateEvent mockedEvent2 = new DateEvent();
        mockedEventList.addEvent(mockedEvent2);
        assertEquals(mockedEventList.getEventList().size(), 2);
    }

    @Test
    void deleteEvent() {
        mockedEventList.deleteEvent(0);
        assertEquals(mockedEventList.getEventList().size(), 0);
        assertTrue(mockedEventList.getEventList().isEmpty());
    }

    @Test
    void getEventList() {
        assertNotNull(mockedEventList.getEventList());
    }

}