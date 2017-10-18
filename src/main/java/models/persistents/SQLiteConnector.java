package models.persistents;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class SQLiteConnector extends DBConnector {

    private int fkID;

    public SQLiteConnector() {
        JDBC_DRIVER = "org.sqlite.JDBC";
        JDBC_URL = initJDBC_URL();
    }

    @Override
    protected String initJDBC_URL() {
        String jdbc_url = "jdbc:sqlite:";
        String homePath = System.getProperty("user.home");
        File dbFolder = new File(homePath + File.separator + "5029db");
        File dbFile = new File(dbFolder.getPath() + File.separator + "DateEvents.db");
        if (!dbFolder.exists() || !dbFile.exists()) try {
            boolean success = dbFolder.mkdir();
            if (success) success = dbFile.createNewFile();
            if (success) System.out.println("Successfully create new DB file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbc_url = jdbc_url + dbFile;
        return jdbc_url;
    }

    @Override
    protected void createTableIfNotExist() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS DateEvent" +
                "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "eventName TEXT NOT NULL," +
                "eventPriority INTEGER NOT NULL," +
                "eventDate TEXT NOT NULL," +
                "eventDescription TEXT," +
                "isRecurred INTEGER NOT NULL)";
        updateDatabase(createTableSQL);
        createTableSQL = "CREATE TABLE IF NOT EXISTS DateEventMeta" +
                "(eventID INTEGER NOT NULL," +
                "repeatMonth INTEGER," +
                "repeatWeek INTEGER," +
                "repeatDay INTEGER," +
                "FOREIGN KEY(eventID) REFERENCES DateEvent(ID)," +
                "PRIMARY KEY(eventID))";
        updateDatabase(createTableSQL);
    }

    @Override
    protected void insertItemToDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, boolean isRecurred) {
        Connection conn = null;
        PreparedStatement pStmt = null;
        try {
            conn = getDatabaseConnection();
            String insertSQL = "INSERT INTO DateEvent (eventName, eventPriority, eventDate, eventDescription, isRecurred) VALUES(?,?,?,?,?)";
            pStmt = conn.prepareStatement(insertSQL);
            pStmt.setString(1, eventName);
            pStmt.setInt(2, eventPriority);
            pStmt.setString(3, eventDate);
            pStmt.setString(4, eventDescription);
            pStmt.setInt(5, isRecurred ? 1 : 0);
            pStmt.executeUpdate();
            fkID = -1;
            if (isRecurred) {
                ResultSet rs = pStmt.getGeneratedKeys();
                if (rs.next()) {
                    fkID = rs.getInt(1);
                }
                rs.close();
            }
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

    @Override
    public void insertItemToDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, boolean isRecurred, boolean isMonthly, boolean isWeekly, boolean isDaily) {
        insertItemToDatabase(eventName, eventPriority, eventDate, eventDescription, isRecurred);
        if(isRecurred){
            Connection conn = null;
            PreparedStatement pStmt = null;
            try {
                conn = getDatabaseConnection();
                String insertSQL = "INSERT INTO DateEventMeta(eventID, repeatMonth, repeatWeek, repeatDay) VALUES(?,?,?,?,?)";
                pStmt = conn.prepareStatement(insertSQL);
                pStmt.setInt(1, fkID);
                pStmt.setInt(3, isMonthly ? 1 : 0);
                pStmt.setInt(4, isWeekly ? 1 : 0);
                pStmt.setInt(5, isDaily ? 1 : 0);
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

    @Override
    protected void modifyItemInDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int eventID, boolean isRecurred) {
        Connection conn = null;
        PreparedStatement pStmt = null;
        try {
            String updateSQL = "UPDATE DateEvent SET eventName = ?, eventPriority = ?, eventDate = ?, eventDescription = ?, isRecurred = ? WHERE ID = ?";
            conn = getDatabaseConnection();
            pStmt = conn.prepareStatement(updateSQL);
            pStmt.setString(1, eventName);
            pStmt.setInt(2, eventPriority);
            pStmt.setString(3, eventDate);
            pStmt.setString(4, eventDescription);
            pStmt.setInt(5, isRecurred? 1: 0);
            pStmt.setInt(6, eventID);
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

    @Override
    public void modifyItemInDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int eventID, boolean isRecurred, boolean isMonthly, boolean isWeekly, boolean isDaily) {
        modifyItemInDatabase(eventName, eventPriority, eventDate, eventDescription, eventID, isRecurred);
        if(isRecurred){
            Connection conn = null;
            PreparedStatement pStmt = null;
            try {
                conn = getDatabaseConnection();
                String updateSQL = "UPDATE DateEventMeta SET repeatInterval = ?, repeatMonth = ?, repeatWeek = ?, repeatDay = ? WHERE eventID = ?";
                pStmt = conn.prepareStatement(updateSQL);
                pStmt.setInt(1, isMonthly? 1 : 0);
                pStmt.setInt(2, isWeekly? 1 : 0);
                pStmt.setInt(3, isDaily? 1 : 0);
                pStmt.setInt(4, eventID);
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
        } else {
            deleteRecurredItemInDatabase(eventID);
        }
    }

    @Override
    public void deleteItemInDatabase(int ID, boolean isRecurred) {
        String deleteSQL = String.format("DELETE FROM DateEvent WHERE ID = %d", ID);
        updateDatabase(deleteSQL);
        if (isRecurred) deleteRecurredItemInDatabase(ID);

    }

    @Override
    protected void deleteRecurredItemInDatabase(int ID) {
        String deleteSQL = String.format("DELETE FROM DateEventMeta WHERE eventID = %d", ID);
        updateDatabase(deleteSQL);
    }
}
