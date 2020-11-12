package pklovetjy.pk.pkmusic.utils.sqlit;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pklovetjy.pk.pkmusic.GlobalConfig;
import pklovetjy.pk.pkmusic.utils.Song;

import java.util.ArrayList;

/**
 * Created by lake
 * 此类的功能：第三页的缓存数据的库
 */
public class SQLitePlayList extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "PkMusicSqlite.db";
    private final static int DATABASE_VERSION = 1;
    private final  static String TABLE_NAME = "playlist";

    //创建数据库，里面添加了3个参数，分别是：Msgone VARCHAR类型，30长度当然这了可以自定义
    //Msgtwo VARCHAR(20)   Msgthree VARCHAR(30))  NOT NULL不能为空
    String sql = "CREATE TABLE " + TABLE_NAME
            + "(_id INTEGER PRIMARY KEY,"
            + " fileName TEXT(30),"
            + " title    TEXT(30) NOT NULL,"
            + " duration  INT(30),"
            + " singer   TEXT(30) NOT NULL,"
            + " album    TEXT(30),"
            + " year     TEXT(30),"
            + " type     TEXT(30),"
            + " size     TEXT(30),"
            + " filePath TEXT(30),"
            + " id       TEXT(30),"
            + " source   TEXT(30),"
            + " url      TEXT(30))"; ;
    //构造函数，创建数据库
    public SQLitePlayList(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    //获取游标
    public Cursor getCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }


    public Song getSong(int position){
        System.out.println("=====-=====================--------------------------===================");
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        Song song = new Song();
        // 文件名;;
        song.fileName    =   cursor.getString(cursor.getColumnIndex("fileName")) ;
        song.title       =   cursor.getString(cursor.getColumnIndex("title")) ;
        song.duration    =   cursor.getInt(cursor.getColumnIndex("duration"))    ;
        song.singer      =   cursor.getString(cursor.getColumnIndex("singer")) ;
        song.album       =   cursor.getString(cursor.getColumnIndex("album")) ;
        song.year        =   cursor.getString(cursor.getColumnIndex("year")) ;
        song.type        =   cursor.getString(cursor.getColumnIndex("type")) ;
        song.size        =   cursor.getString(cursor.getColumnIndex("size")) ;
        song.filePath    =   cursor.getString(cursor.getColumnIndex("filePath")) ;
        song.id          =   cursor.getString(cursor.getColumnIndex("id")) ;
        song.source      =   cursor.getString(cursor.getColumnIndex("source")) ;
        song.url         =   cursor.getString(cursor.getColumnIndex("url")) ;
        cursor.close();
        System.out.println("=====-=====================--------------------------===================");
        return song;
    }

    //插入一条记录
    public long insert(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fileName", song.fileName);
        cv.put("title",    song.title   );
        cv.put("duration", song.duration);
        cv.put("singer",   song.singer  );
        cv.put("album",    song.album   );
        cv.put("year",     song.year    );
        cv.put("type",     song.type    );
        cv.put("size",     song.size    );
        cv.put("filePath", song.filePath);
        cv.put("id",       song.id      );
        cv.put("source",   song.source  );
        cv.put("url",      song.url     );
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }
    public int queryPo(Song song){
        Cursor c = getCursor();
        while (c.moveToNext()){
            Song dataSong= GlobalConfig.sqLitePlayList.getSong(c.getPosition());
            if (dataSong.title.equals(song.title) && dataSong.singer.equals(song.singer)&&dataSong.source.equals(song.source)){
                return c.getPosition();
            }
        }
        return -1;
    }
    //根据条件查询
    public Cursor query(Song song) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE BookName LIKE ?", args);
        String[] sele = new String[]{
               song.title   ,
               song.singer  ,
               song.filePath,
               song.id      ,
               song.source  ,
               song.url
        };
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE title =? AND singer =? AND filePath=? AND id=? AND source=?AND url=?", sele);

        return cursor;
    }
    //删除记录
    public int  delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where ="_id != ?";
        String[] whereValue = { Integer.toString(id) };
        return db.delete(TABLE_NAME, where, whereValue);
    }
    public int deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }
    //删除记录
    public int delete(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] sele = new String[]{
                song.title   ,
                song.singer  ,
                song.filePath,
                song.id      ,
                song.source  ,
                song.url
        };
        return db.delete(TABLE_NAME, "title =? AND singer =? AND filePath=? AND id=? AND source=?AND url=?", sele);
    }
    public ArrayList<Song> getSongs(){
        ArrayList<Song> arrayList = new ArrayList<>();
        Cursor c = getCursor();
        while (c.moveToNext()){
            arrayList.add(getSong(c.getPosition()));
        }
        c.close();
        return arrayList;
    }

    //更新记录
    public void update(Song oldSong,Song newSong ) {
        String[] sele = new String[]{
                oldSong.title   ,
                oldSong.singer  ,
                oldSong.filePath,
                oldSong.id      ,
                oldSong.source  ,
                oldSong.url
        };
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fileName", newSong.fileName);
        cv.put("title", newSong.title   );
        cv.put("duration", newSong.duration);
        cv.put("singer", newSong.singer  );
        cv.put("album", newSong.album   );
        cv.put("year", newSong.year    );
        cv.put("type", newSong.type    );
        cv.put("size", newSong.size    );
        cv.put("filePath", newSong.filePath);
        cv.put("id", newSong.id      );
        cv.put("source", newSong.source  );
        cv.put("url", newSong.url     );
        db.update(TABLE_NAME,
                cv,
                "title =? AND singer =? AND filePath=? AND id=? AND source=?AND url=?",
                sele);
    }
}
