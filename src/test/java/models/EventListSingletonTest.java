package models;

import javafx.collections.ObservableList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Thanapong Supalak 5810405029
 */

public class EventListSingletonTest {

    private ObservableList<DateEvent> testList = EventListSingleton.getInstance().getEventList();
    @Test
    public void testGetInstance() throws Exception {
        assertEquals(testList, EventListSingleton.getInstance().getEventList());
    }
}