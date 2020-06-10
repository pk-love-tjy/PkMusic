package com.pk.PkMusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.pk.pk_tjy_musicplayer.R;
import com.pk.PkMusic.GlobalConfig;
import com.pk.PkMusic.GlobalMusic;
import com.pk.PkMusic.utils.Song;


/**
 * 音乐播放后台服务
 * Created by wcy on 2015/11/27.
 */
public class PlayService extends Service {
    private static final String TAG = "Service";
    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }
    ImageView iv_play_pause;
    ImageView iv_next;
    @Override
    public void onCreate() {
        super.onCreate();
        GlobalConfig.playService=this;


    }

    RemoteViews bigView;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNofication(RemoteViews bigView) {

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        int channelId = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {        //Android 8.0适配
            NotificationChannel channel = new NotificationChannel(

                    String.valueOf(channelId),
                    "com.example.pk_tjy_player_chanelID",
                    NotificationManager.IMPORTANCE_HIGH);
            assert manager != null;
            channel.setSound(null,null);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, String.valueOf(channelId));
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        builder
                .setCustomContentView(bigView)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)    //通知显示的小图标，只能用alpha图层的图片进行设置
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setOngoing(true);//禁止划动删除

        Notification notification = builder.build();
        //channelId为本条通知的id
        assert manager != null;
        manager.notify(channelId, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalConfig.playService=null;
    }

        Song song;
        boolean ISPLAY=false;

        public void setChang() {
            if (song!=GlobalMusic.song||song==null||ISPLAY!=GlobalConfig.musicPlayer.mediaPlayer.isPlaying()){
                song=GlobalMusic.song;
                ISPLAY=GlobalConfig.musicPlayer.mediaPlayer.isPlaying();
                bigView = new RemoteViews(getPackageName(), R.layout.notification);
                bigView.setTextViewText(R.id.tv_title, GlobalMusic.title);
                bigView.setTextViewText(R.id.tv_singer,GlobalMusic.singer);
                if (GlobalConfig.musicPlayer.mediaPlayer.isPlaying()){
                    bigView.setImageViewResource(R.id.iv_play_pause, R.drawable.ic_media_pause);
                }else {
                    bigView.setImageViewResource(R.id.iv_play_pause, R.drawable.ic_media_play);
                }
                createNofication(bigView);
            }
    }
}
