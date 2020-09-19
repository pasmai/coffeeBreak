package ch.hackzurich.coffeebreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ch.hackzurich.coffeebreak.services.MyFirebaseMessagingService;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import static com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID;

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

        urlField = findViewById(R.id.edit_videourl);
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

                String url = urlField.getText().toString();

                i.putExtra(Config.video_url_identifier, url);
                i.putExtra(Config.break_time_identifier, startTime.getTime());

                // send message to server which will then send notifications to all users
                MyFirebaseMessagingService.sendNotificationToServer(url, startTime);

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




        urlField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkRequiredFields();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void checkRequiredFields(){
        if (!urlField.getText().toString().trim().isEmpty()){
            inviteButton.setEnabled(true);
        } else {
            inviteButton.setEnabled(false);
        }
    }

}