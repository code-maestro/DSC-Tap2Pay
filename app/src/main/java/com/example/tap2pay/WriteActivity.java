package com.example.tap2pay;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class WriteActivity extends AppCompatActivity {

    private NfcAdapter nfc;
    private TextView writeTxt;
    private EditText readTxt;
    private ToggleButton tglBtn;

    //ACTVITY LIFE CYCLE FUNCTIONS/METHODS TO HANDLE TRACK THE APP USAGE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        tglBtn = (ToggleButton) findViewById(R.id.readWrite);

        readTxt = findViewById(R.id.read_txt);

        writeTxt = findViewById(R.id.write_txt);
        writeTxt.setText("🚀🚀🚀");

        nfc = NfcAdapter.getDefaultAdapter(this);

    }

    @Override
    protected void onResume() {

        enableForegroundDispatchSystem();

        super.onResume();
    }

    @Override
    protected void onPause() {

        disableForegroundDispatchSystem();

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "NFC INTENT", Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            NdefMessage ndefMessage = createNdefMessage(readTxt.getText() + "420");

            writeNdefMessage(tag, ndefMessage);

        }

        if (tglBtn.isChecked()) {
            Parcelable[] parcelables =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (parcelables != null && parcelables.length > 0) {
                readTextFromTag((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(this, "NO NDEF MESSAGE", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, WriteActivity.class);

        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfc.enableForegroundDispatch(this, pendingIntent, intentFilters,
                null);

    }

    private void disableForegroundDispatchSystem() {
        nfc.disableForegroundDispatch(this);
    }

    // FUNCTION TO DELETE AN CONTENT ON THE NFC TAG BEFORE WRITING THE NEW DATA
    private void formatTag(Tag tag, NdefMessage ndefMessage) {

        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "TAG AIN'T NDEF FORMATABLE", Toast.LENGTH_SHORT)
                        .show();

                return;

            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "TAG WRITTEN 😁😁", Toast.LENGTH_SHORT).show();

        } catch (Exception o) {
            Log.e("Format tag", "formatTag: " + o.getMessage());
        }

    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {

        try {

            if (tag == null) {

                Toast.makeText(this, "TAG OBJECT CANNOT BE NULL", Toast.LENGTH_SHORT)
                        .show();

                return;

            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                formatTag(tag, ndefMessage);
            } else {

                ndef.connect();

                if (!ndef.isWritable()) {

                    Toast.makeText(this, "TAG AIN'T WRITABLE 💤💤",
                            Toast.LENGTH_SHORT).show();

                    ndef.close();

                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "TAG WRITTEN 😁😁", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception o) {
            Log.e("writeNdefMessage: TAG NOT WRTITABLE", o.getMessage(), o);
        }
    }

    private NdefRecord createTextRecord(String message) {

        try {

            byte[] langauge;
            langauge = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = message.getBytes("UTF-8");
            final int langaugeSize = langauge.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langaugeSize
                    + textLength);

            payload.write((byte) (langaugeSize & 0x1F));
            payload.write(langauge, 0, langaugeSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());

        } catch (UnsupportedEncodingException o) {
            Log.e("creating Text record", "createTextRecord: " + o.getMessage(), o);
        }

        return null;

    }

    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }

    //READING FUNCTIONS/METHODS
    private void readWrite(View view) {
        readTxt.setText(" 👀 👀 ");
    }

    private String getTextFromNdefRecord(NdefRecord ndefRecord) {

        String tagContent = null;

        try {

            byte[] payload = ndefRecord.getPayload();

            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            int langaugeSize = payload[0] & 0063;

            tagContent = new String(payload, langaugeSize + 1,
                    payload.length - langaugeSize - 1, textEncoding);


        } catch (UnsupportedEncodingException o) {
            Log.e("getTextFromNdefRecord: ", o.getMessage(), o);
        }

        return tagContent;
    }

    private void readTextFromTag(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            readTxt.setText(tagContent);

        } else {
            Toast.makeText(this, "NO NDEF RECORDS FOUND", Toast.LENGTH_SHORT).show();

        }
    }

}