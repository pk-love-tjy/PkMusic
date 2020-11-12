package pklovetjy.pk.pkmusic.utils;

import android.content.Context;
import android.os.Environment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import pklovetjy.pk.pkmusic.http.requests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 文件工具类
 * Created by wcy on 2016/1/3.
 */
public class FileUtils {
    private static final String MP3 = ".mp3";
    private static final String LRC = ".lrc";

    private static String getAppDir() {
        return Environment.getExternalStorageDirectory() + "/PkMusic";
    }

    public static String getMusicDir() {
        String dir = getAppDir() + "/Music/";
        return mkdirs(dir);
    }
    public static String getPlayListDir() {
        String dir = getAppDir() + "/List/";
        return mkdirs(dir);
    }

    public static String getLrcDir() {
        String dir = getAppDir() + "/Lyric/";
        return mkdirs(dir);
    }

    public static String getLogDir() {
        String dir = getAppDir() + "/Log/";
        return mkdirs(dir);
    }

    public static String getSplashDir(Context context) {
        String dir = context.getFilesDir() + "/splash/";
        return mkdirs(dir);
    }



    public static String getCorpImagePath(Context context) {
        return context.getExternalCacheDir() + "/corp.jpg";
    }

    /**
     * 获取歌词路径<br>
     * 先从已下载文件夹中查找，如果不存在，则从歌曲文件所在文件夹查找。
     *
     * @return 如果存在返回路径，否则返回null
     */
    public static String getLrcFilePath(Object music) {

        return "歌词路径";
    }

    public static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    private static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }


    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    public static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static float b2mb(int b) {
        String mb = String.format(Locale.getDefault(), "%.2f", (float) b / 1024 / 1024);
        return Float.valueOf(mb);
    }
    public static void saveList(ArrayList<Song> songs){
        FileUtils.saveFile(FileUtils.getPlayListDir()+"playlist.save", JSONObject.toJSONString(songs));
    }
    public static ArrayList<Song> readListFile(){
        File file = new File(getPlayListDir()+"playlist.save");
        if (!file.exists()){
            return new ArrayList<Song>();
        }

        String result  = FileUtils.readFile(file);
        if (result.equals("")){
            return new ArrayList<Song>();
        }
        return requests.json2obj(JSONArray.parseArray(result));

    }


    public static String readFile(File f){
        String result = "";
        try{
        FileInputStream inputStream = new FileInputStream(f);
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader bufReader = new BufferedReader(inputReader);
        String line = "";

        while ((line = bufReader.readLine()) != null) {
            if (line.trim().equals(""))
                continue;
            result += line;
        }
        inputStream.close();
        bufReader.close();
        inputReader.close();} catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void saveFile(String path, String content) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
