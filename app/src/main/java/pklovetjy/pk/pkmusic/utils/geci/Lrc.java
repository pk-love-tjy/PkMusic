package pklovetjy.pk.pkmusic.utils.geci;

import pklovetjy.pk.pkmusic.utils.FileUtils;
import pklovetjy.pk.pkmusic.utils.Song;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Lrc {
    /**
     * 从assets目录下读取歌词文件内容
     * @param fileName
     * @return
     */
    public String getFromAssets(Song song){
        try {
            File file=new File(FileUtils.getLrcDir()+(song.getTitle()+"_"+song.getSinger()+".lrc"));
            FileInputStream inputStream =new FileInputStream(file);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String result="";
            while((line = bufReader.readLine()) != null){
                if(line.trim().equals(""))
                    continue;
                result += line + "\r\n";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
