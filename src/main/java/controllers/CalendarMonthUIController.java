package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class CalendarMonthUIController {

    @FXML
    private Label monthLbl, yearLbl;
    @FXML
    private GridPane gridPane;

    private VBox[][] vBoxes;

    private Label[][] dateLabels;

    private LocalDate date;

    @FXML
    public void initialize() {
        date = LocalDate.now();
        initArrays(gridPane.getRowConstraints().size(), gridPane.getColumnConstraints().size());
        updateMonthTable(date);
    }

    private void initArrays(int row, int column) {
        vBoxes = new VBox[row][column];
        dateLabels = new Label[row][column];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < column; j++) {
                vBoxes[i][j] = new VBox();
                dateLabels[i][j] = new Label();
                vBoxes[i][j].getChildren().add(dateLabels[i][j]);
                vBoxes[i][j].setPadding(new Insets(10, 10, 10, 10));
                gridPane.add(vBoxes[i][j], j, i);
            }
    }

    private void updateMonthTable(LocalDate date) {
        monthLbl.setText(date.getMonth().toString());
        yearLbl.setText(String.valueOf(date.getYear()));
        LocalDate firstDateOfMonth = date.withDayOfMonth(1);
        int firstDayOfWeek = firstDateOfMonth.getDayOfWeek().getValue() % 7;
        int indexDay = 0;
        for (int i = 0; i < gridPane.getRowConstraints().size(); i++) {
            for (int j = 0; j < gridPane.getColumnConstraints().size(); j++) {
                LocalDate currentDate = firstDateOfMonth.plusDays(indexDay - firstDayOfWeek);
                dateLabels[i][j].setText(String.valueOf(currentDate.getDayOfMonth()));
                if(currentDate.getMonth() != date.getMonth()){
                    vBoxes[i][j].setDisable(true);
                } else {
                    vBoxes[i][j].setDisable(false);
                }
                indexDay++;
            }
        }
    }

    @FXML
    private void onPrevMonthBtn() {
        updateMonthTable(date = date.minusMonths(1));
    }

    @FXML
    private void onNextMonthBtn() {
        updateMonthTable(date = date.plusMonths(1));
    }
}

