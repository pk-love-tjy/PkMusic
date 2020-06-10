package com.pk.PkMusic;

import android.content.Context;
import android.webkit.WebView;

import com.pk.PkMusic.activity.MainActivity;
import com.pk.PkMusic.activity.PlayActivity;

import com.pk.PkMusic.service.PlayService;
import com.pk.PkMusic.utils.MusicPlayer;
import com.pk.PkMusic.utils.Song;
import com.pk.PkMusic.utils.fileData;
import com.pk.PkMusic.utils.sqlit.SQLitePlayList;

import java.util.ArrayList;


public class GlobalConfig {
    public static Context context = null;
    public static WebView webview = null;
    public static ArrayList<Song> allSong = null;
    public static ArrayList<Song> playListSong = new ArrayList<>();
    public static ArrayList<fileData> allFileData = new ArrayList<>();;
    public static MusicPlayer musicPlayer=MusicPlayer.initMusicPlayer();
    public static final String BROADCAST_ACTION = "com.example.pk_tjy_player.player";

    public static boolean CHANGFLAG = false;

    public static MainActivity mainActivity = null;
    public static PlayActivity playActivity = null;
    public static PlayService playService = null;

    public static SQLitePlayList sqLitePlayList =null;


}
