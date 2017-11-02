package server.persistences;

import javafx.collections.ObservableList;
import common.DateEvent;
import org.sqlite.SQLiteConfig;

import java.sql.*;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public abstract class DBConnector {

    private String DRIVER_URL = setUpDriver();
    protected Connection conn;

    public Connection getDatabaseConnection() {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        try {
            conn = DriverManager.getConnection(DRIVER_URL, config.toProperties());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public abstract String setUpDriver();

    protected abstract void createTableIfNotExist();

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
        PreparedStatement pStmt = null;
        try {
            conn = getDatabaseConnection();
            pStmt = conn.prepareStatement(updateSQL);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { pStmt.close(); } catch (SQLException e) {/* ignored */}
            try { conn.close(); } catch (SQLException e) {/* ignored */}
        }
    }

    public abstract ResultSet loadItemsFromDatabase(ObservableList<DateEvent> eventList);

    protected abstract void pullDataToEventList(ResultSet rs, ObservableList<DateEvent> eventList) throws SQLException;

    public void setDRIVER_URL(String DRIVER_URL) {
        this.DRIVER_URL = DRIVER_URL;
    }
}
