package com.pk.PkMusic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pk.pk_tjy_musicplayer.R;
import com.pk.PkMusic.http.requests;
import com.pk.PkMusic.utils.FileUtils;
import com.pk.PkMusic.utils.fileData;

import java.util.HashMap;
import java.util.Map;

public class GeDanInputActivity extends Activity implements View.OnClickListener{
    EditText qq_edit;
    Button qq_daoru;
    ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gedan_input_activity);
        qq_edit=findViewById(R.id.qq_edit);
        qq_daoru=findViewById(R.id.qq_daoru);
        back=findViewById(R.id.gedan_input_back);
        qq_daoru.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        System.out.println("=========================================>");
        switch (v.getId()){

            case R.id.qq_daoru:{
                new Thread(){
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                qq_daoru.setVisibility(View.GONE);
                            }
                        });
                        String key = String.valueOf(qq_edit.getText());
                        Map<String,String> map = new HashMap<>();
                        map.put("key", String.valueOf(qq_edit.getText()));
                        map.put("type","playlist");
                        map.put("source","qq");

                        String result = requests.doPost("http://39.97.226.107/eapi/", JSONObject.toJSONString(map));
                        assert result != null;
                        if (result.equals("")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    qq_daoru.setVisibility(View.VISIBLE);
                                    Toast.makeText(GeDanInputActivity.this, "导入失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            JSONObject json = JSONObject.parseObject(result);
                            String dirname = String.valueOf(json.get("name"));
                            JSONArray jsonlist = json.getJSONArray("songlist");
                            dirname = FileUtils.stringFilter(dirname);
                            FileUtils.mkdirs(FileUtils.getMusicDir()+"/"+dirname);
                            fileData fileData = new fileData(dirname,FileUtils.getMusicDir()+"/"+dirname,requests.json2obj(jsonlist));
                            FileUtils.saveFile(FileUtils.getMusicDir()+"/"+dirname+".save",JSONObject.toJSONString(fileData));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    qq_daoru.setVisibility(View.VISIBLE);
                                    Toast.makeText(GeDanInputActivity.this, "导入成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }.start();
                break;
            }
            case R.id.gedan_input_back:{
                finish();
                break;
            }
            default:{
                break;
            }
        }
    }
}
