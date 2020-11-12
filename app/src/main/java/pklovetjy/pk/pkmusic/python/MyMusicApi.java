package pklovetjy.pk.pkmusic.python;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import pklovetjy.pk.pkmusic.GlobalConfig;

public class MyMusicApi{
    public MyMusicApi(){
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(GlobalConfig.context));
        }
    }

    public String searchSting(String key){
        Python py = Python.getInstance();
        PyObject pyObject = py.getModule("MyMusicApi").callAttr("search", "广寒宫");
        String str = pyObject.toString();
        System.out.println("找到数据\n\n\n\n\n\n\n"+str+"数据结束\n\n\n\n\n");
        return str;
    }

}
