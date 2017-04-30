package novapplications.goodlooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class CustomerCategoryPicker extends AppCompatActivity {
    public static final int REQUEST_CODE_LOGIN = 1;
    protected FirebaseAuth login;
    protected FirebaseAuth.AuthStateListener loginListner;
    private TextView itemSelection;
    private ListView categories;
    private AdapterView.OnItemClickListener categoriesOnItemClick;
    private String currentItem;
    private EditText distanceEditText;
    private Button searchButton;
    private View.OnClickListener searchButtonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_category_picker);
        handleLogin();
        initListeners();
        initView();
    }

    private void initView()
    {
        categories = (ListView) findViewById(R.id.categoryListView);
        categories.setOnItemClickListener(categoriesOnItemClick);
        itemSelection = (TextView)findViewById(R.id.searchForTextView);
        distanceEditText = (EditText)findViewById(R.id.distanceEditText);
        searchButton = (Button)findViewById(R.id.searchButton);

        searchButton.setOnClickListener(searchButtonListener);
    }


    private void initListeners()
    {
        categoriesOnItemClick = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                currentItem = parent.getAdapter().getItem(position).toString();
                if(itemSelection == null)
                {
                    itemSelection = (TextView)findViewById(R.id.searchForTextView);
                }
               itemSelection.setText("Searching For " + currentItem);


            }
        };

        searchButtonListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent pickStylistActivity = new Intent(getApplicationContext(),CustomerStylistPicker.class);
                if(checkFields())
                {
                    pickStylistActivity.putExtra("category",currentItem);
                    pickStylistActivity.putExtra("zipCode",getZipCode());
                    pickStylistActivity.putExtra("distance",getDistance());
                    startActivity(pickStylistActivity);
                }

            }
        };
    }
    //return distance in miles from the view
    private int getDistance() {
        return 0;
    }

    //check and make sure the required data has been entered by the user before proceeding.
    private boolean checkFields()
    {
        return true;
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
    //grab zipcode from the view
    public int getZipCode()
    {
        return 0;
    }
}
