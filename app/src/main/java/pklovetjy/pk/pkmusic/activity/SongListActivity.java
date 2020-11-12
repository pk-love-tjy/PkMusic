package pklovetjy.pk.pkmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pk.pk_tjy_musicplayer.R;
import pklovetjy.pk.pkmusic.GlobalConfig;
import pklovetjy.pk.pkmusic.adapter.SongAdapter;
import pklovetjy.pk.pkmusic.utils.Song;

import java.util.ArrayList;

public class SongListActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        final int position=getIntent().getIntExtra("position",0);
        TextView gedanview=(TextView)findViewById(R.id.gedanname);
        gedanview.setText(GlobalConfig.allFileData.get(position).gedanname);
        TextView filedirabsview=(TextView)findViewById(R.id.filedirabs);
        filedirabsview.setText(GlobalConfig.allFileData.get(position).filedirabs);
        ImageView add = findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Song> songs = GlobalConfig.sqLitePlayList.getSongs();
                        for (Song song:GlobalConfig.allFileData.get(position).songs){
                            int bzw=0;
                            for (Song s:songs){
                                if (s.title.equals(song.title) && s.singer.equals(song.singer) && s.source.equals(song.source)){
                                    bzw = 1;
                                }
                            }
                            if (bzw==0){
                                GlobalConfig.sqLitePlayList.insert(song);
                            }
//                    if(GlobalConfig.sqLitePlayList.queryPo(song)==-1){
//                        GlobalConfig.sqLitePlayList.insert(song);
//                    }
                        }
                    }
                }).start();
//                FileUtils.saveList(GlobalConfig.playListSong);

            }
        });


        ListView songlistview = findViewById(R.id.songlist);
        songlistview.setAdapter(new SongAdapter(this, GlobalConfig.allFileData.get(position).songs));


        songlistview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
//                GlobalConfig.playListSong.addAll(GlobalConfig.allFileData.get(position).songs);
                GlobalConfig.musicPlayer.setSong(GlobalConfig.allFileData.get(position).songs.get(arg2));

//                Toast.makeText(GlobalConfig.context,GlobalConfig.allFileData.get(position).gedanname+"-已经加入播放列表" , Toast.LENGTH_SHORT).show();
            }
        });
        songlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int p, long id) {
                Intent intent = new Intent(SongListActivity.this,MusicInfoActivity.class);

//                intent.putExtra("position",GlobalConfig.allSong.indexOf(GlobalConfig.allFileData.get(position).songs.get(p)));

                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlist);

    }



}
