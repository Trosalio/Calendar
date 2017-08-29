import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CalendarController {

    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;

    @FXML
    private void onCancel() {
        System.exit(0);
    }

    @FXML
    private void onSave() {

    }


}
