package com.pk.PkMusic.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pk.PkMusic.GlobalConfig;

import java.util.ArrayList;

public class JunHengQi extends Activity {
    SeekBar seekBar1;
    SeekBar seekBar2;
    ArrayList<SeekBar> arraySeekBar;
    short min = GlobalConfig.musicPlayer.mEqualizer.getBandLevelRange()[0];
    short max = GlobalConfig.musicPlayer.mEqualizer.getBandLevelRange()[1];
    int seekp=50;
    int all;
    LinearLayout layout;

    /**
     * 初始化均衡控制器
     */
    @SuppressLint("SetTextI18n")
    private void setupEqualizer()
    {
        // 以MediaPlayer的AudioSessionId创建Equalizer
        // 相当于设置Equalizer负责控制该MediaPlayer

        // 启用均衡控制效果
//        GlobalConfig.musicPlayer.mEqualizer.setEnabled(true);

        // 获取均衡控制器支持最小值和最大值

        // 获取均衡控制器支持的所有频率
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize((float)100);
        textView.setText("均衡器");
        textView.setTextColor(Color.RED);
        layout.addView(textView);

        short brands = GlobalConfig.musicPlayer.mEqualizer.getNumberOfBands();
        for (short i = 0; i < brands; i++)
        {
            TextView eqTextView = new TextView(this);
            // 创建一个TextView，用于显示频率
            eqTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            eqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            // 设置该均衡控制器的频率
            eqTextView.setText((GlobalConfig.musicPlayer.mEqualizer.getCenterFreq(i) / 1000)
                    + "Hz");
            layout.addView(eqTextView);
            // 创建一个水平排列组件的LinearLayout
            LinearLayout tmpLayout = new LinearLayout(this);
            tmpLayout.setOrientation(LinearLayout.HORIZONTAL);
            // 创建显示均衡控制器最小值的TextView
            TextView minDbTextView = new TextView(this);
            minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            // 显示均衡控制器的最小值
            minDbTextView.setText(String.valueOf((int)(min/100))+ "dB");
            // 创建显示均衡控制器最大值的TextView
            TextView maxDbTextView = new TextView(this);
            maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            // 显示均衡控制器的最大值
            maxDbTextView.setText(String.valueOf((int)(max / 100)) + " dB");
            LinearLayout.LayoutParams layoutParams = new
                    LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            // 定义SeekBar做为调整工具

            ;
            arraySeekBar.add(new SeekBar(this));
            arraySeekBar.get((int)i).setLayoutParams(layoutParams);
            arraySeekBar.get((int)i).setMax(max-min);
            arraySeekBar.get((int)i).setProgress(GlobalConfig.musicPlayer.mEqualizer.getBandLevel(i)-min);
            final short brand = i;
            // 为SeekBar的拖动事件设置事件监听器
            arraySeekBar.get((int)i).setOnSeekBarChangeListener(new SeekBar
                    .OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar,
                                              int progress, boolean fromUser)
                {
                    // 设置该频率的均衡值
                    GlobalConfig.musicPlayer.mEqualizer.setBandLevel(brand,
                            (short) (progress + min));
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                }
            });
            // 使用水平排列组件的LinearLayout“盛装”3个组件
            tmpLayout.addView(minDbTextView);
            tmpLayout.addView(arraySeekBar.get((int)i));
            tmpLayout.addView(maxDbTextView);
            // 将水平排列组件的LinearLayout添加到myLayout容器中
            layout.addView(tmpLayout);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arraySeekBar = new ArrayList<>();

        layout = new LinearLayout(this);//代码创建布局
        layout.setOrientation(LinearLayout.VERTICAL);//设置为线性布局-上下排列

        setupEqualizer();
        setContentView(layout);//将布局添加到 Activity

    }
}
