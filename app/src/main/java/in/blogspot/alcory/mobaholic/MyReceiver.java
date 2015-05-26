package in.blogspot.alcory.mobaholic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class MyReceiver extends BroadcastReceiver {
    public static boolean wasScreenOn = true;

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent i =new Intent(context,MyService.class);   // start this sticky service to keep process alive
        context.startService(i);

        if(i.getAction()==Intent.ACTION_USER_PRESENT) {

            //Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
            Log.d("DEEPAK", "DEEPAK USER " + intent.getAction());

            SharedPreferences sharedPref = context.getSharedPreferences(MyApp.MY_SHARED_PREF, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Date currentDate = new Date();
            editor.putLong(MyApp.LAST_TIME_USER_ULOCKED_SCREEN, currentDate.getTime());
            editor.apply();
            Log.d("DEEPAK", "DEEPAK LastUserPresentTime " + currentDate.getTime());
            Long abc = sharedPref.getLong("LastUserPresentTime", 0L);
            Date theLastDate = new Date(abc);
            Log.d("DEEPAK", "DEEPAK LastUserPresentTime removing abc " + abc);
        }
        else if(i.getAction()==Intent.ACTION_DATE_CHANGED){
            MyApp.checkAndSetNewDate(context.getApplicationContext());
        }

    }
}
