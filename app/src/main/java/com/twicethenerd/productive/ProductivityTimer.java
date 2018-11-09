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

public class ProductivityTimer extends AppCompatActivity {

    SeekBar chillTimeSeekBar;
    SeekBar workTimeSeekBar;
    TextView timeTextView;
    TextView chillTextView;
    TextView workTextView;
    Button actionButton;
    boolean isRunning = false;
    CountDownTimer countDownTimer;

    public void updateTimerText(int minutes, int seconds, int miliSeconds) {
        timeTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, miliSeconds));
    }

    private void resetAfterDelay(int delayTime) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                isRunning = false;
                timeTextView.setText("WO:RK:ER");
                chillTimeSeekBar.setProgress(5);
                workTimeSeekBar.setProgress(25);
                actionButton.setText("Start");
            }
        }, delayTime);
    }

    private int toMinutes(int seekValue) {
        return seekValue * 1000 * 60;
    }

    public void timerButton(View view) {
        if (isRunning == false) {
            actionButton.setText("Cancel");

            countDownTimer = new CountDownTimer(toMinutes(workTimeSeekBar.getProgress()), 41) {
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
                    countDownTimer = new CountDownTimer(toMinutes(chillTimeSeekBar.getProgress()), 41) {
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
        setContentView(R.layout.activity_productivity_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* set up elements */
        timeTextView = (TextView)findViewById(R.id.randomTimerText);
        chillTextView = (TextView)findViewById(R.id.chillText);
        workTextView = (TextView)findViewById(R.id.workText);
        chillTimeSeekBar = (SeekBar)findViewById(R.id.chillTimeSeekBar);
        workTimeSeekBar = (SeekBar)findViewById(R.id.workTimeSeekBar);
        actionButton = (Button)findViewById(R.id.actionButton);

        chillTimeSeekBar.setMin(1);
        chillTimeSeekBar.setMax(30);
        chillTimeSeekBar.setProgress(5);
        chillTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                workTimeSeekBar.setMin(progress);
                chillTextView.setText(String.format("Min: %2d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        workTimeSeekBar.setMin(1);
        workTimeSeekBar.setMax(90);
        workTimeSeekBar.setProgress(30);
        workTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chillTimeSeekBar.setMax(progress);
                workTextView.setText(String.format("Max: %2d", progress));
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
