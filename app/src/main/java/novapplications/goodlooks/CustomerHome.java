package novapplications.goodlooks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import novapplications.goodlooks.models.User;
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


public class CustomerHome extends AppCompatActivity
{
    public static final int REQUEST_CODE_LOGIN = 1;
    private static final String TAG = "CustomerHome";
    protected FirebaseAuth login;
    protected FirebaseAuth.AuthStateListener loginListner;
    private ListView appointments;
    private String[] roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            getRolesFromDataBase();
        }
        else
        {
            roles = extras.getStringArray("roles");
        }
        customizeActionBar();
        handleLogin();
        bindListView();
    }

    private void getRolesFromDataBase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUserRoles = ref.child("users/" + uid+"/roles");
        currentUserRoles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.d(TAG,"DATAcHANGED");
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                Iterator<DataSnapshot> itterator = data.iterator();
                roles = new String[(int) dataSnapshot.getChildrenCount()];
                int i = 0;
                while (itterator.hasNext())
                {
                    roles[i++] = itterator.next().getKey();
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.getMessage());
            }
        });
    }

    private void bindListView() {
        appointments = (ListView)findViewById(R.id.appointmentListView );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custommer_action_bar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        //check what roles to add to menu
/*
        boolean check = true;
        int totalTime = 0;
        int maxTime = 1500000;
        int delay = 2000;
        while (check)
        {
            if(roles == null)
            {
                Log.d(TAG,"waiting on getrolesfromdatabase call(1)");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"waiting on getrolesfromdatabase call");
                    }
                }, delay);

                totalTime += delay;
                Log.d(TAG,"elapsed: "+totalTime);
            }
            else
            {
                check = false;
            }


            if(totalTime > maxTime)
            {
                check= false;
                Log.d(TAG,"exceddd max time");
            }
        }*/
        if(roles == null)
        {
            return true;
        }
        else
        {
            if(roles.length == 1)
            {
                //display no role change options.
            }
            else
            {
                //display stylist role
                MenuItem stylistItem = menu.findItem(R.id.changeRoleStylist);
                stylistItem.setVisible(true);
            }
            return true;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.signout:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.changeRoleStylist:
                Intent stylistActivity = new Intent(this,StylistHome.class);
                stylistActivity.putExtra("roles",roles);
                startActivity(stylistActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //containst code for using the actionbar
    protected void customizeActionBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar myBar = getSupportActionBar();

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

    protected void handleLogin()
    {
        login = FirebaseAuth.getInstance();
        loginListner = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user !=null)
                {
                    //signed in
                    //figures out the role of the user, then displays the page accordingly.
                    //If users have multiple roles, an intermediate activity should be launched to make selection
                    onSignIn();
                }
                else
                {
                    //signed out
                    //takes you to the login page proivded by the firebase api
                    onSignOut();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),REQUEST_CODE_LOGIN);
                }
            }
        };


    }



    private void onSignOut() {
        Toast.makeText(this, "signedout", Toast.LENGTH_SHORT).show();
    }

    //set information needed after siging in
    //figure out the right page to display based on the user's role
    private void onSignIn() {
        //populate listview anduser info

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //closes activity if user hits back button from login page
        if(REQUEST_CODE_LOGIN == requestCode)
        {
            if(resultCode == RESULT_CANCELED)
            {
                finish();
            }
        }
    }
}

