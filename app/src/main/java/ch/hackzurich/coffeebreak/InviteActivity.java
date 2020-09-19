package ch.hackzurich.coffeebreak;

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

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import static com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID;

public class InviteActivity extends AppCompatActivity {
    TextView urlField;
    TimePicker startTimeField;
    Button inviteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);


        urlField = findViewById(R.id.edit_videourl);
        startTimeField = findViewById(R.id.timepicker_breaktime);
        inviteButton = findViewById(R.id.button_sendinvite);
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

                // TODO send the invite link to the other participants via notification
                sendNotificationToServer(url, startTime);


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

    private void sendNotificationToServer(String url, Date date){
        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        fm.send(new RemoteMessage.Builder(SENDER_ID + email)
                .setMessageId(Integer.toString(MyFirebaseMessagingService.MSG_ID.incrementAndGet()))
                .addData("url", url)
                .addData("date", date.toString())
                .addData("target_topic", "all")
                .build());
    }
}