package com.demo1.smsapp.utils;

import java.time.LocalDate;

public class ConvertDayOfWeek {
    public static String convertDayOfWeek(String dayOfWeek){
        switch (dayOfWeek){
            case "MONDAY":
                return "T2";
            case "TUESDAY":
                return "T3";
            case "WEDNESDAY":
                return "T4";
            case "THURSDAY":
                return "T5";
            case "FRIDAY":
                return "T6";
            case "SATURDAY":
                return "T7";
            case "SUNDAY":
                return "CN";
        }
        return "";
    }

    public static String convertShift(String shift){
        String split = shift.substring(0,1);
        switch (split){
            case "M":
                return "8:00 - 12:00";
            case "A":
                return "13:30 - 17:30";
            case "E":
                return "17:30 - 21:30";
        }
        return "";
    }
    public static String dateFormat(LocalDate value, String format) {
        String date = "";
        String day = value.getDayOfMonth() < 10 ? "0" + value.getDayOfMonth() : String.valueOf(value.getDayOfMonth());
        String month = value.getMonthValue() < 10 ? "0" + value.getMonthValue() : String.valueOf(value.getMonthValue());
        String year = value.getYear() < 10 ? "0" + value.getYear() : String.valueOf(value.getYear());
        switch (format.toLowerCase()) {
            case "dd/mm/yyyy":
                date = day + "/" + month + "/" + year;
                break;
            case "mm/dd/yyyy":
                date = month + "/" + day + "/" + year;
                break;
            case "yyyy/mm/dd":
                date = year + "/" + month + "/" + day;
                break;
            case "dd-mm-yyyy":
                date = day + "-" + month + "-" + year;
                break;
            case "mm-dd-yyy":
                date = month + "-" + day + "-" + year;
                break;
            case "yyyy-mm-dd":
                date = year + "-" + month + "-" + day;
                break;
        }
        return date;
    }
}
