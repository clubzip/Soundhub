package com.example.login;

import java.util.ArrayList;

public class Music {

    String create_date;
    String projectID;
    String description;
    String like;
    String last_update;
    ArrayList<Commit> commits;
    ArrayList<Request> requests;
    String admin;

    public Music(String create_date, String projectID, String description, String like, String last_update, ArrayList<Commit> commits, ArrayList<Request> requests, String admin) {
        this.create_date = create_date;
        this.projectID = projectID;
        this.description = description;
        this.like = like;
        this.last_update = last_update;
        this.commits = commits;
        this.requests = requests;
        this.admin = admin;
    }

    public String getCreate_date() {
        return create_date;
    }
    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getProjectID() {
        return projectID;
    }
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLike() {
        return like;
    }
    public void setLike(String like) {
        this.like = like;
    }

    public String getLast_update() {
        return last_update;
    }
    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public ArrayList<Commit> getCommits() {
        return commits;
    }
    public void setCommits(ArrayList<Commit> commits) {
        this.commits = commits;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }
    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    public String getAdmin() {
        return admin;
    }
    public void setAdmin(String admin) {
        this.admin = admin;
    }
}
