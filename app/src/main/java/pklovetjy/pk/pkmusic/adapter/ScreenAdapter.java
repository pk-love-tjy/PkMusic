package pklovetjy.pk.pkmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import pklovetjy.pk.pkmusic.utils.Song;

import java.util.List;


public class ScreenAdapter extends MyAdapder{
    private Context context = null;
    private List<Song> list = null;
    public ScreenAdapter(Context context, List<Song> list) {
        this.context = context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getMyView(convertView,context,list.get(position));
    }

}