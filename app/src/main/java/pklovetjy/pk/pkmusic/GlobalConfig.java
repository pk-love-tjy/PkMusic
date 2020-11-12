package pklovetjy.pk.pkmusic;

import android.content.Context;
import android.webkit.WebView;

import pklovetjy.pk.pkmusic.activity.MainActivity;
import pklovetjy.pk.pkmusic.activity.PlayActivity;

import pklovetjy.pk.pkmusic.service.PlayService;
import pklovetjy.pk.pkmusic.utils.MusicPlayer;
import pklovetjy.pk.pkmusic.utils.Song;
import pklovetjy.pk.pkmusic.utils.fileData;
import pklovetjy.pk.pkmusic.utils.sqlit.SQLitePlayList;

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
