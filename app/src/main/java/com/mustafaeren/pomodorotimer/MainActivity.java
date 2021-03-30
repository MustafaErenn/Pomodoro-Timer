package com.mustafaeren.pomodorotimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.CountedCompleter;

public class MainActivity extends AppCompatActivity {
    private TextView timerText;
    private ImageView startImage;
    private ImageView pauseImage;
    private ImageView restartImage;



    final long pomodoroTime = 1500000;//25mins
    private CountDownTimer countDownTimer;
    private long timeLeftMiliseconds = pomodoroTime;
    private boolean timerRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.red31));
        }

        timerText = findViewById(R.id.minute);
        startImage = findViewById(R.id.startButton);
        pauseImage = findViewById(R.id.pauseImage);
        restartImage = findViewById(R.id.restartImage);

        updateTimer();
        startImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFunc();
            }
        });
        pauseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseFunc();
            }
        });
        restartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartFunc();
            }
        });
    }

    public void startFunc(){
        if(timerRunning){
            // change start image to pause image
            startImage.setVisibility(View.VISIBLE);
            pauseImage.setVisibility(View.INVISIBLE);
        }
        else{
            startTimer();

            startImage.setVisibility(View.INVISIBLE);
            pauseImage.setVisibility(View.VISIBLE);
        }
    }
    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftMiliseconds,1000) {
            @Override
            public void onTick(long l) {
                timeLeftMiliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(MainActivity.this,BreakActivity.class);
                startActivity(intent);

            }
        }.start();
        timerRunning=true;
    }

    public void pauseFunc(){
        countDownTimer.cancel();
        startImage.setVisibility(View.VISIBLE);
        pauseImage.setVisibility(View.INVISIBLE);
        timerRunning=false;
    }

    public void updateTimer(){
        int minute = (int) timeLeftMiliseconds/60000;
        int seconds = (int) timeLeftMiliseconds % 60000 / 1000;

        String timeLeft;

        timeLeft = " " +minute + " : ";
        if(seconds<10){
            timeLeft+="0";
        }
        timeLeft += seconds;
        timerText.setText(timeLeft);


    }

    public void restartFunc(){
        timeLeftMiliseconds = pomodoroTime;
        updateTimer();
    }
}