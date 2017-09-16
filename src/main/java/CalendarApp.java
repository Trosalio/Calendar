import controllers.MainUIController;
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
        MainUIController mainUIController = new MainUIController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
        loader.setController(mainUIController);
        Parent root = loader.load();
        EventList eventList = new EventList();
        DBManager dbManager = new DBManager(eventList);
        eventList.setDbManager(dbManager);
        dbManager.loadDatabase();
        mainUIController.setEventList(eventList);

        stage.setScene(new Scene(root));
        stage.setTitle("Calendar");

        stage.setResizable(true);
        stage.show();
    }
}
