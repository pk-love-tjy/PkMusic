package pklovetjy.pk.pkmusic.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.pk.pk_tjy_musicplayer.R;
import pklovetjy.pk.pkmusic.adapter.ScreenAdapter;
import pklovetjy.pk.pkmusic.http.requests;
import pklovetjy.pk.pkmusic.python.MyMusicApi;
import pklovetjy.pk.pkmusic.utils.FileUtils;
import pklovetjy.pk.pkmusic.utils.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScreenActivity extends Activity implements View.OnClickListener, TextWatcher {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.screen_activity);
        editText = findViewById(R.id.screen_edit);
        scrren_button = findViewById(R.id.screen_button);

        scrren_back = findViewById(R.id.scrren_back);

        listView = findViewById(R.id.screen_listview);

        scrren_button.setOnClickListener(this);
        scrren_back.setOnClickListener(this);
        editText.addTextChangedListener(this);
    }

    EditText editText;
    ImageButton scrren_button;
    ImageButton scrren_back;
    ListView listView;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.screen_button:{
                System.out.println("result==================>");
                netscreen();
                break;
            }
            case R.id.scrren_back:{
                finish();
                break;
            }
            default:{
                break;
            }

        }
    }
    ArrayList<Song> arrayList = null;
    public void netscreen(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                String key = (String.valueOf(editText.getText()));
                Map<String,String> map = new HashMap<>();
                map.put("key",key);
                map.put("type","search");
                key = FileUtils.stringFilter(key);
//                String result = requests.doPost("http://39.97.226.107/eapi/", JSONObject.toJSONString(map));

                MyMusicApi musicApi = new MyMusicApi();
                String result = musicApi.searchSting(key);

                JSONArray jsonArray = (JSONArray) JSONArray.parse(result);
                assert jsonArray != null;
                arrayList = requests.jingQuePiPei(requests.json2obj(jsonArray), key);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更改UI；
                        listView.setAdapter(new ScreenAdapter(ScreenActivity.this, arrayList));
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//                            @Override
//                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                                    long arg3) {
//                                // TODO Auto-generated method stub
//                                GlobalConfig.musicPlayer.setSong(arrayList.get(arg2));
//                            }
//                        });
                    }
                });

            }
        }).start();
    }


    /**
     * 编辑框的内容发生改变之前的回调方法
     */
    @SuppressLint("LongLogTag")
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i("MyEditTextChangeListener", "beforeTextChanged---" + charSequence.toString());
    }

    /**
     * 编辑框的内容正在发生改变时的回调方法 >>用户正在输入
     * 我们可以在这里实时地 通过搜索匹配用户的输入
     */
    @SuppressLint("LongLogTag")
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i("MyEditTextChangeListener", "onTextChanged---" + charSequence.toString());
    }

    /**
     * 编辑框的内容改变以后,用户没有继续输入时 的回调方法
     */
    @SuppressLint("LongLogTag")
    @Override
    public void afterTextChanged(Editable editable) {
        Log.i("MyEditTextChangeListener", "afterTextChanged---");
    }

}
