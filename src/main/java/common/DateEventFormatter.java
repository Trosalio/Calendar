package common;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Name:   Thanapong Supalak (Trosalio)
 * ID:     5810405029
 * Project Name: Calendar
 * Credit to: criticalx7
 * <p>
 * This helper class handles format date components
 * into a desirable format.
 */

public class DateEventFormatter {

    // datePattern - Default pattern of this class is dd MMM yyyy
    private String datePattern = "dd MMM yyyy";

    /**
     * Fluent method for change date pattern.
     *
     * @param pattern - Pattern to be change into
     * @return Object itself after a modification.
     */
    public DateEventFormatter ofPattern(String pattern) {
        datePattern = pattern;
        return this;
    }

    /**
     * Format all DatePickers value into a current format
     *
     * @param datePickers A list of DatePicker to be formatted.
     */
    public void formatDatePicker(DatePicker... datePickers) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        for (DatePicker datePicker : datePickers) {
            datePicker.setConverter(new StringConverter<>() {
                @Override
                public String toString(LocalDate date) {
                    return (date != null ? formatter.format(date) : "");
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, formatter);
                    } else {
                        return null;
                    }
                }
            });
            if (datePicker.getValue() != null) {
                datePicker.setValue(datePicker.getValue());
            } else {
                datePicker.setValue(LocalDate.now());
            }
        }


    }

    /**
     * Format all LocalDate TableColumn into a current format.
     *
     * @param columns A list of TableColumn from TableView
     * @param <T>     A class of where property LocalDate belongs.
     */
    @SafeVarargs
    public final <T> void formatDateColumn(TableColumn<T, LocalDate>... columns) {
        for (TableColumn<T, LocalDate> column : columns)
            column.setCellFactory(cell -> new TableCell<>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        setText(DateTimeFormatter.ofPattern(datePattern).format(item));
                    }
                }

            });
    }

    /**
     * @return Current formatter based on current pattern
     */
    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(datePattern);
    }
}

