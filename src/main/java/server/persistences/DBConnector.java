package server.persistences;

import common.DateEvent;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.ArrayList;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public abstract class DBConnector {

    private String DRIVER_URL = setUpDriver();

    protected Connection getDatabaseConnection() {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DRIVER_URL, config.toProperties());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public abstract String setUpDriver();

    protected abstract void createTableIfNotExist();

    public abstract ResultSet loadItemsFromDatabase(ArrayList<DateEvent> eventList);

    protected abstract void pullDataToEventList(ResultSet rs, ArrayList<DateEvent> eventList) throws SQLException;

    // Does not contain recurring event
    protected abstract void insertItemToDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, boolean isRecurred);

    // Contains recurring event
    public abstract void insertItemToDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, boolean isRecurred, boolean isMonthly, boolean isWeekly, boolean isDaily);

    // Does not contain recurring event
    protected abstract void modifyItemInDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int eventID, boolean isRecurred);

    // Contains recurring event
    public abstract void modifyItemInDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int eventID, boolean isRecurred, boolean isMonthly, boolean isWeekly, boolean isDaily);

    public abstract void deleteItemInDatabase(int ID, boolean isRecurred);

    protected abstract void deleteRecurredItemInDatabase(int ID);

    protected void updateDatabase(String updateSQL) {
        try (Connection connection = getDatabaseConnection();
             PreparedStatement pStmt = connection.prepareStatement(updateSQL)) {
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
