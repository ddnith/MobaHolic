package in.blogspot.alcory.mobaholic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DEEPAK on 5/6/2015.
 */
public class DbTools extends SQLiteOpenHelper {
    public DbTools(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);// name is db name
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        // do not put a ; in the end
//        String query1 = "CREATE TABLE UserRecord ( SessionId INTEGER PRIMARY KEY AUTOINCREMENT, Start_Time TEXT NOT NULL, Stop_Time TEXT NOT NULL)";
//        sqLiteDatabase.execSQL(query1);
        // do not put a ; in the end
        String query2 = "CREATE TABLE userhistory (day_index INTEGER PRIMARY KEY, count INTEGER,time_spent INTEGER,date INTEGER)";
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

//    public void insertUserRecord(String start_time,String stop_time){
//        SQLiteDatabase database = getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put("Start_Time",start_time);
//        values.put("Stop_Time",stop_time);
//
//        database.insert("UserRecord",null,values);
//        database.close();
//    }

    public void insertUserHistory(int day_index,int count,long time_spent,long date){
        checkAndDeleteEntryIfAlreadyExisted(day_index);

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("day_index",day_index);
        values.put("count",count);
        values.put("time_spent",time_spent);
        values.put("date",date);

        database.insert("userhistory",null,values);
        database.close();
    }
    //this method will be needed after 1 year
    private void checkAndDeleteEntryIfAlreadyExisted(int day_index) {
        SQLiteDatabase database = getWritableDatabase();
        String deleteQuery = "DELETE FROM userhistory where day_index='"+day_index+"'";
        database.execSQL(deleteQuery);
    }

    public int updateUserHistory(int day_index, int count, long time_spent){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("count",count);
        values.put("time_spent",time_spent);
        return database.update("userhistory",values,"day_index"+" = ?",new String[]{Integer.toString(day_index)});
    }

//    public ArrayList<HashMap<String,String>> getAllUserRecord(){
//        ArrayList<HashMap<String,String>> userRecordList;
//        userRecordList = new ArrayList<HashMap<String, String>>();
//
//        String selectQuery = "SELECT * FROM UserRecord";
//
//        SQLiteDatabase database = getWritableDatabase();
//
//        Cursor cursor = database.rawQuery(selectQuery,null);
//
//        if(cursor.moveToFirst()){
//            do{
//                HashMap<String,String> userRecordMap = new HashMap<String, String>();
//                userRecordMap.put("start_time",cursor.getString(1));
//                userRecordMap.put("stop_time",cursor.getString(2));
//                userRecordList.add(userRecordMap);
//            }while (cursor.moveToNext());
//
//        }
//        database.close();
//        return userRecordList;
//    }

    public ArrayList<HashMap<String,String>> getAllUserHistory(){
        ArrayList<HashMap<String,String>> userHistoryList;
        userHistoryList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM userhistory ORDER BY date DESC";

        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                HashMap<String,String> userHistoryMap = new HashMap<String, String>();
                userHistoryMap.put("day_index",cursor.getString(0));
                userHistoryMap.put("count",cursor.getString(1));
                userHistoryMap.put("time_spent",cursor.getString(2));
                userHistoryMap.put("date",cursor.getString(3));
                userHistoryList.add(userHistoryMap);
            }while (cursor.moveToNext());

        }
        database.close();
        return userHistoryList;
    }

    public HashMap<String,String> getCurrentDayUserHistory(int day_index){
        String selectQuery = "SELECT * FROM userhistory where day_index ='"+Integer.toString(day_index)+"'";

        SQLiteDatabase database = getReadableDatabase();

        HashMap<String,String> userHistoryMap = new HashMap<String, String>();

        Cursor cursor = database.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                userHistoryMap.put("day_index",cursor.getString(0));
                userHistoryMap.put("count",cursor.getString(1));
                userHistoryMap.put("time_spent",cursor.getString(2));
                userHistoryMap.put("date",cursor.getString(3));
                Log.d("DEEPAK"," cursor day_index"+cursor.getString(0));
                Log.d("DEEPAK"," cursor count"+cursor.getString(1));
                Log.d("DEEPAK"," cursor time_spent"+cursor.getString(2));
                Log.d("DEEPAK"," cursor date"+cursor.getString(3));

            }while (cursor.moveToNext());

        }
        database.close();
        return userHistoryMap;
    }
}
