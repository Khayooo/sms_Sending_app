package com.example.massagesendingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {


    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    private EditText etSenderNumber;
    private EditText etReceiverNumber;
    private EditText etMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSenderNumber = findViewById(R.id.etSenderNumber);
        etReceiverNumber = findViewById(R.id.etReceiverNumber);
        etMessage = findViewById(R.id.etMessage);
        Button btnSendMessage = findViewById(R.id.btnSendMessage);

        btnSendMessage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
            } else {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String senderNumber = etSenderNumber.getText().toString().trim();
        String receiverNumber = etReceiverNumber.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (TextUtils.isEmpty(senderNumber) || TextUtils.isEmpty(receiverNumber) || TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(receiverNumber, senderNumber, message, null, null);
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage();
            } else {
                Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
}