import controllers.CalendarMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.DBManager;
import models.EventList;

import java.io.IOException;

/**
 * Thanapong Supalak 5810405029
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
        CalendarMainController calendarMainController = loader.getController();

        calendarMainController.setEventList(eventList);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Calendar");
        stage.setResizable(false);
        stage.show();
    }
}
