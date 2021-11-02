package com.example.taskmaster;

public class TaskModel {
    private String title;
    private String body;
    private String status;

    public TaskModel(String title, String body, String status) {
        this.title = title;
        this.body = body;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }
}
