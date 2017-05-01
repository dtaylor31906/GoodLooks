package novapplications.goodlooks;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Iterator;

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
    private ArrayAdapter<String> babyStylistadapter;
    private ListView StylistListView;
    private SharedPreferences userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_stylist_picker2);
        handleLogin();
        userPref = getApplicationContext().getSharedPreferences("user", MODE_PRIVATE);
        stylistsGetAll(babyStylists);
        StylistListView = (ListView) findViewById(R.id.stylist_listview);

        String[] genitalia = new String[]{"Vagina", "Pussy", "Coochie", "Cat", "Cunt", "Twat", "Punanny", "Yum-Yum"};
        ArrayList<String> genitaliaList = new ArrayList<String>();
        genitaliaList.addAll(Arrays.asList(genitalia));
        babyStylistadapter = new ArrayAdapter<String>(this, R.layout.listviewcomponents, genitaliaList);
        stylistListener = mDBhandler.stylistsGetAll(babyStylists);
        stylist.addListenerForSingleValueEvent(stylistListener);


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
        mStylist = babyStylists.get(0);
        babyStylistadapter.add(mStylist.getFirstName());
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