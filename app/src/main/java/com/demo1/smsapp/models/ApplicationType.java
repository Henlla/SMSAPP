package com.demo1.smsapp.models;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ApplicationType {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("file")
    private String file;
    @SerializedName("applicationById")
    private List<Application> applicationsById;

    public ApplicationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<Application> getApplicationsById() {
        return applicationsById;
    }

    public void setApplicationsById(List<Application> applicationsById) {
        this.applicationsById = applicationsById;
    }

    @NotNull
    @Override
    public String toString() {
        return name.substring(0,name.length()-5);
    }
}
