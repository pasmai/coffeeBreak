package ch.hackzurich.coffeebreak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InviteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);


        final TextView urlField = findViewById(R.id.edit_videourl);
        TextView startTimeField = findViewById(R.id.edit_breaktime);
        Button inviteButton = findViewById(R.id.button_sendinvite);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
        Date date = null;
        try {
            date = formatter.parse(startTimeField.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse date", Toast.LENGTH_SHORT).show();
        }


        final Date startTime = date;
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InviteActivity.this, ChatActivity.class);
                i.putExtra(Config.video_url_identifier, Uri.parse(urlField.getText().toString()));
                i.putExtra(Config.break_time_identifier, startTime);

                // TODO send the invite link to the other participants via notification
                startActivity(i);
            }
        });

    }
}