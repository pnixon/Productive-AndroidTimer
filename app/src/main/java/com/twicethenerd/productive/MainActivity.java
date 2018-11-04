package com.twicethenerd.productive;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public SeekBar timerSeekBar;
    public TextView timerTextView;
    public Button actionButton;
    public CountDownTimer countDownTimer;
    private boolean isRunning = false;

    public void updateTimerText(int minutes, int seconds, int miliSeconds) {
        timerTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, miliSeconds));
    }

    public void timerButton(View view) {
        if (isRunning == false) {
            actionButton.setText("Cancel");

            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 * 60, 41) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long totalSeconds = (millisUntilFinished / 1000);
                    int minutesLeft = (int) totalSeconds / 60;
                    int secondsLeft = (int) totalSeconds % 60;
                    int miliSeconds = (int) millisUntilFinished % 100;

                    updateTimerText(minutesLeft, secondsLeft, miliSeconds);
                    isRunning = true;
                }

                @Override
                public void onFinish() {
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(3000, 255));
                    }
                    updateTimerText(0, 0, 0);
                    resetAfterDelay(1000);
                }
            }.start();
        } else {
            countDownTimer.cancel();
            resetAfterDelay(0);
        }
    }

    private void resetAfterDelay(int delayTime) {
        isRunning = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                updateTimerText(25, 0, 0);
                timerSeekBar.setProgress(25);
                actionButton.setText("Start");
            }
        }, delayTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        timerSeekBar = (SeekBar)findViewById(R.id.seekBar2);
        timerSeekBar.setMin(10);
        timerSeekBar.setMax(90);
        timerSeekBar.setProgress(25);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isRunning == false) {
                    int minutes = (int) progress;
                    int seconds = 0;
                    int miliSeconds = 0;

                    updateTimerText(minutes, seconds, miliSeconds);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timerTextView = (TextView)findViewById(R.id.TimeText);

        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
            }
        };

        actionButton = (Button)findViewById(R.id.timer_button);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_single_timer) {

        } else if (id == R.id.nav_multiple_timers) {

        } else if (id == R.id.nav_random_timer) {
            Intent randomTimerIntent = new Intent(timerTextView.getContext(), RandomTimer.class);
            startActivityForResult(randomTimerIntent, 0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
