package com.example.tap2pay;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private TextView welcome;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        welcome = findViewById(R.id.starter);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            welcome.setText("NFC 😎😎 IS ENABLED AND AVALIABLE");
        } else {
            welcome.setText("NFC NOT ENABLED 😌😌 NOTHING VIBES JUST ");
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {

        Toast.makeText(this, "NFC INTENT RECEIVED ", Toast.LENGTH_SHORT).show();

        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {

        Intent intent = new Intent(this, TestActivity.class);

        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters,
                null);

        super.onResume();
    }

    @Override
    protected void onPause() {

        mNfcAdapter.disableForegroundDispatch(this);

        super.onPause();
    }
}