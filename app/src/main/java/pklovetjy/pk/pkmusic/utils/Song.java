package pklovetjy.pk.pkmusic.utils;

/**
 *
 * @ClassName: com.example.pk_tjy_player
 * @Description: 歌曲实体类
 * @author pengKua
 * @date 2020-12-4 上午11:49:59
 *
 */
public class Song {

   public String fileName  ="";
   public String title     = "";
   public int    duration  =-1;
   public String singer    ="";
   public String album     ="";
   public String year      ="";
   public String type      ="";
   public String size      ="";
   public String filePath  ="";
   public String id        ="0";
   public String source    ="";
   public String url       ="";

    public String getFileName() {
        return fileName;
    }
    private String str_zhuanyi(String str){
//        if (str.contains("'")){
//            str=str.replaceAll("'","*.*");
//        }
//        if (str.contains("\"")){
//            str=str.replaceAll("\"","*..*");
//        }
        return str;
    }
    public void setFileName(String fileName) {
        fileName=str_zhuanyi(fileName);
        this.fileName = fileName;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title=str_zhuanyi(title);
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        singer=str_zhuanyi(singer);
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        album=str_zhuanyi(album);
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        year=str_zhuanyi(year);
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        type=str_zhuanyi(type);
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        size=str_zhuanyi(size);
        this.size = size;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        filePath =str_zhuanyi(filePath);
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Song() {
        super();
    }

    public Song(String fileName, String title, int duration, String singer,
                String album, String year, String type, String size, String filePath,String id,String source,String url) {
        super();
        this.fileName = fileName;
        this.title = title;
        this.duration = duration;
        this.singer = singer;
        this.album = album;
        this.year = year;
        this.type = type;
        this.size = size;
        this.filePath = filePath;
        this.id = id;
        this.source = source;
        this.url = url;
    }


}