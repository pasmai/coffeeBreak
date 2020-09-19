package ch.hackzurich.coffeebreak;

import androidx.appcompat.app.AppCompatActivity;
import ch.hackzurich.coffeebreak.services.MyFirebaseMessagingService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Random;

public class InviteActivity extends AppCompatActivity {
    TextView urlField;
    TimePicker startTimeField;
    Button inviteButton;
    Button settingsButton;

    private static final String TAG = "InviteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        
        startTimeField = findViewById(R.id.timepicker_breaktime);
        inviteButton = findViewById(R.id.button_sendinvite);
        settingsButton = findViewById(R.id.button_settings);

        startTimeField.setIs24HourView(true);


        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InviteActivity.this, ChatActivity.class);

                Date startTime = Helper.getDateFromTimePicker(startTimeField);

                boolean inFuture = startTime.compareTo(new Date(System.currentTimeMillis())) > 0;
                if (!inFuture){
                    Toast.makeText(InviteActivity.this, "Time has to be in the future.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 10;
                Random random = new Random();
                StringBuilder buffer = new StringBuilder(targetStringLength);
                for (int j = 0; j < targetStringLength; j++) {
                    int randomLimitedInt = leftLimit + (int)
                            (random.nextFloat() * (rightLimit - leftLimit + 1));
                    buffer.append((char) randomLimitedInt);
                }

                String url = buffer.toString();

                i.putExtra(Config.video_meeting_id, url);
                i.putExtra(Config.break_time_identifier, startTime.getTime());

                // send message to server which will then send notifications to all users
                MyFirebaseMessagingService.sendNotificationToServer(url, startTime);

                // Write a message to the database
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                database.child("invites").child(url).child(Long.toString(startTime.getTime())).setValue("dummy");

                startActivity(i);
            }
        });


        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InviteActivity.this, SettingsActivity.class);

                startActivity(i);
            }
        });
    }
}