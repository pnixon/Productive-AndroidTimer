package com.twicethenerd.productive;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class RandomTimer extends AppCompatActivity {

    SeekBar minimumSeekBar;
    SeekBar maximumSeekBar;
    TextView timeTextView;
    TextView minTextView;
    TextView maxTextView;
    Button actionButton;
    boolean isRunning = false;
    CountDownTimer countDownTimer;

    public void updateTimerText(int minutes, int seconds, int miliSeconds) {
        timeTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, miliSeconds));
    }

    private int getRandomTimeInRange() {
        int min = minimumSeekBar.getProgress();
        int max = maximumSeekBar.getProgress();
        Integer random = (new Random().nextInt((max - min) + 1) + min);
        int millis = random * 1000 * 60;
        return millis;
    }

    private void resetAfterDelay(int delayTime) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                isRunning = false;
                timeTextView.setText("RA:ND:OM");
                minimumSeekBar.setProgress(5);
                maximumSeekBar.setProgress(30);
                actionButton.setText("Start");
            }
        }, delayTime);
    }

    public void timerButton(View view) {
        if (isRunning == false) {
            actionButton.setText("Cancel");

            countDownTimer = new CountDownTimer(getRandomTimeInRange(), 41) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* set up elements */
        timeTextView = (TextView)findViewById(R.id.randomTimerText);
        minTextView = (TextView)findViewById(R.id.minText);
        maxTextView = (TextView)findViewById(R.id.maxText);
        minimumSeekBar = (SeekBar)findViewById(R.id.minSeekBar);
        maximumSeekBar = (SeekBar)findViewById(R.id.maxSeekBar);
        actionButton = (Button)findViewById(R.id.actionButton);

        minimumSeekBar.setMin(5);
        minimumSeekBar.setMax(30);
        minimumSeekBar.setProgress(5);
        minimumSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maximumSeekBar.setMin(progress);
                minTextView.setText(String.format("Min: %2d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        maximumSeekBar.setMin(5);
        maximumSeekBar.setMax(90);
        maximumSeekBar.setProgress(30);
        maximumSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minimumSeekBar.setMax(progress);
                maxTextView.setText(String.format("Max: %2d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        timeTextView.setText("RA:ND:OM");


    }

}
