import controllers.CalendarMainUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.DBManager;
import models.EventList;

import java.io.IOException;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class CalendarApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

        @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarMainUI.fxml"));
        Parent root = loader.load();

        EventList eventList = new EventList();
        DBManager dbManager = new DBManager(eventList);
        eventList.setDbManager(dbManager);
        dbManager.loadDatabase();
        CalendarMainUIController calendarMainUIController = loader.getController();

        calendarMainUIController.setEventList(eventList);

        stage.setScene(new Scene(root));
        stage.setTitle("Calendar");
        stage.setResizable(false);
        stage.show();
    }
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("CalendarMonthUI.fxml"));
//        Parent root = loader.load();
//
//        primaryStage.setScene(new Scene(root));
//        primaryStage.setTitle("Calendar");
//        primaryStage.setResizable(false);
//        primaryStage.show();
//    }

}
