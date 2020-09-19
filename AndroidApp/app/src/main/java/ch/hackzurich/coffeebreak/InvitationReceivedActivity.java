package ch.hackzurich.coffeebreak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class InvitationReceivedActivity extends AppCompatActivity {

    String TAG = "InvitationReceived";
    String url;
    Date breakTime = new Date();
    boolean accepted = false;
    boolean isTimerExpired = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_received);

        // TODO display break information and timer
        long dateLong = getIntent().getLongExtra(Config.break_time_identifier, -1);
        if (dateLong == - 1) {
            Log.d(TAG, "Received invitation with timestamp null");
            finish();
        }

        breakTime.setTime(dateLong);
        url = getIntent().getStringExtra(Config.video_meeting_id);


        Button button = findViewById(R.id.button_accept_invite);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accepted = true;
                openMeetingUrl();
            }
        });

        // update Timer until coffee break
        final TextView timer = findViewById(R.id.text_timer_until_break_recv);
        long millisInFuture = breakTime.getTime() - System.currentTimeMillis();
        new CountDownTimer(millisInFuture, 1000) {

            @Override
            public void onTick(long millisRemaining) {
                long remainSecs = millisRemaining / 1000;
                timer.setText(String.format("%2d:%02d", remainSecs / 60, remainSecs % 60));
            }

            @Override
            public void onFinish() {
                timer.setText("00:00");
                isTimerExpired = true;
                openMeetingUrl();

                cancel();
            }
        }.start();
    }

    private void openMeetingUrl() {
        if (!isTimerExpired) return;

        // open video call URL in external App/Browser
        Intent i = new Intent(this, VideoChatActivity.class);
        i.putExtra(Config.video_meeting_id, url);
        startActivity(i);
    }
}