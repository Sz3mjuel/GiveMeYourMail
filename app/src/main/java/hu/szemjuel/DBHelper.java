package hu.szemjuel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Szemjuel on 2017. 02. 20..
 */

public class DBHelper extends SQLiteOpenHelper {

    //tring mName, String mEmail, Type mGameType, int mPhone, int mTime, int mDay
    private static final String DB_NAME = "players.db";
    private static final String TABLE_NAME = "players_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "EMAIL";
    private static final String COL_4 = "GAMETYPE";
    private static final String COL_5 = "PHONE";
    private static final String COL_6 = "TIME";
    private static final String COL_7 = "DAY";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT, EMAIL TEXT, GAMETYPE TEXT, PHONE INTEGER, TIME INTEGER, DAY INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData (Player p){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, p.getmName());
        contentValues.put(COL_3, p.getmEmail());
        contentValues.put(COL_4, p.getmGameType().toString());
        try {
            contentValues.put(COL_5, Integer.toString(p.getmPhone()));
            contentValues.put(COL_6, Integer.toString(p.getmTime()));
            contentValues.put(COL_7, Integer.toString(p.getmDay()));
        }catch (Exception e){
            e.printStackTrace();
        }
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }
}
