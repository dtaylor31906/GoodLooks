package novapplications.goodlooks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import novapplications.goodlooks.models.Address;
import novapplications.goodlooks.models.Service;
import novapplications.goodlooks.models.User;

//page for stylist to configure details such as hours to work and services they provide.
public class SettingsStylist extends AppCompatActivity {
    public static final int REQUEST_CODE_LOGIN = 1;
    protected FirebaseAuth login;
    protected FirebaseAuth.AuthStateListener loginListner;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_stylist);
        handleLogin();
        currentUser = (User) getIntent().getSerializableExtra("user");
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
    //adds a service to the user list and to the list of providers of service
    private void addService(Service service)
    {
        DatabaseReference DB = FirebaseDatabase.getInstance().getReference();
        DatabaseReference serviceCategories = DB.child("serviceCategories");
        DatabaseReference ableStylist= serviceCategories.child(service.getCategory()).child("ableStylist").child(login.getCurrentUser().getUid());
        ableStylist.child("firstName").setValue(currentUser.getFirstName());
        ableStylist.child("lastName").setValue(currentUser.getLastName());
    }
    private void addAddressOfAppointments(Address address)
    {
        //use geoLocation to get lat and long
    }
}
