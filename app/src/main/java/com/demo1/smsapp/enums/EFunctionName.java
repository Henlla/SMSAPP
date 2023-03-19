package com.demo1.smsapp.enums;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public enum EFunctionName {
    ATTENDANCE("ATTENDANCE"),
    MARK("MARK"),
    SCHEDULES("SCHEDULES"),
    APPLICATION("APPLICATION"),
    TEACHING_SCHEDULE("TEACHING_SCHEDULE"),
    ;

    private final String text;

    EFunctionName(String text) {
        this.text = text;
    }


    @NonNull
    @NotNull
    @Override
    public String toString() {
        return text;
    }
}

