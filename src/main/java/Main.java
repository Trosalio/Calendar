import controllers.MainUIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.EventManager;
import models.persistences.DBConnector;
import models.persistences.DBManager;
import models.persistences.SQLiteConnector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        ApplicationContext bf = new ClassPathXmlApplicationContext("/configs/main-context.xml");

        EventManager eventManager = new EventManager();
        DBConnector DBConnector = bf.getBean("sQLiteConnector", SQLiteConnector.class);
        DBManager dbManager = new DBManager(eventManager);
        dbManager.setDatabaseConnector(DBConnector);

        FXMLLoader mainUILoader = new FXMLLoader(getClass().getResource("/fxml/MainUI.fxml"));
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
