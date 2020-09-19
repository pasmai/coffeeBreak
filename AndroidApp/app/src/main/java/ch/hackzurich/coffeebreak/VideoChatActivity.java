package ch.hackzurich.coffeebreak;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

public class VideoChatActivity extends AppCompatActivity {

    String meetingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);

        meetingId = getIntent().getStringExtra(Config.video_meeting_id);
    }

    public void onButtonClick(View v) {
        EditText editText = findViewById(R.id.conferenceName);
        String text = editText.getText().toString();

        if (!text.isEmpty()) {

            // Build options object for joining the conference. The SDK will merge the default
            // one we set earlier and this one when joining.
            JitsiMeetUserInfo user = new JitsiMeetUserInfo();
            user.setDisplayName(text);

            JitsiMeetConferenceOptions options
                    = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(meetingId)
                    .setUserInfo(user)
                    .build();
            // Launch the new activity with the given options. The launch() method takes care
            // of creating the required Intent and passing the options.
            JitsiMeetActivity.launch(this, options);
        }
    }
}
