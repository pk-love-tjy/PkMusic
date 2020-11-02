package com.pk.PkMusic.utils;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pk.PkMusic.GlobalConfig;
import com.pk.PkMusic.GlobalMusic;
import com.pk.PkMusic.http.requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MusicPlayer{


    public MediaPlayer mediaPlayer = new MediaPlayer();
    public Equalizer mEqualizer;
    public short getBands(){
        return mEqualizer.getNumberOfBands();
    }
     
    public  static MusicPlayer initMusicPlayer(){
        final MusicPlayer musicPlayer =new MusicPlayer();

        musicPlayer.mEqualizer = new Equalizer(0,musicPlayer.mediaPlayer.getAudioSessionId());

        musicPlayer.mEqualizer.setEnabled(true);



//        然后我们再获取支持的频谱：
        short bands = musicPlayer.mEqualizer.getNumberOfBands();
//        接着就是获取频谱中的等级范围，我们只需要获取最低和最高即可。
        final short minEqualizer = musicPlayer.mEqualizer.getBandLevelRange()[0];
        final short maxEqualizer = musicPlayer.mEqualizer.getBandLevelRange()[1];
        for (short band =0;band< bands;band++){
            System.out.println(musicPlayer.mEqualizer.getBandLevel(band));
        }





        musicPlayer.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 装载完毕回调
                System.out.println("jiazaiwanbi+++++++++++++++++++++");
                musicPlayer.mediaPlayer.start();
            }
        });

        musicPlayer.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 在播放完毕被回调
                if (musicPlayer.mediaPlayer.getCurrentPosition()==0 || musicPlayer.mediaPlayer.getDuration()==0){
                    return;
                }
                System.out.println("当前精度：------》之后精度----》");
                if (musicPlayer.mediaPlayer.isLooping()){
                    musicPlayer.mediaPlayer.start();
                }else {
                    musicPlayer.next();
                }
            }
        });
        musicPlayer.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (what == -38){
                    return false;
                }
                musicPlayer.next();
                return false;
            }
        });

        return musicPlayer;

    }
    public void next(){
        Cursor c = GlobalConfig.sqLitePlayList.getCursor();

        int position=0;
        if (c!=null&&c.getCount()>0){
            while (c.moveToNext()){
                Song dataSong=GlobalConfig.sqLitePlayList.getSong(c.getPosition());
                if (dataSong.title.equals(GlobalMusic.title) && dataSong.singer.equals(GlobalMusic.singer)&&dataSong.source.equals(GlobalMusic.song.source)){
                    position = c.getPosition();
                    if (position+1<c.getCount()-1){
                        position = position +1;
                    }else {
                        position= 0;
                    }
                    break;
                }
            }
            c.close();
            setSong(GlobalConfig.sqLitePlayList.getSong(position));
        }
    }
    public void prev(){
        Cursor c = GlobalConfig.sqLitePlayList.getCursor();
        int position =0;
        if (c!=null&&c.getCount()>0){
            Song dataSong;
            while (c.moveToNext()){
                dataSong=GlobalConfig.sqLitePlayList.getSong(c.getPosition());
                if (dataSong.title.equals(GlobalMusic.title) && dataSong.singer.equals(GlobalMusic.singer)&&dataSong.source.equals(GlobalMusic.song.source)){
                    position = c.getPosition();
                    if (position-1>=0){
                        position = position -1;
                    }else {
                        position= 0;
                    }
                    break;
                }
            }
            c.close();
            setSong(GlobalConfig.sqLitePlayList.getSong(position));
        }

//        if (GlobalConfig.playListSong==null||GlobalConfig.playListSong.size()==0){
//            return;
//        }
//        int po=GlobalConfig.playListSong.indexOf(GlobalMusic.song);
//        if(po==0){
//            po=GlobalConfig.playListSong.size()-1;
//        }else {
//            po-=1;
//        }
//        setSong(GlobalConfig.playListSong.get(po));
    }


    public void setSong(Song song){

        System.out.println("this is setSong");
        System.out.println(song.getFilePath());
        if (!song.getFilePath().equals("")){
            GlobalMusic.setSong(song);
            setpath(song.getFilePath());
            mediaPlayer.prepareAsync();
        }
        else if (!song.getUrl().equals("")){
            song.setUrl(song.getUrl());
            GlobalMusic.setSong(song);
            setpath(song.getUrl());
            mediaPlayer.prepareAsync();
        }
        else if(song.getId().length()>1) {
            netYuan(song);
        }
        else if(song.getId().length()<=0||song.getId()==null){
            huanYuan(song,0);
        }
    }
    private void netYuan(Song song){
        final Song oldSong = song;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> map = new HashMap<>();
                map.put("type","geturl");
                map.put("id",song.getId());
                map.put("source",song.getSource());
                String result = requests.doPost("http://39.97.226.107/eapi/", JSONObject.toJSONString(map));
                System.out.println(result);
                if(result == null){
                    result="";
                }
                if (!result.equals("")){
                    song.setUrl(result);
                    GlobalMusic.setSong(song);
                    if (GlobalConfig.sqLitePlayList.queryPo(song)==-1){
                        GlobalConfig.sqLitePlayList.insert(song);
                    }else {
                        GlobalConfig.sqLitePlayList.update(oldSong,song);
                    }
//                    GlobalConfig.playListSong.add(song);
                    setpath(result);
                    mediaPlayer.prepareAsync();
                }else {
                    huanYuan(song,0);
                }
            }
        }).start();
    }
    private void huanYuan(Song song,int yuan){
        String title = song.getTitle();
        String singer = song.getSinger();
        System.out.println("============================================zhaodaole");
        for (int i =0;i<GlobalConfig.allFileData.size();i++){
            for (int j = 0;j<GlobalConfig.allFileData.get(i).songs.size();j++){
//                    System.out.println(GlobalConfig.allFileData.get(i).songs.get(j).getTitle());
//                    System.out.println(GlobalConfig.allFileData.get(i).songs.get(j).getSinger());
                if (song.getTitle().equals(GlobalConfig.allFileData.get(i).songs.get(j).getTitle()) && song.getSinger().equals(GlobalConfig.allFileData.get(i).songs.get(j).getSinger())){
                    GlobalConfig.allFileData.get(i).songs.get(j);
                    final int index_1=i;
                    final int index_2=j;
                    new Thread(){
                        @Override
                        public void run() {
                            String key = title.replaceAll(" ","")+" "+singer.replaceAll(" ","");
                            Map<String,String> map = new HashMap<>();
                            map.put("key",key);
                            map.put("type","search");
                            String result = requests.doPost("http://39.97.226.107/eapi/", JSONObject.toJSONString(map));
                            JSONArray jsonArray = (JSONArray) JSONArray.parse(result);
                            assert jsonArray != null;
                            ArrayList<Song> arrayList = requests.jingQuePiPei(requests.json2obj(jsonArray),key);
//                                .get(index_2)=arrayList.get(0);
                            GlobalConfig.allFileData.get(index_1).songs.remove(index_2);
                            GlobalConfig.allFileData.get(index_1).songs.add(index_2,arrayList.get(yuan));
//                            int playlistindex=-1;
//                            for(Song s:GlobalConfig.playListSong){
//                                if(s.getTitle().equals(song.getTitle()) && s.getSinger().equals(song.getSinger())){
//                                    playlistindex=GlobalConfig.playListSong.indexOf(s);
//                                    break;
//                                }
//                            }
//                            在歌单中同时修改歌单
//                            if (playlistindex!=-1){
////                                GlobalConfig.playListSong.remove(playlistindex);
////                                GlobalConfig.playListSong.add(playlistindex,arrayList.get(yuan));
////                                FileUtils.saveList(GlobalConfig.playListSong);
////                            }
                            if (GlobalConfig.sqLitePlayList.queryPo(song)==-1){
                                GlobalConfig.sqLitePlayList.insert(song);
                            }else {
                                GlobalConfig.sqLitePlayList.update(song,arrayList.get(yuan));
                            }

                            GlobalConfig.allFileData.get(index_1);
                            FileUtils.saveFile(FileUtils.getMusicDir()+"/"+GlobalConfig.allFileData.get(index_1).gedanname+".save",JSONObject.toJSONString(GlobalConfig.allFileData.get(index_1)));
                            arrayList.clear();
                            setSong(GlobalConfig.allFileData.get(index_1).songs.get(index_2));
                        }
                    }.start();
                    return;
                }

            }
        }
    }

    public void setpath(String path){
        try {
            mediaPlayer.reset();
            System.out.println("++++++++++++++++++++++++++++++++");
            System.out.println(path);
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            System.out.println("-----------------------------------");
            e.printStackTrace();
        }
    }
    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    public void pause(){
        mediaPlayer.pause();
    }

    public void seekTo(int time){
        mediaPlayer.seekTo(time);
    }
    public void setVolume(float left,float right){
        mediaPlayer.setVolume(left,right);
    }
    public void start(){
        mediaPlayer.start();
    }
    public void stop(){
        mediaPlayer.stop();
    }
    public void release(){
        mediaPlayer.reset();
        mediaPlayer.release();
    }

}
