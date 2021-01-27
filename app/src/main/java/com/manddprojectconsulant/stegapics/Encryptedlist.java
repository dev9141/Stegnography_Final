package com.manddprojectconsulant.stegapics;

public class Encryptedlist {

    String filepath;
    String title;

    public Encryptedlist(String filepath, String title){
       this.filepath = filepath;
       this.title=title;
    }

    public Encryptedlist() {
    }

    public String getFilepath() {
        return filepath;
    }

    public String getTitle() {
        return title;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
