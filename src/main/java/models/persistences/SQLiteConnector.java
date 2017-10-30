package models.persistences;

import javafx.collections.ObservableList;
import models.DateEvent;
import models.DateEventFormatter;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class SQLiteConnector extends DBConnector {

    private int fkID;

    public SQLiteConnector(String JDBC_URL) {
        super(JDBC_URL);
    }

    @Override
    public String setUpJDBC_URL() {
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
            try { pStmt.close(); } catch (SQLException e) {/* ignored */}
            try { conn.close(); } catch (SQLException e) {/* ignored */}
        }
    }

    @Override
    public void insertItemToDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, boolean isRecurred, boolean isMonthly, boolean isWeekly, boolean isDaily) {
        insertItemToDatabase(eventName, eventPriority, eventDate, eventDescription, isRecurred);
        if (isRecurred) {
            PreparedStatement pStmt = null;
            try {
                conn = getDatabaseConnection();
                String insertSQL = "INSERT INTO DateEventMeta(eventID, repeatMonth, repeatWeek, repeatDay) VALUES(?,?,?,?)";
                pStmt = conn.prepareStatement(insertSQL);
                pStmt.setInt(1, fkID);
                pStmt.setInt(2, isMonthly ? 1 : 0);
                pStmt.setInt(3, isWeekly ? 1 : 0);
                pStmt.setInt(4, isDaily ? 1 : 0);
                pStmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try { pStmt.close(); } catch (SQLException e) {/* ignored */}
                try { conn.close(); } catch (SQLException e) {/* ignored */}
            }
        }
    }

    @Override
    protected void modifyItemInDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int eventID, boolean isRecurred) {
        PreparedStatement pStmt = null;
        try {
            String updateSQL = "UPDATE DateEvent SET eventName = ?, eventPriority = ?, eventDate = ?, eventDescription = ?, isRecurred = ? WHERE ID = ?";
            conn = getDatabaseConnection();
            pStmt = conn.prepareStatement(updateSQL);
            pStmt.setString(1, eventName);
            pStmt.setInt(2, eventPriority);
            pStmt.setString(3, eventDate);
            pStmt.setString(4, eventDescription);
            pStmt.setInt(5, isRecurred ? 1 : 0);
            pStmt.setInt(6, eventID);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { pStmt.close(); } catch (SQLException e) {/* ignored */}
            try { conn.close(); } catch (SQLException e) {/* ignored */}
        }
    }

    @Override
    public void modifyItemInDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int eventID, boolean isRecurred, boolean isMonthly, boolean isWeekly, boolean isDaily) {
        modifyItemInDatabase(eventName, eventPriority, eventDate, eventDescription, eventID, isRecurred);
        if (isRecurred) {
            PreparedStatement pStmt = null;
            try {
                conn = getDatabaseConnection();
                String updateSQL = "INSERT OR REPLACE INTO DateEventMeta(eventID, repeatMonth, repeatWeek, repeatDay) VALUES(?,?,?,?)";
                pStmt = conn.prepareStatement(updateSQL);
                pStmt.setInt(1, eventID);
                pStmt.setInt(2, isMonthly ? 1 : 0);
                pStmt.setInt(3, isWeekly ? 1 : 0);
                pStmt.setInt(4, isDaily ? 1 : 0);
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

    @Override
    public ResultSet loadItemsFromDatabase(ObservableList<DateEvent> eventList) {
        createTableIfNotExist();
        PreparedStatement pStmt = null;
        ResultSet resultSet;
        try {
            conn = getDatabaseConnection();
            String[] selectSQLs = new String[]{"SELECT * FROM DateEvent WHERE isRecurred = 0", "SELECT * FROM DateEvent INNER JOIN DateEventMeta WHERE ID = eventID"};
            for (String sql : selectSQLs) {
                pStmt = conn.prepareStatement(sql);
                resultSet = pStmt.executeQuery();
                pullDataToEventList(resultSet, eventList);
                resultSet.close();
            }
            String selectSQL = "SELECT seq FROM sqlite_sequence WHERE name = 'DateEvent'";
            resultSet = conn.prepareStatement(selectSQL).executeQuery();
            int pkID = resultSet.next() ? resultSet.getInt(1) : 1;
            DateEvent.setPrimaryKeyID(pkID);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { pStmt.close(); } catch (SQLException e) {/* ignored */}
            try { conn.close(); } catch (SQLException e) {/* ignored */}
        }
        return null;
    }

    protected void pullDataToEventList(ResultSet rs, ObservableList<DateEvent> eventList) throws SQLException {
        while (rs.next()) {
            DateEvent event = new DateEvent();
            int ID = rs.getInt("ID");
            String name = rs.getString("eventName");
            int priority = rs.getInt("eventPriority");
            String date = rs.getString("eventDate");
            String desc = rs.getString("eventDescription");
            boolean isRecurred = rs.getInt("isRecurred") == 1;
            event.setID(ID);
            event.setEventName(name);
            event.setEventPriority(priority);
            event.setEventStartDate(LocalDate.parse(date, new DateEventFormatter().getFormatter()));
            event.setEventDescription(desc);
            event.setRecurred(isRecurred);
            if (isRecurred) {
                event.setRepeatMonth(rs.getInt("repeatMonth") == 1);
                event.setRepeatWeek(rs.getInt("repeatWeek") == 1);
                event.setRepeatDay(rs.getInt("repeatDay") == 1);
            }
            DateEvent.setPrimaryKeyID(rs.getInt("ID"));
            eventList.add(event);
        }
    }
}
