import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        FXMLLoader calendarUILoader = new FXMLLoader(getClass().getResource("calendarUI.fxml"));
        Scene calendarUI = new Scene(calendarUILoader.load());
        stage.setScene(calendarUI);
        stage.setTitle("Event Window");
        stage.setResizable(false);
        stage.show();
    }
}
