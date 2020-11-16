package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainFacruraList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_facrura_list);

        getSupportActionBar().setTitle("Facturacion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}