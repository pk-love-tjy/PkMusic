package com.pk.PkMusic.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.pk.pk_tjy_musicplayer.R;

import com.pk.PkMusic.GlobalConfig;
import com.pk.PkMusic.GlobalMusic;
import com.pk.PkMusic.adapter.FileDirAdapter;
import com.pk.PkMusic.service.ChangFlagTimer;
import com.pk.PkMusic.service.PlayService;
import com.pk.PkMusic.utils.AudioUtils;
import com.pk.PkMusic.utils.PermissionReq;

import com.pk.PkMusic.utils.sqlit.SQLitePlayList;
import com.pk.PkMusic.utils.ScreenUtils;
import com.pk.PkMusic.utils.Song;
//import com.google.android.material.internal.NavigationMenu;
//import com.google.android.material.navigation.NavigationView;


@SuppressLint("Registered")
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Song song = null;
    ImageButton iv_play_bar_play;
    ImageButton iv_play_bar_next;
    ImageButton v_play_bar_playlist;
    ImageButton menu_button;
    ImageButton screen_button;
    DrawerLayout drawerLayout;
    TextView tv_play_bar_title;
    TextView tv_play_bar_artist;
    LinearLayout play_activity;

    @Override
    protected void onResume() {
        super.onResume();
        PermissionReq.with(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .result(new PermissionReq.Result() {

                    @Override
                    public void onGranted() {
                        GlobalConfig.allFileData=AudioUtils.getAllSongs(GlobalConfig.context);
                    }

                    @Override
                    public void onDenied() {
                        System.out.println("fuck you----你不配=============");
                    }
                })
                .request();
        ListView filedirlist = findViewById(R.id.filedirlist);
        filedirlist.setAdapter(new FileDirAdapter(this, GlobalConfig.allFileData));
        filedirlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, SongListActivity.class);
                intent.putExtra("position", arg2);
                startActivity(intent);
//                Toast.makeText(MainActivity.this, new String(String.valueOf(arg2)), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        startService(new Intent(this, ChangFlagTimer.class));
        startService(new Intent(this, PlayService.class));
        GlobalConfig.context = getApplicationContext();
        GlobalConfig.mainActivity = this;
        ScreenUtils.init(this);
        initListener();

//        GlobalConfig.playListSong=FileUtils.readListFile();

        GlobalConfig.sqLitePlayList  = new SQLitePlayList(this);



//        Toast.makeText(this, Arrays.toString(sqLiteHelper.select().getColumnNames()), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalConfig.mainActivity = null;
    }




    public void initListener(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutIndex);
        tv_play_bar_title = findViewById(R.id.tv_play_bar_title);
        tv_play_bar_artist = findViewById(R.id.tv_play_bar_artist);

        iv_play_bar_play=findViewById(R.id.iv_play_bar_play);
        iv_play_bar_next=findViewById(R.id.iv_play_bar_next);
        v_play_bar_playlist=findViewById(R.id.v_play_bar_playlist);
        menu_button=findViewById(R.id.menu_button);
        screen_button=findViewById(R.id.screen_button);
        play_activity=findViewById(R.id.play_activity);

        iv_play_bar_play.setOnClickListener(this);
        iv_play_bar_next.setOnClickListener(this);
        v_play_bar_playlist.setOnClickListener(this);
        menu_button.setOnClickListener(this);
        screen_button.setOnClickListener(this);
        play_activity.setOnClickListener(this);
    }





//    NavigationView navigationView;
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    @SuppressLint("ResourceType")
//    public void shownv(){
//        navigationView = new NavigationView(this);
//        navigationView.setCheckedItem(R.menu.navigation_menu);
//        navigationView.showContextMenu(100,100);
//    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.iv_play_bar_play:{
                if (GlobalConfig.musicPlayer.mediaPlayer.isPlaying()){
                    GlobalConfig.musicPlayer.mediaPlayer.pause();
                    iv_play_bar_play.setImageDrawable(getDrawable(R.drawable.ic_media_play));

                }else if(GlobalMusic.song!=null){
                    GlobalConfig.musicPlayer.mediaPlayer.start();
                    iv_play_bar_play.setImageDrawable(getDrawable(R.drawable.ic_media_pause));
                }else {
                    Toast.makeText(this, "你的播放列表中无歌曲", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.iv_play_bar_next:{
                GlobalConfig.musicPlayer.next();
                break;
            }
            case R.id.v_play_bar_playlist:{
                PlayListShow.show(this);
                break;
            }
            case R.id.menu_button:{
//                drawerLayout.openDrawer(GravityCompat.START);
                startActivity(new Intent(this,GeDanInputActivity.class));

                break;
            }
            case R.id.screen_button:{
                startActivity(new Intent(MainActivity.this,ScreenActivity.class));
                break;
            }
            case R.id.play_activity:{
                startActivity(new Intent(MainActivity.this,PlayActivity.class));
                break;
            }
            default:{
                break;
            }
        }
    }

    public void setChang() {
        if (song != GlobalMusic.song || song != null) {
            song = GlobalMusic.song;
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tv_play_bar_title.setText(GlobalMusic.title);
                    tv_play_bar_artist.setText(GlobalMusic.singer);
                    if (GlobalConfig.musicPlayer.mediaPlayer.isPlaying()){
                        iv_play_bar_play.setImageDrawable(getDrawable(R.drawable.ic_media_pause));
                    }else{
                        iv_play_bar_play.setImageDrawable(getDrawable(R.drawable.ic_media_play));
                    }
                }
            });
        }

    }
}







