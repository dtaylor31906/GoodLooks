package novapplications.goodlooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Iterator;

import static novapplications.goodlooks.R.attr.windowActionBar;

public class StylistHome extends AppCompatActivity
{

    public static final int REQUEST_CODE_LOGIN = 1;
    private static final String TAG = "StylistHome";
    protected FirebaseAuth login;
    protected FirebaseAuth.AuthStateListener loginListner;
    private Button customerButton;
    private String[] roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stylist_home);
        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            getRolesFromDataBase();
        }
        else
        {
            roles = extras.getStringArray("roles");
        }
        handleLogin();
        customizeActionBar();

    }

    private void getRolesFromDataBase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUserRoles = ref.child("users/" + uid+"/roles");
        currentUserRoles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                Iterator<DataSnapshot> itterator = data.iterator();
                roles = new String[(int) dataSnapshot.getChildrenCount()];
                int i = 0;
                while (itterator.hasNext())
                {
                    roles[i++] = itterator.next().getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.getMessage());
            }
        });
    }

    protected void customizeActionBar()
    {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar myBar = getSupportActionBar();

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
        Toast.makeText(this, "signedout", Toast.LENGTH_SHORT).show();
    }

    //set information needed after siging in
    //figure out the right page to display based on the user's role
    private void onSignIn()
    {
        //initiate page
        customerButton = (Button)findViewById(R.id.customerButton);
        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customerActivity = new Intent(getApplicationContext(),CustomerHome.class);
                customerActivity.putExtras(getIntent().getExtras());
                startActivity(customerActivity);
            }
        });

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
        if(loginListner != null) {
            login.removeAuthStateListener(loginListner);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custommer_action_bar, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.changeRoleCustomer).setVisible(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.signout:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.changeRoleCustomer:
                Intent customerActivity = new Intent(getApplicationContext(), CustomerHome.class);
                customerActivity.putExtra("roles",roles);
                startActivity(customerActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
