package models.persistents;

import javafx.collections.ObservableList;
import models.DateEvent;

import java.sql.*;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public abstract class DBConnector {

    protected String JDBC_DRIVER;
    protected String JDBC_URL;
    protected Connection conn;

    public Connection getDatabaseConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(JDBC_URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    protected abstract String initJDBC_URL();

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
            if (pStmt != null) try {
                pStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (conn != null) try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract ResultSet loadItemsFromDatabase(ObservableList<DateEvent> eventList);

    protected abstract void pullDataToEventList(ResultSet rs, ObservableList<DateEvent> eventList) throws SQLException;
}
