/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Admin
 */
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

final class ViewUtils {
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat MONEY_FORMATTER = new DecimalFormat("#,##0");

    private ViewUtils() {
    }

    static String formatDate(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMATTER);
    }

    static String blank(String value) {
        return value == null ? "" : value;
    }

    static String formatMoney(double value) {
        return MONEY_FORMATTER.format(value) + " VND";
    }

    static String formatRawMoney(double value) {
        return MONEY_FORMATTER.format(value);
    }

    static double parseMoney(String value) {
        String normalized = value.trim().replace(",", "").replace(" ", "");
        return Double.parseDouble(normalized);
    }
}
