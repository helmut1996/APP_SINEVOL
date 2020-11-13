package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
EditText editPint;
Button btn_entar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPint =(EditText)findViewById(R.id.edit_Pin);
        btn_entar =(Button)findViewById(R.id.btn_entrar);

        btn_entar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPint.getText().toString().isEmpty()==equals("")){

                    Toast.makeText(getApplicationContext(),"pin correcto",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),MainMenu.class));
                }else{

                    Toast.makeText(getApplicationContext(),"debe ingresar su pin ",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}