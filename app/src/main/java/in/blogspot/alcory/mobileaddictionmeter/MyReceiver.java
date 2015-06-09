package in.blogspot.alcory.mobileaddictionmeter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DEEPAK", "DEEPAK USER 1 - " + intent.getAction());

        Intent i = new Intent(context,MyService.class);   // start this sticky service to keep process alive
        context.startService(i);

        if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {

            //Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
            Log.d("DEEPAK", "DEEPAK USER 2 - " + intent.getAction());

            SharedPreferences sharedPref = context.getSharedPreferences(MyApp.MY_SHARED_PREF, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Date currentDate = new Date();
            editor.putLong(MyApp.LAST_TIME_USER_ULOCKED_SCREEN, currentDate.getTime());
            editor.apply();
        }
        else if(i.getAction()==Intent.ACTION_DATE_CHANGED){
            MyApp.checkAndSetNewDate(context.getApplicationContext());
        }

    }
}