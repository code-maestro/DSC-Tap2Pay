package com.example.tap2pay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button mButton, writeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);
        writeBtn = findViewById(R.id.write_btn);

        mButton.setOnClickListener(v -> {
            startActivity(new Intent(this, TestActivity.class));
            //finish();
        });

        writeBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, WriteActivity.class));
        });

    }
}