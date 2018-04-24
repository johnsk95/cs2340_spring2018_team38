package com.example.team38.mail;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class SendMailIntentService extends IntentService {

    public static final String PARAM_RECIPIENT = "param_recipient";
    public static final String PARAM_SUBJECT = "param_subject";
    public static final String PARAM_MESSAGE = "param_message";
    public SendMailIntentService() {
        super("SendMainIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String recipient = intent.getStringExtra(SendMailIntentService.PARAM_RECIPIENT);
        String subject = intent.getStringExtra(SendMailIntentService.PARAM_SUBJECT);
        String msg = intent.getStringExtra(SendMailIntentService.PARAM_MESSAGE);

        if (recipient == null || subject == null || msg == null) {
            Log.e("mail", "could not retrieve info parcels for mail");
        }
        try {
            Log.d("MAIL", "trying to send mail");
            GMailSender sender = new GMailSender();
            sender.sendMail(subject,
                    msg,
                    "janc.team38@gmail.com",
                    recipient);
            Toast t = Toast.makeText(getApplicationContext(),
                    "Sent email!",
                    Toast.LENGTH_SHORT);
            t.show();
        } catch (Exception e) {
            Log.e("MAIL EXCEPTION", e.getMessage(), e);
        }
    }
}
