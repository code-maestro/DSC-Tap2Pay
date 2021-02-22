package com.example.tap2pay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        TextView mView = (TextView) this.findViewById(R.id.check);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter!=null && nfcAdapter.isEnabled()){

            mView.setText("NFC IS AVAILABLE AND ENABLED 🤣🤣🤣");

            Toast.makeText(this, "NFC IS AVAILABLE AND ENABLED", Toast.LENGTH_SHORT)
                    .show();
        }else{
            mView.setText("NO NFC, 🤣🤣🤣 NOTHING KUMANYOKO");

            finish();

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {

        Toast.makeText(this, "NFC INTENT RECEIVED", Toast.LENGTH_SHORT).show();

        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {

        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters,
                null);

        super.onResume();
    }

    @Override
    protected void onPause() {

        nfcAdapter.disableForegroundDispatch(this);

        super.onPause();
    }
}