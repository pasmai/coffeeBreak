package ch.hackzurich.coffeebreak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // set video URL on button
        Button buttonOpenVideo = findViewById(R.id.button_openvideo);
        String meetingId = getIntent().getStringExtra(Config.video_meeting_id);


        buttonOpenVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open video call URL in external App/Browser
                Intent i = new Intent(ChatActivity.this, VideoChatActivity.class);
                i.putExtra(Config.video_meeting_id, meetingId);
                startActivity(i);
            }
        });


        // update Timer until coffee break
        final TextView timer = findViewById(R.id.text_timer_until_break);
        Date date = new Date();
        long dateLong = getIntent().getLongExtra(Config.break_time_identifier, -1);
        date.setTime(dateLong);

        if (dateLong == -1) finish();

        long millisInFuture = date.getTime() - System.currentTimeMillis();
        new CountDownTimer(millisInFuture, 1000) {

            @Override
            public void onTick(long millisRemaining) {
                long remainSecs = millisRemaining / 1000;
                timer.setText(String.format("%2d:%02d", remainSecs / 60, remainSecs % 60));
            }

            @Override
            public void onFinish() {
                timer.setText("00:00");
                cancel();
            }
        }.start();


        // TODO update number of participants
    }
}