package com.example.lab1;

import static android.app.ProgressDialog.show;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

TextView text1;
EditText edit1;
Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.textview1);
        edit1 = (EditText) findViewById(R.id.edt1);
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userinfo = edit1.getText().toString();
                Toast.makeText(getApplicationContext(),
                         "hello "+ userinfo,Toast.LENGTH_LONG).show();
            }
        });



    }
}