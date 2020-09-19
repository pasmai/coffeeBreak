package ch.hackzurich.coffeebreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            //Intent i = new Intent(MainActivity.this, ChooserActivity.class);
            //startActivity(i);
            // Choose an arbitrary request code value

            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                            //new AuthUI.IdpConfig.FacebookBuilder().build(),
                            //new AuthUI.IdpConfig.TwitterBuilder().build(),
                            //new AuthUI.IdpConfig.MicrosoftBuilder().build(),
                            // AuthUI.IdpConfig.YahooBuilder().build(),
                            //new AuthUI.IdpConfig.AppleBuilder().build(),
                            //new AuthUI.IdpConfig.PhoneBuilder().build()),
                            new AuthUI.IdpConfig.EmailBuilder().build())).build(),
                    RC_SIGN_IN);
        }else{
            startActivity(new Intent(MainActivity.this, InviteActivity.class));
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(MainActivity.this, InviteActivity.class));
                finish();
            } else {
            }
        }
    }
}