package com.example.tap2pay;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    private TextView welcome;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        welcome = findViewById(R.id.starter);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter!= null && mNfcAdapter.isEnabled()){
            welcome.setText("NFC 😎😎 IS ENABLED AND AVALIABLE");
        }else{
            welcome.setText("NFC NOT ENABLED 😌😌 NOTHING VIBES JUST ");
        }

    }
}