package com.pk.PkMusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.pk.PkMusic.GlobalConfig;
import com.pk.PkMusic.GlobalMusic;

import java.util.Timer;
import java.util.TimerTask;

public class ChangFlagTimer extends Service{

    PlayTimer playTimer;
    Timer timer;
    @Override
    public void onCreate() {
        super.onCreate();
        playTimer = new PlayTimer();
        timer = new Timer();
        timer.schedule(playTimer,0,100);
    }

    class PlayTimer extends TimerTask{
        @Override
        public void run() {
            if(GlobalMusic.song==null){
                return;
            }

            if(GlobalConfig.playActivity!=null){
                GlobalConfig.playActivity.setChang();
            }
            if(GlobalConfig.playService!=null){
                GlobalConfig.playService.setChang();
            }
            if(GlobalConfig.mainActivity!=null){
                GlobalConfig.mainActivity.setChang();
            }
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
