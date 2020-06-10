package com.pk.PkMusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.pk.pk_tjy_musicplayer.R;
import com.pk.PkMusic.GlobalConfig;
import com.pk.PkMusic.utils.Song;

public class MyAdapder extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }

    private TextView item_text_title;
    private TextView item_text_singer;
    private ImageView item_image_source;
    private LinearLayout item_layout;
    private ImageView item_iamge_del;
    @SuppressLint("UseCompatLoadingForDrawables")
    public View getMyView(View convertView, Context context, Song data){
        if (convertView==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.view_holder_music, null, true);
            item_text_title = (TextView) convertView.findViewById(R.id.tv_title);
            item_text_singer = (TextView) convertView.findViewById(R.id.tv_singer);
            item_image_source = (ImageView) convertView.findViewById(R.id.tv_source);
            item_layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            item_iamge_del = (ImageView) convertView.findViewById(R.id.item_del);
            convertView.setTag(this);

        }else {
            MyAdapder viewHolder = (MyAdapder) convertView.getTag();
            this.item_layout=viewHolder.item_layout;
            this.item_text_title =viewHolder.item_text_title;
            this.item_text_singer =viewHolder.item_text_singer;
            this.item_image_source =viewHolder.item_image_source;
            this.item_iamge_del = viewHolder.item_iamge_del;
        }


        String gedanname = data.getTitle();
        String filedirabs = data.getSinger();
        String source = data.getSource();


        this.item_text_title.setText(gedanname);
        this.item_text_singer.setText(filedirabs);
        System.out.println(JSONObject.toJSONString(data));

        this.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalConfig.musicPlayer.setSong(data);
            }
        });

//        this.item_iamge_del.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        switch (source){
            case "qq":{
                this.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_qq));
                break;
            }
            case "netease":{
                this.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_wangyi));
                break;
            }
            case "kuwo":{
                this.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_kuwo));
                break;
            }
            case "kugou":{
                this.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_kugou));
                break;
            }
            case "migu":{
                this.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_migu));
                break;
            }
            default:{
                this.item_image_source.setImageDrawable(context.getDrawable(R.drawable.ic_pkmusic));
                break;
            }
        }
        return convertView;
    }
}
