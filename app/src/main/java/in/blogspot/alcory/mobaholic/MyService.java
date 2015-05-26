package in.blogspot.alcory.mobaholic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class MyService extends Service {

    public MyService() {
    }

    Handler mTimeCheckerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(shouldShowTimeExceededNotification()){
                sendMaxUsesNotification();
            }
            return true;
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("DEEPAK", " MyService onStartCommand ");
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();  //use isInteractive instead of isScreenOn for api level 20 and above
        if(!isScreenOn)
            MyApp.updateSessionEntry(getApplicationContext());

        if(shouldShowTimeExceededNotification())
            sendMaxUsesNotification();

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return Service.START_STICKY;
    }



    private boolean shouldShowTimeExceededNotification() {
        SharedPreferences sharedPref = getSharedPreferences(MyApp.MY_SHARED_PREF,MODE_PRIVATE);
        if(!sharedPref.getBoolean(MyApp.NEED_TO_NOTIFY_USER_FOR_MAX_LIMIT,false))
            return false;

        if(sharedPref.getBoolean(MyApp.MAX_USE_NOTIFICATION_SETTING,false)){
         long timeSpentToday = MyApp.getTotalTimeSpentToday(this);
            long timeForNotification = (long)(sharedPref.getFloat(MyApp.MAX_USE_NOTIFICATION_TIME,0)*60*60*1000);
            Log.d("DEEPAK","abc timeForNotification"+timeForNotification);
            Log.d("DEEPAK","abc MAX_USE_NOTIFICATION_TIME"+sharedPref.getFloat(MyApp.MAX_USE_NOTIFICATION_TIME,0));
            if(timeSpentToday >= timeForNotification)
                return true;
            else
                mTimeCheckerHandler.sendEmptyMessageDelayed(0,timeForNotification-timeSpentToday);
        }
           return false;
    }

    private void sendMaxUsesNotification() {
        SharedPreferences sharedPref = getSharedPreferences(MyApp.MY_SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(MyApp.NEED_TO_NOTIFY_USER_FOR_MAX_LIMIT, false);
        editor.commit();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("MobaHolic")
                        .setContentText("You have reached your mobile uses limit for the day");
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(),0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        // Sets an ID for the notification
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
