package models.persistents;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */


//  Did not implement anything(for now) as I considered it as a mocked class in order to fulfill current user requirements
public class MySQLConnector extends DBConnector {

    public MySQLConnector(){
        JDBC_DRIVER = "com.mysql.jdbc.Driver";
        JDBC_URL = initJDBC_URL();
    }

    @Override
    protected String initJDBC_URL() {
        return "jdbc:mysql://localhost:3306/MemoView?user=root";
    }

    @Override
    protected void createTableIfNotExist() {

    }

    @Override
    protected void insertItemToDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, boolean isRecurred) {

    }

    @Override
    public void insertItemToDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, boolean isRecurred, boolean isMonthly, boolean isWeekly, boolean isDaily) {

    }

    @Override
    protected void modifyItemInDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int eventID, boolean isRecurred) {

    }

    @Override
    public void modifyItemInDatabase(String eventName, int eventPriority, String eventDate, String eventDescription, int eventID, boolean isRecurred, boolean isMonthly, boolean isWeekly, boolean isDaily) {

    }

    @Override
    public void deleteItemInDatabase(int ID, boolean isRecurred) {

    }

    @Override
    protected void deleteRecurredItemInDatabase(int ID) {

    }
}
