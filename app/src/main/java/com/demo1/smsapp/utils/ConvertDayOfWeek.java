package com.demo1.smsapp.utils;

public class ConvertDayOfWeek {
    public static String convertDayOfWeek(String dayOfWeek){
        switch (dayOfWeek){
            case "MONDAY":
                return "Thứ hai";
            case "TUESDAY":
                return "Thứ ba";
            case "WEDNESDAY":
                return "Thứ tư";
            case "THURSDAY":
                return "Thứ năm";
            case "FRIDAY":
                return "Thứ sáu";
            case "SATURDAY":
                return "Thứ bảy";
            case "SUNDAY":
                return "Chủ nhật";
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
