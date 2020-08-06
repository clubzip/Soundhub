package com.example.login;

public class Commit {

    String date;
    String artistID;
    String commitID;
    String category;

    public Commit(String date, String artistID, String commitID, String category) {
        this.date = date;
        this.artistID = artistID;
        this.commitID = commitID;
        this.category = category;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getArtistID() {
        return artistID;
    }
    public void setArtistID(String artistID) {
        this.artistID = artistID;
    }

    public String getCommitID() {
        return commitID;
    }
    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
