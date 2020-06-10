package com.pk.PkMusic.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pk.pk_tjy_musicplayer.R;
import com.pk.PkMusic.GlobalConfig;
import com.pk.PkMusic.GlobalMusic;
import com.pk.PkMusic.http.requests;
import com.pk.PkMusic.utils.geci.DefaultLrcBuilder;
import com.pk.PkMusic.utils.FileUtils;
import com.pk.PkMusic.utils.Song;
import com.pk.PkMusic.utils.geci.ILrcBuilder;
import com.pk.PkMusic.utils.geci.ILrcView;
import com.pk.PkMusic.utils.geci.ILrcViewListener;
import com.pk.PkMusic.utils.geci.LrcRow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.TimerTask;


public class PlayActivity extends Activity implements View.OnClickListener {
    public final static String TAG = "PlayActity";

    Song song;
    //自定义LrcView，用来展示歌词
    ILrcView mLrcView;

    TextView tv_title;
    TextView tv_artist;

    TextView tv_current_time;
    TextView tv_total_time;

    SeekBar sb_progress;
    SeekBar sb_volume;
    AudioManager audioManager;

    ImageButton iv_back;
    ImageView iv_prev;
    ImageView iv_play;
    ImageView iv_next;

    ImageButton junhengqi;

    long time;

    boolean isclick = false;
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_back:{
                PlayActivity.this.finish();
                break;
            }
            case R.id.iv_prev:{
                GlobalConfig.musicPlayer.prev();
                break;
            }
            case R.id.iv_next:{
                GlobalConfig.musicPlayer.next();
                break;
            }
            case R.id.iv_play:{
                if (GlobalConfig.musicPlayer.mediaPlayer.isPlaying()){
                    GlobalConfig.musicPlayer.mediaPlayer.pause();
                }else {
                    GlobalConfig.musicPlayer.mediaPlayer.start();
                }
                break;
            }
            case R.id.junhengqi:{
                startActivity(new Intent(PlayActivity.this,JunHengQi.class));
                break;
            }
            default:{
                break;
            }

        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取自定义的LrcView
        GlobalConfig.playActivity=this;
        setContentView(R.layout.fragment_play);
        tv_title =findViewById(R.id.tv_title);
        tv_artist =findViewById(R.id.tv_artist);

        tv_current_time =findViewById(R.id.tv_current_time);
        tv_total_time =findViewById(R.id.tv_total_time);


         iv_back =findViewById(R.id.iv_back);
        iv_prev =findViewById(R.id.iv_prev);
        iv_play =findViewById(R.id.iv_play);
        iv_next =findViewById(R.id.iv_next);

        junhengqi=findViewById(R.id.junhengqi);

        iv_back.setOnClickListener(this);
        iv_prev.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_next.setOnClickListener(this);

        junhengqi.setOnClickListener(this);





        sb_progress =findViewById(R.id.sb_progress);
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    GlobalConfig.musicPlayer.mediaPlayer.seekTo(progress*GlobalConfig.musicPlayer.mediaPlayer.getDuration()/100);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_volume =(SeekBar) findViewById(R.id.sb_volume);
        sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress*max/100,AudioManager.FLAG_PLAY_SOUND);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mLrcView=(ILrcView)findViewById(R.id.lrcView);
        //设置自定义的LrcView上下拖动歌词时监听
        mLrcView.setListener(new ILrcViewListener() {
            //当歌词被用户上下拖动的时候回调该方法,从高亮的那一句歌词开始播放
            public void onLrcSeeked(int newPosition, LrcRow row) {
                if (GlobalConfig.musicPlayer != null) {
                    GlobalConfig.musicPlayer.seekTo((int) row.time);
                }
            }
        });
    }
    public String time_times(int time){
        if (time==0){
            return "00:00";
        }
        time = time/1000;
        int m = time/60;
        int s = time%60;
        String ms="";
        String ss="";
        if (m<10){ms ="0"+m;}else {ms= String.valueOf(m);}
        if (s<10){ss ="0"+s;}else {ss = String.valueOf(s);
        }
        return ms+":"+ss;
    }
    public void setChang(){
        if (GlobalMusic.song==null){

            return;
        }
        PlayActivity.this.runOnUiThread(new Runnable() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void run() {
                if (GlobalConfig.musicPlayer.mediaPlayer.isPlaying()){
                    iv_play.setImageDrawable(getDrawable(R.drawable.ic_media_pause));
                }else {
                    iv_play.setImageDrawable(getDrawable(R.drawable.ic_media_play));
                }
                tv_title.setText(GlobalMusic.title);
                tv_artist.setText(GlobalMusic.singer);

                if (GlobalMusic.song!=song){
                    if (GlobalConfig.musicPlayer.mediaPlayer.getDuration()>0){
                        song=GlobalMusic.song;
                        init();
                    }

                }
//
//
                if(GlobalConfig.musicPlayer.isPlaying()){
                    long timePassed = GlobalConfig.musicPlayer.getCurrentPosition();
                    mLrcView.seekLrcToTime(timePassed);
                }
                if (GlobalConfig.musicPlayer.mediaPlayer.isPlaying()){
                    tv_current_time.setText(time_times(GlobalConfig.musicPlayer.mediaPlayer.getCurrentPosition()));
                    tv_total_time.setText(time_times(GlobalConfig.musicPlayer.mediaPlayer.getDuration()));
                    sb_progress.setProgress(100*GlobalConfig.musicPlayer.mediaPlayer.getCurrentPosition()/GlobalConfig.musicPlayer.mediaPlayer.getDuration());

                }
                int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                sb_volume.setProgress((100*current)/max);
            }
        });

    }

    public void init(){
        //从assets目录下读取歌词文件内容
        try {
            File file=new File(FileUtils.getLrcDir()+(GlobalMusic.song.getTitle()+"_"+GlobalMusic.song.getSinger()+".lrc"));
            if (file.exists()){
                FileInputStream inputStream =new FileInputStream(file);
                InputStreamReader inputReader = new InputStreamReader(inputStream);
                BufferedReader bufReader = new BufferedReader(inputReader);
                String line="";
                String result="";
                while((line = bufReader.readLine()) != null){
                    if(line.trim().equals(""))
                        continue;
                    result += line + "\r\n";
                }
                inputStream.close();
                bufReader.close();
                inputReader.close();
                startLrcPlay(result);
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //滚动歌词
                        String text=requests.getKuWoSongLrc(GlobalMusic.song.getTitle(),GlobalMusic.song.getSinger(),GlobalConfig.musicPlayer.mediaPlayer.getDuration());
                        System.out.println("------------------------------------------------------------------>歌词下载");
                        System.out.println(text);
                        System.out.println("------------------------------------------------------------------>歌词下载");


                        StringReader reader = new StringReader(text);
                        BufferedReader bufReader = new BufferedReader(reader);
                        String line="";
                        String result="";
                        try {
                            while ((line = bufReader.readLine()) != null) {
                                if (line.trim().equals(""))
                                    continue;
                                result += line + "\r\n";
                            }

                            //开始播放歌曲并同步展示歌词
                            startLrcPlay(result);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
;
    /**
     * 展示歌词
     */
    public void startLrcPlay(String result){
        //解析歌词构造器
        ILrcBuilder builder = new DefaultLrcBuilder();
        //解析歌词返回LrcRow集合
        List<LrcRow> rows = builder.getLrcRows(result);
        //将得到的歌词集合传给mLrcView用来展示
        mLrcView.setLrc(rows);
    }



    /**
     * 展示歌曲的定时任务
     */
    class LrcTask extends TimerTask{
        @Override
        public void run() {
            //获取歌曲播放的位置
            final long timePassed = GlobalConfig.musicPlayer.getCurrentPosition();
            PlayActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //滚动歌词
                    mLrcView.seekLrcToTime(timePassed);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalConfig.playActivity = null;
    }
}
