package com.example.sinevollibreria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.sinevol.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainFullScreen extends Activity {
int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_full_screen);

        id = getIntent().getIntExtra("IdVendedor", 0);
        System.out.println("ID vendedor activity fullscreen===========>" + id);
        TimerTask tarea= new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MainFullScreen.this,MainMenu.class);
                intent.putExtra("IdVendedor",id);
                startActivity(intent);
                finish();
            }
        };


        Timer tiempo = new Timer();
        tiempo.schedule(tarea,3000);
    }

    }

