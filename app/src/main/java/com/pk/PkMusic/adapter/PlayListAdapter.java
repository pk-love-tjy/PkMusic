package com.pk.PkMusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.pk.pk_tjy_musicplayer.R;
import com.pk.PkMusic.GlobalConfig;
import com.pk.PkMusic.activity.PlayListShow;
import com.pk.PkMusic.utils.Song;

public class PlayListAdapter extends MyAdapder {

    private Context context = null;

    public PlayListAdapter(Context context) {
        this.context = context;

    }
    @Override
    public int getCount() {
        Cursor c = GlobalConfig.sqLitePlayList.getCursor();
        int num = c.getCount();
        System.out.println(num);
        c.close();
        return num;
    }
    @Override
    public Object getItem(int position) {
        System.out.println(position);
        return GlobalConfig.sqLitePlayList.getSong(position);
    }
    @Override
    public long getItemId(int position) {
        System.out.println(position);
        return position;
    }
    private class Holder{
        private TextView item_text_title;
        private TextView item_text_singer;
        private ImageView item_image_source;
        private LinearLayout item_layout;
        private ImageView item_iamge_del;
    }
    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//         super.getMyView(convertView,context,GlobalConfig.sqLitePlayList.getSong(position));
         Song data = GlobalConfig.sqLitePlayList.getSong(position);
         Holder mholder = new Holder();
         if (convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.view_holder_music, null, true);
            mholder.item_text_title = (TextView) convertView.findViewById(R.id.tv_title);
            mholder.item_text_singer = (TextView) convertView.findViewById(R.id.tv_singer);
            mholder.item_image_source = (ImageView) convertView.findViewById(R.id.tv_source);
            mholder.item_layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            mholder.item_iamge_del = (ImageView) convertView.findViewById(R.id.item_del);
            convertView.setTag(mholder);

        }else {
            mholder = (Holder) convertView.getTag();
        }


        String gedanname = data.getTitle();
        String filedirabs = data.getSinger();
        String source = data.getSource();


        mholder.item_text_title.setText(gedanname);
        mholder.item_text_singer.setText(filedirabs);
        System.out.println(JSONObject.toJSONString(data));

        mholder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(JSONObject.toJSONString(data));
                GlobalConfig.musicPlayer.setSong(data);
            }
        });
        final View v = convertView;
        mholder.item_iamge_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalConfig.sqLitePlayList.delete(data);
//                v.postInvalidate();
                PlayListShow.playlist.setAdapter(new PlayListAdapter(context));
            }
        });

        switch (source){
            case "qq":{
                mholder.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_qq));
                break;
            }
            case "netease":{
                mholder.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_wangyi));
                break;
            }
            case "kuwo":{
                mholder.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_kuwo));
                break;
            }
            case "kugou":{
                mholder.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_kugou));
                break;
            }
            case "migu":{
                mholder.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_migu));
                break;
            }
            default:{
                mholder.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_pkmusic));
                break;
            }
        }
        return convertView;
    }

}
