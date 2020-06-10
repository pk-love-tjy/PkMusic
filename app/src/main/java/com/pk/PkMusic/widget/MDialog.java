package com.pk.PkMusic.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IdRes;

import com.pk.pk_tjy_musicplayer.R;

public class MDialog {
   private Context context;
   private View view;
   private AlertDialog.Builder alertBuilder;
   private AlertDialog alertDialog;
   private int width = -2;
   private int height = -2;
   private int gravity = 17;
   private int x =0;
   private int y =0;
   private int left = 0;
   private int top = 0;
   private int right = 0;
   private int bottom = 0;
   private Point screen;

    public void setWH(int width,int height) {
        this.width = width;
        this.height = height;
    }
    public void setWH(float width,float height) {

        this.width = (int)(width*this.screen.x);
        this.height = (int)(height*this.screen.y);

    }
    public void setXY(float x,float y) {
        this.x = (int)(x*this.screen.x);
        this.y = (int)(y*this.screen.y);
    }

    public void setXY(int x,int y) {
        this.x = x;
        this.y = y;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setPadding(int left,
            int top ,
            int right ,
            int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
    public void dismiss(){
        this.alertDialog.dismiss();
    }
    public MDialog(Context context) {
        this.context = context;
        this.alertBuilder = new AlertDialog.Builder(this.context, R.style.dialog);
        WindowManager wm = (WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        wm.getDefaultDisplay().getRealSize(outSize);
        this.screen = outSize;
    }

    public void setView(View view) {
        this.view = view;
        this.alertBuilder.setView(this.view);
    }

    @SuppressLint("ResourceType")
    public void setView(@IdRes int id) {
        this.view = LayoutInflater.from(this.context).inflate(id, null);
        this.alertBuilder.setView(this.view);
    }

    public void show() {
        this.alertDialog = this.alertBuilder.create();
        this.alertDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
        this.alertDialog.show();
        setWHXY();
    }

    private void setWHXY() {
        WindowManager.LayoutParams layoutParams = this.alertDialog.getWindow().getAttributes();


        layoutParams.gravity = this.gravity;
        layoutParams.width = this.width;
        layoutParams.height = this.height;
        layoutParams.x = this.x;
        layoutParams.y = this.y;
        this.alertDialog.getWindow().getDecorView().setPadding(this.left, this.top, this.right, this.bottom);
        this.alertDialog.getWindow().setAttributes(layoutParams);
    }

    public View getView() {
        return view;
    }
}
