package com.example.tap2pay;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mView = (TextView) this.findViewById(R.id.check);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter!=null || nfcAdapter.isEnabled()){

            mView.setText("NFC IS AVAILABLE AND ENABLED 🤣🤣🤣 NOTHING KUMANYOKO");

            Toast.makeText(this, "NFC IS AVAILABLE AND ENABLED", Toast.LENGTH_SHORT)
                    .show();
        }else{
            mView.setText("NO NFC, 🤣🤣🤣 NOTHING KUMANYOKO");
        }

    }
}