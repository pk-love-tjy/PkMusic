package pklovetjy.pk.pkmusic.utils;

import java.util.ArrayList;

public class fileData {
    public String gedanname;
    public String filedirabs;
    public ArrayList<Song> songs;

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public void setFiledirabs(String filedirabs) {
        this.filedirabs = filedirabs;
    }

    public void setGedanname(String gedanname) {
        this.gedanname = gedanname;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public String getFiledirabs() {
        return filedirabs;
    }

    public String getGedanname() {
        return gedanname;
    }

    public fileData(String gedanname, String filedirabs, ArrayList<Song> songs){
        this.gedanname=gedanname;
        this.filedirabs=filedirabs;
        this.songs=songs;
    }
}
