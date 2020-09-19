package ch.hackzurich.coffeebreak;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

    public class VideoChatActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_video_chat);

            // Initialize default options for Jitsi Meet conferences.
            String meetingId = getIntent().getStringExtra(Config.video_url_identifier);
            URL serverURL;
            try {
                serverURL = new URL("https://meet.jit.si/" + meetingId);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Could not open meeting URL", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            JitsiMeetConferenceOptions defaultOptions
                    = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .setWelcomePageEnabled(false)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        }

        public void onButtonClick(View v) {
            EditText editText = findViewById(R.id.conferenceName);
            String text = editText.getText().toString();

            if (text.length() > 0) {
                // Build options object for joining the conference. The SDK will merge the default
                // one we set earlier and this one when joining.
                JitsiMeetConferenceOptions options
                        = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(text)
                        .build();
                // Launch the new activity with the given options. The launch() method takes care
                // of creating the required Intent and passing the options.
                JitsiMeetActivity.launch(this, options);
            }
        }
    }

