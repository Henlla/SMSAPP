package com.demo1.smsapp.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatHelper {
    public static String Format(String type, String value) {
        String formated = "";
        switch (type.toUpperCase()) {
            case "DATE":
                Date date = new Date(value);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                formated = sdf.format(date);
                break;
            default:
                formated = "Không tìm thấy dâng format của bạn";
                break;
        }
        return formated;
    }
}
