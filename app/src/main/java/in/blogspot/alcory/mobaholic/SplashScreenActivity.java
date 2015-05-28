package in.blogspot.alcory.mobaholic;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_SHOW_TIME = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        new BackgroundSplashTask().execute();

    }

    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    private class BackgroundSplashTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            // I have just given a sleep for this thread
            // if you want to load database, make
            // network calls, load images
            // you can do them here and remove the following
            // sleep

            // do not worry about this Thread.sleep
            // this is an async task, it will not disrupt the UI
            try {
                Thread.sleep(SPLASH_SHOW_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Intent i = new Intent(SplashScreenActivity.this,
                    MainActivity.class);
            // any info loaded can during splash_show
            // can be passed to main activity using
            // below
            i.putExtra("loaded_info", " ");
            startActivity(i);
            finish();
        }
    }
}