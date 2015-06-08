package in.blogspot.alcory.mobileaddictionmeter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    // THANKS JASON

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d("DEEPAK", "DEEPAK ScreenReceiver " + intent.getAction());
            MyApp.updateSessionEntry(context);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // AND DO WHATEVER YOU NEED TO DO HERE
            Log.d("DEEPAK","DEEPAK ScreenReceiver "+intent.getAction());
        }
    }

}