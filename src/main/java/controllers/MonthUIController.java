package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import models.DateEvent;
import models.EventManager;

import java.time.LocalDate;

/**
 * ~Created by~
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 */

public class MonthUIController {

    @FXML
    private Label monthLbl, yearLbl;
    @FXML
    private GridPane gridPane;

    private VBox[][] vBoxes;

    private LocalDate baseDate;
    private EventManager eventManager;

    @FXML
    public void initialize() {
        baseDate = LocalDate.now();
        initArrays(gridPane.getRowConstraints().size(), gridPane.getColumnConstraints().size());
    }

    private void initArrays(int row, int column) {
        vBoxes = new VBox[row][column];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < column; j++) {
                vBoxes[i][j] = new VBox();
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
                vBoxes[i][j].getChildren().clear();
                LocalDate currentDate = firstDateOfMonth.plusDays(indexDay - firstDayOfWeek);
                setGridInfo(vBoxes[i][j], currentDate, firstDateOfMonth, eventManager);
                indexDay++;
            }
        }
    }

    private void setGridInfo(VBox vBox, LocalDate currentDate, LocalDate firstDate, EventManager list) {
        Label dateLabel = new Label(String.valueOf(currentDate.getDayOfMonth()));
        dateLabel.setFont(Font.font(14));
        vBox.getChildren().add(dateLabel);
        if (currentDate.getMonth() != firstDate.getMonth()) {
            dateLabel.setDisable(true);
        } else {
            dateLabel.setDisable(false);
            if (currentDate.isEqual(LocalDate.now())) {
                dateLabel.setUnderline(true);
            } else {
                dateLabel.setUnderline(false);
            }
            for (DateEvent event : list.getEvents()) {
                Label eventLabel;
                if (vBox.getChildren().size() < 3) {
                    if (!currentDate.isBefore(event.getEventStartDate())) {
                        if ((!event.isRecurred() && currentDate.isEqual(event.getEventStartDate())) ||
                                (event.isRecurred() &&
                                        ((event.isRepeatMonth() && currentDate.getDayOfMonth() == event.getEventStartDate().getDayOfMonth()) ||
                                                (event.isRepeatWeek() && currentDate.getDayOfWeek().equals(event.getEventStartDate().getDayOfWeek()) ||
                                                        event.isRepeatDay())))) {
                            eventLabel = new Label(event.getEventName());
                            vBox.getChildren().add(eventLabel);
                        }
                    }
                } else if (vBox.getChildren().size() == 3) {
                    vBox.getChildren().remove(2);
                    eventLabel = new Label("more...");
                    vBox.getChildren().add(eventLabel);
                }
            }
        }
        vBox.setOnMouseClicked(mouseEvent -> {
            displayEventListOf(currentDate);
            mouseEvent.consume();
        });
    }

    private void displayEventListOf(LocalDate currentDate) {
        new DateListUIController().displayEventOfDate(currentDate);
        refreshTable();
    }

    @FXML
    private void onPrevMonthBtn() {
        updateMonthTable(baseDate = baseDate.minusMonths(1));
    }

    @FXML
    private void onNextMonthBtn() {
        updateMonthTable(baseDate = baseDate.plusMonths(1));
    }

    @FXML
    private void onRefreshBtn() {
        refreshTable();
    }

    public void refreshTable() {
        updateMonthTable(baseDate);
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        updateMonthTable(baseDate);
    }
}

