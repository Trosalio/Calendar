package models.persistents;

import models.DateEvent;
import models.EventManager;

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

    private DateTimeFormatter dateTimeFormatter;
    private EventManager eventManager;
    private DBConnector databaseConnector;

    public DBManager(EventManager eventManager) {
        this.eventManager = eventManager;
        this.eventManager.setDbManager(this);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    }

    public void insertEventRecord(DateEvent event) {
        String eventName = event.getEventName();
        int eventPriority = event.getEventPriority();
        String eventStartDateDate = dateTimeFormatter.format(event.getEventStartDate());
        String eventDescription = event.getEventDescription();
        boolean isRecurred = event.isRecurred();
        boolean isRepeatMonth = event.isRepeatMonth();
        boolean isRepeatWeek = event.isRepeatWeek();
        boolean isRepeatDay = event.isRepeatDay();
        databaseConnector.insertItemToDatabase(eventName, eventPriority, eventStartDateDate,eventDescription,
                isRecurred, isRepeatMonth, isRepeatWeek, isRepeatDay);
    }

    public void deleteEventRecord(int ID, boolean isRecurred) {
        databaseConnector.deleteItemInDatabase(ID, isRecurred);
    }

    public void modifyEventRecord(DateEvent event) {
        String eventName = event.getEventName();
        int eventPriority = event.getEventPriority();
        String eventStartDateDate = dateTimeFormatter.format(event.getEventStartDate());
        String eventDescription = event.getEventDescription();
        int eventID = event.getID();
        boolean isRecurred = event.isRecurred();
        boolean isRepeatMonth = event.isRepeatMonth();
        boolean isRepeatWeek = event.isRepeatWeek();
        boolean isRepeatDay = event.isRepeatDay();
        databaseConnector.modifyItemInDatabase(eventName, eventPriority, eventStartDateDate, eventDescription, eventID,
                isRecurred, isRepeatMonth, isRepeatWeek, isRepeatDay);
    }

    public void loadDatabase() {
        databaseConnector.createTableIfNotExist();
        Connection conn = null;
        PreparedStatement pStmt = null;
        try {
            String selectSQL = "SELECT * FROM DateEvent WHERE isRecurred = 0";
            conn = databaseConnector.getDatabaseConnection();
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
                event.setRepeatMonth(rs.getInt("repeatMonth") == 1);
                event.setRepeatWeek(rs.getInt("repeatWeek") == 1);
                event.setRepeatDay(rs.getInt("repeatDay") == 1);
            }
            DateEvent.setPrimaryKeyID(rs.getInt("ID"));
            eventManager.getEvents().add(event);
        }
    }

    public void setDatabaseConnector(DBConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }
}
