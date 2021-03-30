package com.mustafaeren.pomodorotimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class FinishTimeActivity extends AppCompatActivity {
    ArrayList<FinishModel> finishModelArrayList;
    DoneAdapter doneAdapterX;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_time);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.blue31));
        }

        finishModelArrayList = new ArrayList<FinishModel>();
        readDataFromSqlite();
        RecyclerView recyclerView = findViewById(R.id.finishTimeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        doneAdapterX = new DoneAdapter(finishModelArrayList);
        recyclerView.setAdapter(doneAdapterX);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FinishTimeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void readDataFromSqlite(){
        database = this.openOrCreateDatabase("Times",MODE_PRIVATE,null);
        Cursor res = database.rawQuery( "select * from "+"times", null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            String id = (res.getString(res.getColumnIndex("id")));

            String description = (res.getString(res.getColumnIndex("description")));

            String dateTime = (res.getString(res.getColumnIndex("time")));


            finishModelArrayList.add(new FinishModel(id,description,dateTime));
            //doneAdapterX.notifyDataSetChanged();

            res.moveToNext();
        }
    }
}