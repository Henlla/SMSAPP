package com.demo1.smsapp.utils;

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
                return "7h30 - 11h30";
            case "A":
                return "13h30 - 17h30";
            case "E":
                return "17h30 - 21h30";
        }
        return "";
    }
}
