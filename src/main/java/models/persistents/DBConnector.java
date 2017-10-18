package models.persistents;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public abstract class DBConnector {

    protected String JDBC_DRIVER;
    protected String JDBC_URL;

    public Connection getDatabaseConnection(){
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(JDBC_URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
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

    protected void updateDatabase(String updateSQL){
        PreparedStatement pStmt = null;
        Connection conn = null;
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
}
