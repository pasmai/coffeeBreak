package ch.hackzurich.coffeebreak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.URL;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // set video URL on button
        Button buttonOpenVideo = findViewById(R.id.button_openvideo);
        final Uri url = getIntent().getExtras().getParcelable(Config.video_url_identifier);
        buttonOpenVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open video call URL in external app/browser
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(url);
                startActivity(i);
            }
        });


        // update Timer until coffee break
        final TextView timer = findViewById(R.id.text_timer_until_break);
        Date date = getIntent().getExtras().getParcelable(Config.break_time_identifier);
        if (date == null) finish();

        long millisInFuture = date.getTime() - System.currentTimeMillis();
        new CountDownTimer(millisInFuture, 1000) {

            @Override
            public void onTick(long millisRemaining) {
                long remainSecs = millisRemaining / 1000;
                timer.setText("" + remainSecs / 60 + ":" + remainSecs % 60);
            }

            @Override
            public void onFinish() {
                timer.setText("00:00");
                cancel();
            }
        }.start();

    }
}