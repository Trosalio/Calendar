package server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.persistences.DBConnector;
import server.persistences.DBManager;
import server.persistences.SQLiteConnector;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class ServerMain {
    public static void main(String[] args) throws Exception {
        ApplicationContext bf = new ClassPathXmlApplicationContext("/configs/calendar-server.xml");
        CalendarServiceImp serviceImp = (CalendarServiceImp) bf.getBean("calendarServiceImp");
        DBConnector DBConnector = bf.getBean("sQLiteConnector", SQLiteConnector.class);
        DBManager dbManager = (DBManager) bf.getBean("dbManager");
        dbManager.setDatabaseConnector(DBConnector);
        dbManager.loadDatabase();
        System.out.println("Server is on");
    }
}
