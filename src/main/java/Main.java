import controllers.MainUIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.EventManager;
import models.persistents.DBConnector;
import models.persistents.DBManager;
import models.persistents.SQLiteConnector;

import java.io.IOException;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        EventManager eventManager = new EventManager();
        DBConnector DBConnector = new SQLiteConnector();
        DBManager dbManager = new DBManager(eventManager);
        dbManager.setDatabaseConnector(DBConnector);

        FXMLLoader mainUILoader = new FXMLLoader(getClass().getResource("/MainUI.fxml"));
        Parent root = mainUILoader.load();
        MainUIController mainUIController = mainUILoader.getController();
        mainUIController.setEventManager(eventManager);
        dbManager.loadDatabase();
        mainUIController.initUITabs();

        stage.setScene(new Scene(root));
        stage.setTitle("Calendar");
        stage.setResizable(true);

        // perform cleanup before closing the program
        stage.setOnHidden(e -> Platform.exit());
        stage.show();
    }
}
