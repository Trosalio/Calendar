package models;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class DBManager {

    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static String DB_URL;

    private DateTimeFormatter dateTimeFormatter;
    private EventList eventList;

    public DBManager(EventList eventList) {
        this.eventList = eventList;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        DB_URL = initDatabaseURL();
        createTableIfNotExist();
    }

    public void insertEventRecord(DateEvent event) {
        Connection conn = null;
        PreparedStatement pStmt = null;
        String insertSQL = "INSERT INTO DateEvent (EventName, EventDate, EventDescription) VALUES(?,?,?)";
        try {
            conn = connectDB();
            pStmt = conn.prepareStatement(insertSQL);
            pStmt.setString(1, event.getEventName());
            pStmt.setString(2, dateTimeFormatter.format(event.getEventDate()));
            pStmt.setString(3, event.getEventDescription());
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

    public void deleteEventRecord(int dateID) {
        String deleteSQL = String.format("DELETE FROM DateEvent WHERE DateID = %d", dateID);
        updateDatabase(deleteSQL);
    }

    public void modifyEventRecord(DateEvent event) {
        Connection conn = null;
        PreparedStatement pStmt = null;
        String updateSQL = "UPDATE DateEvent SET EventName = ?, EventDate = ?, EventDescription = ? WHERE DateID = ?";
        try {
            conn = connectDB();
            pStmt = conn.prepareStatement(updateSQL);
            pStmt.setString(1, event.getEventName());
            pStmt.setString(2, dateTimeFormatter.format(event.getEventDate()));
            pStmt.setString(3, event.getEventDescription());
            pStmt.setInt(4, event.getDateID());
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

    private static String initDatabaseURL() {
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

    public void loadDatabase() {
        Connection conn = null;
        PreparedStatement pStmt = null;
        String selectSQL = "SELECT * FROM DateEvent";
        try {
            conn = connectDB();
            pStmt = conn.prepareStatement(selectSQL);
            ResultSet resultSet = pStmt.executeQuery();
            pullDataToEventList(resultSet);
            resultSet.close();

            selectSQL = "SELECT seq FROM sqlite_sequence";
            resultSet = conn.prepareStatement(selectSQL).executeQuery();
            int pkID = resultSet.next() ? resultSet.getInt(1) : 1;
            DateEvent.setPrimaryKeyDateID(pkID);
            resultSet.close();
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

    private void updateDatabase(String updateSQL) {
        PreparedStatement pStmt = null;
        Connection conn = null;
        try {
            conn = connectDB();
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

    private void createTableIfNotExist() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS DateEvent" +
                "(DateID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "EventName TEXT NOT NULL," +
                "EventDate TEXT NOT NULL," +
                "EventDescription TEXT)";
        updateDatabase(createTableSQL);
    }

    private void pullDataToEventList(ResultSet rs) throws SQLException {
        while (rs.next()) {
            DateEvent event = new DateEvent();
            int DateID = rs.getInt("DateID");
            String name = rs.getString("EventName");
            String date = rs.getString("EventDate");
            String desc = rs.getString("EventDescription");
            event.setDateID(DateID);
            event.setEventName(name);
            event.setEventDate(LocalDate.parse(date, dateTimeFormatter));
            event.setEventDescription(desc);
            DateEvent.setPrimaryKeyDateID(rs.getInt("DateID"));
            eventList.getEventList().add(event);
        }
    }

    private static Connection connectDB() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
