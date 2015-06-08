package in.blogspot.alcory.mobileaddictionmeter;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by DEEPAK on 5/2/2015.
 */
public class MyApp  extends Application{
    public static final String MY_SHARED_PREF = "myPref" ;
    public static final String LAST_TIME_USER_ULOCKED_SCREEN = "LastUserPresentTime";
    public static final String TOTAL_COUNT = "total_count";
    public static final String DATABASE_NAME ="userData.db";
    public static final String CURRENT_DATE = "current_date";
    public static final String MAX_USE_NOTIFICATION_SETTING ="max_use_notification_setting";
    public static final String MAX_USE_NOTIFICATION_TIME ="max_use_notification_time";
    public static final String NEED_TO_NOTIFY_USER_FOR_MAX_LIMIT = "need_to_notify_user_for_max_limit";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DEEPAK", "DEEPAK onCreate ");
       checkAndSetNewDate(this);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);

    }

    public static void checkAndSetNewDate(Context context){
        Log.d("DEEPAK","checkAndSetNewDate DEEPAK");
        Calendar c = Calendar.getInstance();
        int thisDay = c.get(Calendar.DAY_OF_YEAR);
        long todayMillis = c.getTimeInMillis();

        SharedPreferences sharedPref = context.getSharedPreferences(MY_SHARED_PREF, MODE_PRIVATE);
        long last = sharedPref.getLong(CURRENT_DATE, 0);
        c.setTimeInMillis(last);

        int lastDay = c.get(Calendar.DAY_OF_YEAR);
        Log.d("DEEPAK","checkAndSetNewDate DEEPAK entry last "+last);
        Log.d("DEEPAK","checkAndSetNewDate DEEPAK entry thisDay "+thisDay);
        Log.d("DEEPAK","checkAndSetNewDate DEEPAK entry lastDay "+lastDay);
        if(last == 0 || thisDay != lastDay){
            updateInterMediateSessionEntry(context);
            Log.d("DEEPAK","checkAndSetNewDate DEEPAK entry in");
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.putLong(CURRENT_DATE, todayMillis);
            edit.putBoolean(NEED_TO_NOTIFY_USER_FOR_MAX_LIMIT, true);
            edit.commit();
            insertUserHistory(context,thisDay,0,0,todayMillis);
        }
    }

    private static void insertUserHistory(Context context,int day_index,int count,long time_spent,long date){
        DbTools dbTools = new DbTools(context,DATABASE_NAME,null,1);
        dbTools.insertUserHistory(day_index,count,time_spent, date);
    }

    public static void updateCurrentDayUserHistory(Context context,long time_spent_in_session,boolean isInterMediate){
        Log.d("DEEPAK","time_spent_in_session time"+time_spent_in_session);
        Calendar c = Calendar.getInstance();
        int thisDay = c.get(Calendar.DAY_OF_YEAR);
        DbTools dbTools = new DbTools(context,DATABASE_NAME,null,1);

        HashMap<String,String> currentDayUserHistory = dbTools.getCurrentDayUserHistory(thisDay);
        Log.d("DEEPAK","time_spent_old  time without parse "+currentDayUserHistory.get("time_spent"));

        String time_spent = currentDayUserHistory.get("time_spent");
        if(time_spent == null)return;
        long time_spent_old = Long.parseLong(time_spent);
        Log.d("DEEPAK","time_spent_old  time "+time_spent_old);
        long time = time_spent_old+time_spent_in_session;
        Log.d("DEEPAK","total  time "+time);

        int count = Integer.parseInt(currentDayUserHistory.get("count"));
        count = isInterMediate?count:count+1;

        dbTools.updateUserHistory(thisDay,count,time);

    }
    public static void updateSessionEntry(Context context){
        updateSessionEntry(context,false);
    }

    public static void updateSessionEntry(Context context,boolean isInterMediate){

        SharedPreferences sharedPref = context.getSharedPreferences(MY_SHARED_PREF, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(!sharedPref.contains(MyApp.LAST_TIME_USER_ULOCKED_SCREEN)){
            Log.d("DEEPAK","DEEPAK IGNORE this request ");
            return;
        }
        long millis = sharedPref.getLong(MyApp.LAST_TIME_USER_ULOCKED_SCREEN, 0L);
        Log.d("DEEPAK", "DEEPAK millis   "+millis);
        Date theLastDate = new Date(millis);
        Date currentDate = new Date();

//        DbTools dbTools = new DbTools(context,DATABASE_NAME,null,1);
//        dbTools.insertUserRecord(theLastDate.toString(),currentDate.toString());

        SimpleDateFormat format = new SimpleDateFormat();
        long diff = currentDate.getTime() - theLastDate.getTime();
        updateCurrentDayUserHistory(context,diff,isInterMediate);

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        int totalCount = sharedPref.getInt(MyApp.TOTAL_COUNT,0);
        editor.putInt(MyApp.TOTAL_COUNT,totalCount+1);

        editor.remove(MyApp.LAST_TIME_USER_ULOCKED_SCREEN);
        editor.apply();
    }
    public static long getTotalTimeSpentToday(Context context){
        Calendar c = Calendar.getInstance();
        int thisDay = c.get(Calendar.DAY_OF_YEAR);
        return getTotalTimeSpentToday(context,thisDay);
    }

    public static long getTotalTimeSpentToday(Context context,int thisDay){
        updateInterMediateSessionEntry(context);
        DbTools dbTools = new DbTools(context,DATABASE_NAME,null,1);
        String totalTimeSpent = "0";
        HashMap<String,String> currentDayUserHistory = dbTools.getCurrentDayUserHistory(thisDay);
        totalTimeSpent = currentDayUserHistory.get("time_spent");
        if(null == totalTimeSpent)return 0;
        Log.d("DEEPAK"," currentDayUserHistory "+currentDayUserHistory);
        return Long.parseLong(totalTimeSpent);
    }

    public static void updateInterMediateSessionEntry(Context context){

        PowerManager pm = (PowerManager)
                context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();  //use isInteractive instead of isScreenOn for api level 20 and above
        if(isScreenOn){
            updateSessionEntry(context,true);
            SharedPreferences sharedPref = context.getSharedPreferences(MyApp.MY_SHARED_PREF, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Date currentDate=new Date();
            editor.putLong(MyApp.LAST_TIME_USER_ULOCKED_SCREEN,currentDate.getTime());
            editor.apply();
        }
    }
}
