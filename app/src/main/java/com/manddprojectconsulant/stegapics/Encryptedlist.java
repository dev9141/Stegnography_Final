package com.manddprojectconsulant.stegapics;

public class Encryptedlist {

    int imageid;
    String title;

    public Encryptedlist(int imageid,String title){
       this.imageid=imageid;
       this.title=title;
    }

    public int getImageid() {
        return imageid;
    }

    public String getTitle() {
        return title;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
