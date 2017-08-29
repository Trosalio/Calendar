import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CalendarApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("calendarUI.fxml"));
        loader.setControllerFactory(t -> new CalendarController());

        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Event Window");
        stage.setResizable(false);
        stage.show();
    }
}
