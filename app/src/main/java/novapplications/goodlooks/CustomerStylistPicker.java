package novapplications.goodlooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class CustomerStylistPicker extends AppCompatActivity
{
    private int zipCode, searchDistance;
    public static final int REQUEST_CODE_LOGIN = 1;
    protected FirebaseAuth login;
    protected FirebaseAuth.AuthStateListener loginListner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_stylist_picker);
        handleLogin();

        Intent intent = getIntent();
        zipCode = intent.getIntExtra("zipCode",-1);
        searchDistance = intent.getIntExtra("distance",-1);

    }
    protected void handleLogin() {
        login = FirebaseAuth.getInstance();
        loginListner = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //signed in
                    onSignIn();
                } else {
                    //signed out
                    //takes you to the login page proivded by the firebase api
                    onSignOut();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(), REQUEST_CODE_LOGIN);
                }
            }
        };
    }

    private void onSignOut() {
        removeListeners();
    }

    private void onSignIn() {
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        login.addAuthStateListener(loginListner);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        removeListeners();
    }

    private void removeListeners()
    {

        if(loginListner != null) {
            login.removeAuthStateListener(loginListner);
        }
    }

}
