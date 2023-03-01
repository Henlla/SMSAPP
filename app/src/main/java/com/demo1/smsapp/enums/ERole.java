package com.demo1.smsapp.enums;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

public enum ERole {
    Student("STUDENT"),
    Teacher("TEACHER");
    private final String text;
    ERole(String text) {
        this.text = text;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return text;
    }
}
