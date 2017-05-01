package novapplications.goodlooks;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import novapplications.goodlooks.datamodels.Appointment;
import novapplications.goodlooks.datamodels.Stylist;

public class CustomerStylistPicker2 extends AppCompatActivity {

    private static final String TAG = "Whaddup";
    DBhandler mDBhandler = new DBhandler();
    ValueEventListener stylistListener;
    DatabaseReference stylist = DBhandler.stylists;
    public Stylist mStylist;
    public static final int REQUEST_CODE_LOGIN = 1;
    protected FirebaseAuth login;
    protected FirebaseAuth.AuthStateListener loginListner;

    private ArrayList<Stylist> babyStylists = new ArrayList<>();
    private ArrayList<Stylist> stylistAdaptorHelper;
    private HashMap<String,Stylist> stylistMap = new HashMap<>();
    private ArrayAdapter<String> babyStylistadapter;
    private ListView StylistListView;
    private SharedPreferences userPref;
    private Appointment appointment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_stylist_picker2);
        userPref = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        stylistsGetAll(babyStylists);
        StylistListView = (ListView) findViewById(R.id.stylist_listview);
        userPref = getApplicationContext().getSharedPreferences("user",MODE_PRIVATE);
        String[] temp = new String[]{};
        ArrayList<String> tempList = new ArrayList<String>();
        tempList.addAll(Arrays.asList(temp));
        babyStylistadapter = new ArrayAdapter<String>(this, R.layout.listviewcomponents, tempList);
        stylistListener = stylistsGetAll(stylistMap);
        stylist.addListenerForSingleValueEvent(stylistListener);
        handleLogin();
        StylistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                appointment = new Appointment();
                appointment.setCustomerFirstName(userPref.getString("firstName",""));
                appointment.setCustomerLastName(userPref.getString("lastNme",""));
                appointment.setCustomerUid(userPref.getString("uid",""));
                appointment.setStylistFirstName(view.toString().split("/s")[0]);
                appointment.setStylistFirstName(view.toString().split("/s")[1]);
            }
        });
    }

    private ValueEventListener stylistsGetAll(final HashMap<String, Stylist> stylists)
    {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    stylists.clear();
                    Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = data.iterator();
                    DataSnapshot stylistDataSnapshot;
                    while (iterator.hasNext()) {
                        stylistDataSnapshot = iterator.next();
                        stylists.put(stylistDataSnapshot.getKey(), stylistDataSnapshot.getValue(Stylist.class));
                    }
                    initListView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
                Log.d(TAG, databaseError.getDetails());
            }
        };
    }

    protected void handleLogin() {
        login = FirebaseAuth.getInstance();
        loginListner = new FirebaseAuth.AuthStateListener() {
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
    protected void onResume() {
        super.onResume();
        login.addAuthStateListener(loginListner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeListeners();
    }

    private void removeListeners() {

        if (loginListner != null) {
            login.removeAuthStateListener(loginListner);
        }
        if (stylistListener != null) {
            stylist.removeEventListener(stylistListener);
        }
    }


    private void initListView() {
        stylistAdaptorHelper = new ArrayList<Stylist>();
        babyStylistadapter.clear();
        for(Map.Entry<String, Stylist> entry : stylistMap.entrySet())
        {
            babyStylistadapter.add(entry.getValue().getFirstName()+ " "+ entry.getValue().getLastName());
            stylistAdaptorHelper.add(entry.getValue());

        }
        StylistListView.setAdapter(babyStylistadapter);

    }

    public ValueEventListener stylistsGetAll(final ArrayList<Stylist> stylists) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    stylists.clear();
                    Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = data.iterator();
                    while (iterator.hasNext()) {
                        stylists.add(iterator.next().getValue(Stylist.class));
                    }
                    initListView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
                Log.d(TAG, databaseError.getDetails());
            }
        };


    }

}