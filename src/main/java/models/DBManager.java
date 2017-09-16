package models;

import org.sqlite.SQLiteConfig;

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

        try {
            String insertSQL = "INSERT INTO DateEvent (eventName, eventPriority, eventDate, eventDescription, isRecurred) VALUES(?,?,?,?,?)";
            conn = connectDB();
            pStmt = conn.prepareStatement(insertSQL);
            pStmt.setString(1, event.getEventName());
            pStmt.setInt(2, event.getEventPriority());
            pStmt.setString(3, dateTimeFormatter.format(event.getEventStartDate()));
            pStmt.setString(4, event.getEventDescription());
            pStmt.setInt(5, event.isRecurred() ? 1 : 0);
            pStmt.executeUpdate();
            if (event.isRecurred()) {
                ResultSet rs = pStmt.getGeneratedKeys();
                int fkID = -1;
                if (rs.next()) {
                    fkID = rs.getInt(1);
                }
                rs.close();
                insertSQL = "INSERT INTO DateEventMeta(eventID, repeatInterval, repeatMonth, repeatWeek, repeatDay) VALUES(?,?,?,?,?)";
                pStmt = conn.prepareStatement(insertSQL);
                pStmt.setInt(1, fkID);
                pStmt.setInt(2, event.getRepeatInterval());
                pStmt.setInt(3, event.isRepeatMonth() ? 1 : 0);
                pStmt.setInt(4, event.isRepeatWeek() ? 1 : 0);
                pStmt.setInt(5, event.isRepeatDay() ? 1 : 0);
                pStmt.executeUpdate();
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

    public void deleteEventRecord(int ID) {
        String deleteSQL = String.format("DELETE FROM DateEvent WHERE ID = %d", ID);
        updateDatabase(deleteSQL);
    }

    public void deleteRecurRecord(int ID) {
        String deleteSQL = String.format("DELETE FROM DateEventMeta WHERE eventID = %d", ID);
        updateDatabase(deleteSQL);
    }

    public void modifyEventRecord(DateEvent event) {
        Connection conn = null;
        PreparedStatement pStmt = null;
        try {
            String updateSQL = "UPDATE DateEvent SET eventName = ?, eventPriority = ?, eventDate = ?, eventDescription = ?, isRecurred = ? WHERE ID = ?";
            conn = connectDB();
            pStmt = conn.prepareStatement(updateSQL);
            pStmt.setString(1, event.getEventName());
            pStmt.setInt(2, event.getEventPriority());
            pStmt.setString(3, dateTimeFormatter.format(event.getEventStartDate()));
            pStmt.setString(4, event.getEventDescription());
            pStmt.setInt(5, event.isRecurred() ? 1 : 0);
            pStmt.setInt(6, event.getID());
            pStmt.executeUpdate();
            if (event.isRecurred()) {
                pStmt.close();
                updateSQL = "UPDATE DateEventMeta SET repeatInterval = ?, repeatMonth = ?, repeatWeek = ?, repeatDay = ? WHERE eventID = ?";
                pStmt = conn.prepareStatement(updateSQL);
                pStmt.setInt(1, event.getRepeatInterval());
                pStmt.setInt(2, event.isRepeatMonth() ? 1 : 0);
                pStmt.setInt(3, event.isRepeatWeek() ? 1 : 0);
                pStmt.setInt(4, event.isRepeatDay() ? 1 : 0);
                pStmt.setInt(5, event.getID());
                pStmt.executeUpdate();
            } else {
                deleteRecurRecord(event.getID());
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
        try {
            String selectSQL = "SELECT * FROM DateEvent WHERE isRecurred = 0";
            conn = connectDB();
            pStmt = conn.prepareStatement(selectSQL);
            ResultSet resultSet = pStmt.executeQuery();
            pullDataToEventList(resultSet);
            resultSet.close();
            selectSQL = "SELECT * FROM DateEvent INNER JOIN DateEventMeta WHERE ID = eventID";
            resultSet = conn.prepareStatement(selectSQL).executeQuery();
            pullDataToEventList(resultSet);
            resultSet.close();
            selectSQL = "SELECT seq FROM sqlite_sequence WHERE name = 'DateEvent'";
            resultSet = conn.prepareStatement(selectSQL).executeQuery();
            int pkID = resultSet.next() ? resultSet.getInt(1) : 1;
            DateEvent.setPrimaryKeyID(pkID);
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
                "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "eventName TEXT NOT NULL," +
                "eventPriority INTEGER NOT NULL," +
                "eventDate TEXT NOT NULL," +
                "eventDescription TEXT," +
                "isRecurred INTEGER NOT NULL)";
        updateDatabase(createTableSQL);
        createTableSQL = "CREATE TABLE IF NOT EXISTS DateEventMeta" +
                "(eventID INTEGER NOT NULL," +
                "repeatInterval INTEGER," +
                "repeatMonth INTEGER," +
                "repeatWeek INTEGER," +
                "repeatDay INTEGER," +
                "FOREIGN KEY(eventID) REFERENCES DateEvent(ID)," +
                "PRIMARY KEY(eventID))";
        updateDatabase(createTableSQL);
    }

    private void pullDataToEventList(ResultSet rs) throws SQLException {
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
            event.setEventStartDate(LocalDate.parse(date, dateTimeFormatter));
            event.setEventDescription(desc);
            event.setRecurred(isRecurred);
            if (isRecurred) {
                event.setRepeatInterval(rs.getInt("repeatInterval"));
                event.setRepeatMonth(rs.getInt("repeatMonth") == 1);
                event.setRepeatWeek(rs.getInt("repeatWeek") == 1);
                event.setRepeatDay(rs.getInt("repeatDay") == 1);
            }
            DateEvent.setPrimaryKeyID(rs.getInt("ID"));
            eventList.getEvents().add(event);
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
