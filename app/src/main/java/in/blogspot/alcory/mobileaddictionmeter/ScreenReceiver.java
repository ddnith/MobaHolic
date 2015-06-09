package in.blogspot.alcory.mobileaddictionmeter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

public class ScreenReceiver extends BroadcastReceiver {

    // THANKS JASON

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d("DEEPAK", "DEEPAK ScreenReceiver " + intent.getAction());
            MyApp.updateSessionEntry(context);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // AND DO WHATEVER YOU NEED TO DO HERE
            Log.d("DEEPAK","DEEPAK ScreenReceiver check "+intent.getAction());
            SharedPreferences sharedPref = context.getSharedPreferences(MyApp.MY_SHARED_PREF, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Date currentDate = new Date();
            editor.putLong(MyApp.LAST_TIME_USER_ULOCKED_SCREEN, currentDate.getTime());
            editor.apply();
        }
    }

}