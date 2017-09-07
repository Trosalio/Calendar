package models;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author
 * Name:   Thanapong Supalak
 * ID:     5810405029
 * @Project Project Name: Calendar
 */

public class DBManager {

    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:DateEvents.db";

    private DateTimeFormatter dateTimeFormatter;
    private EventList eventList;

    public DBManager(EventList eventList) {
        this.eventList = eventList;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    }

    public void loadDatabase() {
        Connection conn = null;
        PreparedStatement pStmt = null;
        String selectQuery = "SELECT * FROM DateEvent";
        try {
            conn = connectDB();
            pStmt = conn.prepareStatement(selectQuery);
            ResultSet resultSet = pStmt.executeQuery();
            pullDataToEventList(resultSet);
            resultSet.close();

            selectQuery = "SELECT seq FROM sqlite_sequence";
            resultSet = conn.prepareStatement(selectQuery).executeQuery();
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

    public void insertEventRecord(DateEvent event) {
        Connection conn = null;
        PreparedStatement pStmt = null;
        String insertQuery = "INSERT INTO DateEvent (EventName, EventDate, EventDescription) VALUES(?,?,?)";
        try {
            conn = connectDB();
            pStmt = conn.prepareStatement(insertQuery);
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
        Connection conn = null;
        PreparedStatement pStmt = null;
        String deleteQuery = String.format("DELETE FROM DateEvent WHERE DateID = %d", dateID);
        try {
            conn = connectDB();
            pStmt = conn.prepareStatement(deleteQuery);
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

    public void modifyEventRecord(DateEvent event) {
        Connection conn = null;
        PreparedStatement pStmt = null;
        String updateQuery = "UPDATE DateEvent SET EventName = ?, EventDate = ?, EventDescription = ? WHERE DateID = ?";
        try {
            conn = connectDB();
            pStmt = conn.prepareStatement(updateQuery);
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
