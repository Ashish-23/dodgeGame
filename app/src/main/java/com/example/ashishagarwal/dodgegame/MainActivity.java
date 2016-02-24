package com.example.ashishagarwal.dodgegame;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    ViewGroup _root;
    Button pause;
    int sec = 0, min = 0;
    BouncingBallView bbv;

    Timer timer;
    int x, y, t,pauseTime, f = 0, height, width;
    String time1 = " ";
    Random random;
    ImageView life;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _root = (ViewGroup) findViewById(R.id.root1);

        life = (ImageView) findViewById(R.id.ivLife);
        pause = (Button) findViewById(R.id.bPause);
        pause.setOnClickListener(this);



        random = new Random();

        t = random.nextInt(2);


        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                f++;
                if(bbv.getGameover() || bbv.getImageShow())
                {
                    Log.v(TAG,"inside ");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            life.setVisibility(View.GONE);
                        }
                    });
                    f = 0;
                    t = random.nextInt(3);

                }
                if (f == t) {
                    startLife();

                }
                if (f == t + pauseTime) {
                    stopLife();
                }
                time1 = "Time = ";
                sec++;
                if (sec > 60) {
                    min++;
                    sec = 0;
                }
                if (min < 10) {
                    time1 = time1 + " 0" + min + ":";
                } else {
                    time1 = time1 + min + ":";
                }
                if (sec < 10) {
                    time1 = time1 + "0" + sec;
                } else {
                    time1 = time1 + sec;
                }


            }
        }, 1000, 1000);

        bbv = new BouncingBallView(this);
        _root.addView(bbv);
    }

    private void stopLife() {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                life.setVisibility(View.GONE);
            }
        });
        f = 0;
        t = random.nextInt(20);
    }

    private void startLife() {


        x = random.nextInt(width - 200);
        y = random.nextInt(height - 200);

        bbv.setLifexy(x,y);
        life.setX(x);
        life.setY(y);
        SharedPreferences sp = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        pauseTime = sp.getInt("pause",5);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                life.setVisibility(View.VISIBLE);
            }
        });

    }


    public void onDestroy() {

        super.onDestroy();

    }

    public void onPause() {

        super.onPause();

    }

    public void onResume() {
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        super.onResume();
        //  mGLView.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,UserSettingActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bPause:
                String state = bbv.pause();
                pause.setText(state);
                break;
        }
    }


}
