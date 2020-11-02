package com.pk.PkMusic.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;

import com.pk.pk_tjy_musicplayer.R;
import com.pk.PkMusic.GlobalConfig;
import com.pk.PkMusic.adapter.PlayListAdapter;
import com.pk.PkMusic.widget.MDialog;

public class PlayListShow{
    public static ListView playlist;
    @SuppressLint("ResourceType")
    public static void show(Context context){
        MDialog mDialog = new MDialog(context);
        mDialog.setView(R.layout.playlist);
        mDialog.getView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.getView().findViewById(R.id.playlist_delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalConfig.sqLitePlayList.deleteAll();
                mDialog.getView().postInvalidate();
                ListView playlist = mDialog.getView().findViewById(R.id.playlist);
                playlist.setAdapter(new PlayListAdapter(context));
//                mDialog.dismiss();
            }
        });
        playlist = mDialog.getView().findViewById(R.id.playlist);
        playlist.setAdapter(new PlayListAdapter(context));
        mDialog.setWH((float) 1.0,(float) 0.7);
        mDialog.setGravity(Gravity.LEFT|Gravity.BOTTOM);
        mDialog.show();
    }

}
