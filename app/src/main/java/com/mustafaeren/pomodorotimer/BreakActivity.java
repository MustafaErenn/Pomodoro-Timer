package com.mustafaeren.pomodorotimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class BreakActivity extends AppCompatActivity {

    private TextView timerText;
    private ImageView startImage;
    private ImageView pauseImage;
    private ImageView restartImage;
    private ImageView saveImage;



    private final long breakTime = 300000;//5mins
    private CountDownTimer countDownTimer;
    private long timeLeftMiliseconds = breakTime;
    private boolean timerRunning;


    private EditText shortDesc;
    private Button saveToSqlite;

    SQLiteDatabase database;
    SimpleDateFormat sdf;
    String currentDateandTime;

    private Button backToMainPageButton;
    private Button viewYourFinishTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.green31));
        }
        timerText = findViewById(R.id.minuteBreak);
        startImage = findViewById(R.id.startButtonBreak);
        pauseImage = findViewById(R.id.pauseImageBreak);
        saveImage = findViewById(R.id.saveButtonBreak);
        shortDesc = findViewById(R.id.shortDesc);
        saveToSqlite = findViewById(R.id.saveToSqlite);
        backToMainPageButton = findViewById(R.id.backToMain);
        viewYourFinishTimes = findViewById(R.id.viewYourFinishTime);


        if(timerRunning){

            startImage.setVisibility(View.VISIBLE);
            pauseImage.setVisibility(View.INVISIBLE);
        }
        else{
            startTimer();
            startImage.setVisibility(View.INVISIBLE);
            pauseImage.setVisibility(View.VISIBLE);
        }

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

    }

    public void startFunc(){
        if(timerRunning){

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

                pauseImage.setVisibility(View.INVISIBLE);
                startImage.setVisibility(View.INVISIBLE);
                saveImage.setVisibility(View.VISIBLE);
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                currentDateandTime = sdf.format(new Date());

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
    public void savePomodoro(View view){

        saveImage.setVisibility(View.INVISIBLE);
        shortDesc.setVisibility(View.VISIBLE);
        saveToSqlite.setVisibility(View.VISIBLE);
    }


    public void saveToSql(View view){



        if(!shortDesc.getText().toString().matches("")){


            try {

                database = this.openOrCreateDatabase("Times",MODE_PRIVATE,null);
                database.execSQL("CREATE TABLE IF NOT EXISTS times(id INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR, time DATETIME)");
                String sqlString = "INSERT INTO times(description,time) VALUES (?,?)";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                sqLiteStatement.bindString(1,shortDesc.getText().toString());
                sqLiteStatement.bindString(2,currentDateandTime);
                sqLiteStatement.execute();
            }catch (Exception e){

            }

            shortDesc.setVisibility(View.INVISIBLE);
            saveToSqlite.setVisibility(View.INVISIBLE);

            backToMainPageButton.setVisibility(View.VISIBLE);
            viewYourFinishTimes.setVisibility(View.VISIBLE);

        }
        else{
            Toast.makeText(this,"Please Enter Your Short Description",Toast.LENGTH_SHORT).show();
        }
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BreakActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void backToMainPage(View view){
        Intent intentToMain = new Intent(BreakActivity.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }
    public void viewOldFinishTimes(View view){
        Intent intentToDones = new Intent(BreakActivity.this,FinishTimeActivity.class);
        startActivity(intentToDones);
        finish();


    }
}