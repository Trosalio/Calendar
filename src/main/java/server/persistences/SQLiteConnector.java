package server.persistences;

import common.DateEvent;
import common.DateEventFormatter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class SQLiteConnector extends DBConnector {

    @Override
    public String setUpDriver() {
        String jdbc_url = "jdbc:sqlite:";
        File dbFile = new File("DateEvents.db");
        if (!dbFile.exists()) try {
            boolean success = dbFile.createNewFile();
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
                "recurrence TEXT)";
        updateDatabase(createTableSQL);
    }

    @Override
    public ResultSet loadItemsFromDatabase(ArrayList<DateEvent> eventList) {
        createTableIfNotExist();
        ResultSet resultSet;
        try (Connection connection = getDatabaseConnection()) {
            String selectSQL = "SELECT * FROM DateEvent";
            resultSet = connection.prepareStatement(selectSQL).executeQuery();
            pullDataToEventList(resultSet, eventList);
            resultSet.close();
            selectSQL = "SELECT seq FROM sqlite_sequence WHERE name = 'DateEvent'";
            resultSet = connection.prepareStatement(selectSQL).executeQuery();
            int pkID = resultSet.next() ? resultSet.getInt(1) : 1;
            DateEvent.setPrimaryKeyID(pkID);
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void pullDataToEventList(ResultSet rs, ArrayList<DateEvent> eventList) throws SQLException {
        while (rs.next()) {
            DateEvent event = new DateEvent();
            int ID = rs.getInt("ID");
            String name = rs.getString("eventName");
            int priority = rs.getInt("eventPriority");
            String date = rs.getString("eventDate");
            String desc = rs.getString("eventDescription");
            int recurrence = rs.getInt("recurrence");
            event.setID(ID);
            event.setEventName(name);
            event.setEventPriority(priority);
            event.setEventStartDate(LocalDate.parse(date, new DateEventFormatter().getFormatter()));
            event.setEventDescription(desc);
            event.setRecurrence(recurrence);
            DateEvent.setPrimaryKeyID(rs.getInt("ID"));
            eventList.add(event);
        }
    }

    @Override
    protected void insertItemToDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int recurrence) {
        String insertSQL = "INSERT INTO DateEvent (eventName, eventPriority, eventDate, eventDescription, recurrence) VALUES(?,?,?,?,?)";
        try (Connection connection = getDatabaseConnection();
             PreparedStatement pStmt = connection.prepareStatement(insertSQL)) {
            pStmt.setString(1, eventName);
            pStmt.setInt(2, eventPriority);
            pStmt.setString(3, eventDate);
            pStmt.setString(4, eventDescription);
            pStmt.setInt(5, recurrence);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void modifyItemInDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int eventID, int recurrence) {
        String updateSQL = "UPDATE DateEvent SET eventName = ?, eventPriority = ?, eventDate = ?, eventDescription = ?, recurrence = ? WHERE ID = ?";
        try (Connection connection = getDatabaseConnection();
             PreparedStatement pStmt = connection.prepareStatement(updateSQL)) {
            pStmt.setString(1, eventName);
            pStmt.setInt(2, eventPriority);
            pStmt.setString(3, eventDate);
            pStmt.setString(4, eventDescription);
            pStmt.setInt(5, recurrence);
            pStmt.setInt(6, eventID);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteItemInDatabase(int ID) {
        String deleteSQL = String.format("DELETE FROM DateEvent WHERE ID = %d", ID);
        updateDatabase(deleteSQL);
    }
}
