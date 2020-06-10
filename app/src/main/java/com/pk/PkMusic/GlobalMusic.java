package com.pk.PkMusic;

import com.alibaba.fastjson.JSON;
import com.pk.PkMusic.utils.Song;

import java.util.HashMap;
import java.util.Map;

public class GlobalMusic {
    public static String fileName = "null";
    public static String title= "null";
    public static int duration= 0;
    public static String singer= "null";
    public static String album= "null";
    public static String year= "null";
    public static String type= "null";
    public static String size= "null";
    public static String filePath = "null";
    public static String isPlaying ="0";
    public static int time =0;
    public static Song song = null;

    public  static void setSong(Song song){
        GlobalMusic.fileName = song.getFileName();
        GlobalMusic.title = song.getTitle();
        GlobalMusic.duration = song.getDuration();
        GlobalMusic.singer = song.getSinger();
        GlobalMusic.album = song.getAlbum();
        GlobalMusic.year = song.getYear();
        GlobalMusic.type = song.getType();
        GlobalMusic.size = song.getSize();
        GlobalMusic.filePath = song.getFilePath();
        GlobalMusic.song=song;
    }

    public static JSON toJSON(){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("fileName",GlobalMusic.fileName);
        map.put("title",GlobalMusic.title);
        map.put("duration",GlobalMusic.duration);
        map.put("singer",GlobalMusic.singer);
        map.put("album",GlobalMusic.album);
        map.put("year",GlobalMusic.year);
        map.put("type",GlobalMusic.type);
        map.put("size",GlobalMusic.size);
        map.put("filePath",GlobalMusic.filePath);
        map.put("isPlaying",GlobalMusic.isPlaying);
        map.put("time",GlobalMusic.time);
        return (JSON) JSON.toJSON(map);
    }

    public static String string() {
        return  toJSON().toString();
    }

}


