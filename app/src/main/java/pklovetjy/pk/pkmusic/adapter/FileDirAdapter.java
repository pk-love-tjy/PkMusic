package pklovetjy.pk.pkmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pk.pk_tjy_musicplayer.R;
import pklovetjy.pk.pkmusic.utils.fileData;

import java.util.List;

public class FileDirAdapter extends BaseAdapter {
    private Context context = null;
    private List<fileData> list = null;
    public FileDirAdapter(Context context, List<fileData> list) {
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.view_holder_filedir, null, true);
            mHolder.text_item_listview_gedanname = (TextView) convertView.findViewById(R.id.gedanname);
            mHolder.text_item_listview_filedirabs = (TextView) convertView.findViewById(R.id.filedirabs);

//            mHolder.listView_item_song = (ListView)convertView.findViewById(R.id.songlist) ;

            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        fileData data=list.get(position);
        String gedanname = data.gedanname;
        String filedirabs = data.filedirabs;
        mHolder.text_item_listview_gedanname.setText(gedanname);
        mHolder.text_item_listview_filedirabs.setText(filedirabs);

        return convertView;
    }


    private class ViewHolder {
        private TextView text_item_listview_gedanname;
        private TextView text_item_listview_filedirabs;
//        private ListView listView_item_song;
    }
}