package pklovetjy.pk.pkmusic.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.pk.pk_tjy_musicplayer.R;
import pklovetjy.pk.pkmusic.GlobalConfig;
import pklovetjy.pk.pkmusic.utils.Song;

public class MusicInfoActivity extends Activity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_info);
        int position = getIntent().getIntExtra("position", 0);


//        final Song song = GlobalConfig.allSong.get(position);
        Song song = new Song();
        TextView et_music_info_title = (TextView) findViewById(R.id.et_music_info_title);
        TextView et_music_info_artist = (TextView) findViewById(R.id.et_music_info_artist);
        TextView et_music_info_album = (TextView) findViewById(R.id.et_music_info_album);
        TextView tv_music_info_duration_m = (TextView) findViewById(R.id.tv_music_info_duration_m);
        TextView tv_music_info_duration_s = (TextView) findViewById(R.id.tv_music_info_duration_s);
        TextView tv_music_info_file_name = (TextView) findViewById(R.id.tv_music_info_file_name);
        TextView tv_music_info_file_size = (TextView) findViewById(R.id.tv_music_info_file_size);
        TextView tv_music_info_file_path = (TextView) findViewById(R.id.tv_music_info_file_path);

        et_music_info_title.setText("标题：" + song.getTitle());
        et_music_info_artist.setText("艺术家：" + song.getSinger());
        et_music_info_album.setText("专辑：" + song.getAlbum());
        int s=song.getDuration()/1000;


        tv_music_info_duration_s.setText("播放时长：" + song.getDuration());
        tv_music_info_duration_m.setText("播放时长(毫秒)：" + String.valueOf(s/60)+":" +String.valueOf(s%60));


        tv_music_info_file_name.setText("文件名称：" + song.getFileName());
        tv_music_info_file_size.setText("文件大小：" + String.valueOf(song.getSize()));
        tv_music_info_file_path.setText("文件路径：" + song.getFilePath());




        Button add_play_list_song=(Button)findViewById(R.id.add_play_list_song);
        add_play_list_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalConfig.sqLitePlayList.queryPo(song)!=-1){
                    Toast.makeText(MusicInfoActivity.this, song.getTitle()+"-已存在播放列表", Toast.LENGTH_SHORT).show();
                }else {
                    GlobalConfig.sqLitePlayList.insert(song);
                    Toast.makeText(MusicInfoActivity.this, song.getTitle()+"-加入播放列表成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
