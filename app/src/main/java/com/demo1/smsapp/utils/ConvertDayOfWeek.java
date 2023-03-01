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
}
