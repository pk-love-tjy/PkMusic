package pklovetjy.pk.pkmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.alibaba.fastjson.JSONObject;
import pklovetjy.pk.pkmusic.http.requests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhaokaiqiang
 * @ClassName: com.example.mediastore.AudioUtils
 * @Description: 音频文件帮助类
 * @date 2014-12-4 上午11:39:45
 */
public class AudioUtils {

    /**
     * 获取sd卡所有的音乐文件
     *
     * @return
     * @throws Exception
     */

//    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static ArrayList<fileData> getAllSongs(Context context) {

        ArrayList<Song> songs = null;


        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.TITLE,
//                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.YEAR,
                        MediaStore.Audio.Media.MIME_TYPE,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA
                },
                MediaStore.Audio.Media.IS_MUSIC + "!=0",
                null, null);
        songs = new ArrayList<Song>();
        assert cursor != null;
        if (cursor.moveToFirst()) {
            Song song = null;
            do {
                song = new Song();
                // 文件名
                song.setFileName(cursor.getString(1));
                // 歌曲名
                song.setTitle(cursor.getString(2));
                // 时长
//                song.setDuration(cursor.getInt(3));
                // 歌手名
                song.setSinger(cursor.getString(3));
                // 专辑名
                song.setAlbum(cursor.getString(4));
                // 年代
                if (cursor.getString(5) != null) {
                    song.setYear(cursor.getString(5));
                } else {
                    song.setYear("未知");
                }
                // 歌曲格式
                if ("audio/mpeg".equals(cursor.getString(6).trim())) {
                    song.setType("mp3");
                } else if ("audio/x-ms-wma".equals(cursor.getString(6).trim())) {
                    song.setType("wma");
                }
                // 文件大小
                if (cursor.getString(7) != null) {
                    float size = cursor.getInt(7) / 1024f / 1024f;
                    song.setSize((size + "").substring(0, 4) + "M");
                } else {
                    song.setSize("未知");
                }
                // 文件路径
                if (cursor.getString(8) != null) {
                    song.setFilePath(cursor.getString(8));
                }
                song.setSource("本地");
                songs.add(song);
            } while (cursor.moveToNext());
            cursor.close();
        }

        Map<String, ArrayList<Song>> map = new HashMap<>();
        map.put("本地音乐", songs);
        for (Song song : songs) {
            String filepath = song.getFilePath();
            String geDanName = filepath.substring(0, filepath.lastIndexOf("/"));
            if (map.containsKey(geDanName)) {
                ArrayList<Song> arrayList = map.get(geDanName);
                if (arrayList == null) {
                    arrayList = new ArrayList<Song>();
                }
                arrayList.add(song);
                map.put(geDanName, arrayList);
            } else {
                ArrayList<Song> arrayList = new ArrayList<Song>();
                arrayList.add(song);
                map.put(geDanName, arrayList);
            }
        }

        ArrayList<fileData> fileDatalist = new ArrayList<fileData>();

//        for (String key : map.keySet()) {
//            fileData data = new fileData(key.split("/")[key.split("/").length - 1], key, Objects.requireNonNull(map.get(key)));
//            fileDatalist.add(data);
//        }
//        GlobalConfig.allFileData = fileDatalist;
//        GlobalConfig.allFileData = fileDatalist;
        fileDatalist.add(new fileData("本地音乐", "local",songs));
        map.clear();
//        songs.clear();
        File file = new File(FileUtils.getMusicDir());
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isFile()) {
                if (String.valueOf(f.getName()).lastIndexOf(".") != -1) {
                    if (String.valueOf(f.getName()).substring(String.valueOf(f.getName()).lastIndexOf(".")).equals(".save")) {

                            String result  = FileUtils.readFile(f);
                            if(result.equals("")){
                                continue;
                            }
                            System.out.println("=========================================????");
                            JSONObject data = JSONObject.parseObject(result);
                            System.out.println(data.toJSONString());
                            fileData fd=new fileData(data.getString("gedanname"),data.getString("filedirabs"),requests.json2obj(data.getJSONArray("songs")));
                            fileDatalist.add(fd);

                    }
                }
            }
        }
        return fileDatalist;
    }

}