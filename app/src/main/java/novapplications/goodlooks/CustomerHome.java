package novapplications.goodlooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import novapplications.goodlooks.models.*;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import novapplications.goodlooks.models.Appointment;


public class CustomerHome extends AppCompatActivity
{
    public static final int REQUEST_CODE_LOGIN = 1;
    private static final String TAG = "CustomerHome";
    protected FirebaseAuth login;
    protected FirebaseAuth.AuthStateListener loginListner;
    private ListView appointmentsListView;
    private ArrayList<customerAppointment> appointments;
    private String[] roles;
    private Button appointmentButton;
    DatabaseReference currentUserRoles;
    private ValueEventListener rolesListner;
    private View.OnClickListener appointmentButtonListener;
    private SharedPreferences userPref;
    private ValueEventListener appointmentsListner;
    private DatabaseReference appointmentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        userPref = getApplicationContext().getSharedPreferences("user",MODE_PRIVATE);
        appointments = new ArrayList<customerAppointment>();
        initListners();
        customizeActionBar();
        handleLogin();
        initView();
        getAppointments();

    }

    private void getAppointments()
    {
        String uid = userPref.getString("uid",null);
        DatabaseReference currentUserRef = DBhandler.users.child(uid);
        appointmentsListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = data.iterator();
                    while (iterator.hasNext()) {
                        appointments.add(iterator.next().getValue(customerAppointment.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
                Log.d(TAG, databaseError.getDetails());
            }
        };
        appointmentsRef = currentUserRef.child("appointments");
        appointmentsRef.addValueEventListener(appointmentsListner);
    }

    private void initListners()
    {
        appointmentButtonListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //inquire about services
                Intent categorySelection = new Intent(getApplicationContext(), CustomerCategoryPicker.class);
                startActivity(categorySelection);
                //then take customer to a page of stylist in there area that offer that service.
            }
        };
    }

    private void initView()
    {
        bindListView();
        appointmentButton = (Button) findViewById(R.id.appointmentButton);
        appointmentButton.setOnClickListener(appointmentButtonListener);
    }

    private void getRolesFromDataBase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserRoles = ref.child("users/" + uid+"/roles");
        rolesListner = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "DATAcHANGED");
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                Iterator<DataSnapshot> itterator = data.iterator();
                roles = new String[(int) dataSnapshot.getChildrenCount()];
                int i = 0;
                while (itterator.hasNext()) {
                    roles[i++] = itterator.next().getKey();
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };
        currentUserRoles.addListenerForSingleValueEvent(rolesListner);
    }

    private void bindListView() {
        appointmentsListView = (ListView)findViewById(R.id.appointmentListView );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.default_action_bar, menu);
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

        removeListners();
    }

    private void removeListners()
    {
        if(loginListner != null) {
            login.removeAuthStateListener(loginListner);
        }
        if(rolesListner != null)
        {
            currentUserRoles.removeEventListener(rolesListner);
        }
        if (appointmentsListner != null)
        {
            appointmentsRef.removeEventListener(appointmentsListner);
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
                    onSignIn(user);
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
    private void onSignIn(FirebaseUser user) {

        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            getRolesFromDataBase();
        }
        else
        {
            roles = extras.getStringArray("roles");
        }
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

