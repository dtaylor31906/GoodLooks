package novapplications.goodlooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Arrays;

/*This class is the controller for the very first activity of the App.
* It will basically be a logo or image to the user.
* It will be checking for authentication and deciding which type of experience to provide based on
* the type of user that is authorized.*/
public class StartPage extends AppCompatActivity
{

    public static final int REQUEST_CODE_LOGIN = 1;
    private static final String TAG ="StartPage" ;
    protected FirebaseAuth login;
    protected FirebaseAuth.AuthStateListener loginListner;
    private DatabaseReference DB;
    private DatabaseReference users;
    private DatabaseReference owners;
    private DatabaseReference stylists;
    private FirebaseUser user;
    private User newUser;
    private EditText firstName, lastName;
    private CheckBox customerCB, ownerCB, stylistCB;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //get datbase refrence
        DB = FirebaseDatabase.getInstance().getReference();
        users = DB.child("users");
        owners = DB.child("owners");
        stylists = DB.child("stylists");
        login = FirebaseAuth.getInstance();
        handleLogin();
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

    protected void handleLogin()
    {

        loginListner = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                user = firebaseAuth.getCurrentUser();

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
                                    .build(), REQUEST_CODE_LOGIN);
                }
            }
        };


    }



    private void onSignOut() {

    }

    //set information needed after siging in
    //figure out the right page to display based on the user's role
    private void onSignIn()
    {
        //Check user roles
        final String uid = user.getUid();
        checkUserRole();

        //to start there will only be a place for customers.
        //start activity for customer

    }

    private void checkUserRole()
    {
        final String uid = user.getUid();
        //database read
        users.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())//if user in the database
                {
                    Intent customerActivity = new Intent(getApplicationContext(),CustomerHome.class);
                    //TODO: get customer object from database, then put in in intent
                    startActivity(customerActivity);
                }
                else//if user not in database add user and get information
                {
                    //init view for collecting user data.
                    initiateSignUp(uid);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.w(TAG, "checkRoles:onCancelled", databaseError.toException());
            }
        });

    }

    private void initiateSignUp(String userid)
    {
        setContentView(R.layout.activity_start_page);
        final String uid = userid;
        firstName = (EditText)findViewById(R.id.firstNameEditText);
        lastName = (EditText)findViewById(R.id.lastNameEditText);
        customerCB = (CheckBox)findViewById(R.id.customerCheckBox);
        ownerCB = (CheckBox)findViewById(R.id.ownerCheckBox);
        stylistCB = (CheckBox)findViewById(R.id.stylistCheckBox);
        submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                newUser  = new User(firstName.getText().toString(),lastName.getText().toString(),user.getUid());
                newUser.addToRoles(User.ROLE_CUSTOMER);//everyone will have the role of customer
                //add other roles if checked
                if(ownerCB.isChecked())
                {
                    //role
                    newUser.addToRoles(User.ROLE_OWNER);
                }
                if(stylistCB.isChecked())
                {
                    //role
                    newUser.addToRoles(User.ROLE_STYLIST );
                }
                storeUser(uid,newUser);
                //users.child(uid).setValue(newUser);//stores user information in the database
                goToHome();
            }
        });

    }

    private void storeUser(String uid, User user)
    {
        DatabaseReference uidRef = users.child(uid);
        uidRef.child("firstName").setValue(user.getFirstName());
        uidRef.child("lastName").setValue(user.getLastName());
        //store roles
        DatabaseReference rolesRef = uidRef.child("roles");
        for (int i = 0; i < user.getNumberOfRoles(); i++)
        {
            String currentRole = user.getRoles()[i];
            rolesRef.child(currentRole).setValue(true);
            if(i>=1) //no need to add customer ; customer is always first; every user is a customer by default
            {
                String rolePlural = currentRole + "s";
                DatabaseReference currentRoleRef = DB.child(rolePlural).child(user.getUid());
                currentRoleRef.child("firstName").setValue(user.getFirstName());
                currentRoleRef.child("lastName").setValue(user.getLastName());
            }

        }
    }

    private void goToHome()
    {
        String[] rolesList = newUser.getRoles();
        if(rolesList.length == 1)
        {
            //launch customer home
            Intent customerActivity = new Intent(getApplicationContext(),CustomerHome.class);
            Bundle bundle = new Bundle();
            bundle.putString("firstName",newUser.getFirstName());
            bundle.putString("lastName",newUser.getLastName());
            bundle.putString("uid",newUser.getUid());
            bundle.putStringArray("roles",rolesList);
            customerActivity.putExtras(bundle);
            /*customerActivity.putExtra("firstName",newUser.getFirstName());
            customerActivity.putExtra("lastName",newUser.getLastName());
            customerActivity.putExtra("uid",newUser.getUid());
            customerActivity.putExtra("roles",rolesList);*/
            startActivity(customerActivity);
        }
        else //if other roles exist
        {
            chooseDestination(rolesList);
            /*Intent customerActivity = new Intent(getApplicationContext(),CustomerHome.class);
            startActivity(customerActivity);*/
        }

    }

    private void chooseDestination(String[] rolesList)
    {
        //the first role is always customer
        if(rolesList[1].equals(User.ROLE_OWNER))//if the second listing is a owner role
        {
            //go to owner home
            Intent ownerActivity = new Intent(getApplicationContext(),OwnerHome.class);
            ownerActivity.putExtra("user",newUser);
            startActivity(ownerActivity);
        }
        else
        {
            //open stylist home
            Intent stylistActivity = new Intent(getApplicationContext(),StylistHome.class);
            Bundle bundle = new Bundle();
            bundle.putString("firstName",newUser.getFirstName());
            bundle.putString("lastName",newUser.getLastName());
            bundle.putString("uid",newUser.getUid());
            bundle.putStringArray("roles",rolesList);
           /* stylistActivity.putExtra("firstName",newUser.getFirstName());
            stylistActivity.putExtra("lastName",newUser.getLastName());
            stylistActivity.putExtra("uid",newUser.getUid());
            stylistActivity.putExtra("roles",rolesList);*/
           stylistActivity.putExtras(bundle);
            startActivity(stylistActivity);
        }
    }


}
