package novapplications.goodlooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import novapplications.goodlooks.datamodels.User;

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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private FirebaseUser user;
    private User newUser;
    private EditText firstName, lastName;
    private CheckBox customerCB, ownerCB, stylistCB;
    private Button submitButton;
    private ValueEventListener rolesListener;
    private DatabaseReference currentUserRef;
    private SharedPreferences userPref;
    private SharedPreferences.Editor editor;
    private DBhandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //get datbase refrence
        db = new DBhandler();
        userPref = getApplicationContext().getSharedPreferences("user",MODE_PRIVATE);
        editor = userPref.edit();
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
        removeListeners();
    }

    private void removeListeners()
    {
        if(loginListner != null)
        {
            login.removeAuthStateListener(loginListner);
        }
        if(rolesListener != null)
        {
            currentUserRef.removeEventListener(rolesListener);
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



    private void onSignOut()
    {
        removeListeners();
    }

    //set information needed after siging in
    //figure out the right page to display based on the user's role
    private void onSignIn()
    {
        //Check user roles
        final String uid = user.getUid();
        checkUserRole();


    }

    private void checkUserRole()
    {
        final String uid = user.getUid();
        //database read
        //if user in the database
//TODO: get customer object from database, then put in in intent
//if user not in database add user and get information
//init view for collecting user data.
        rolesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())//if user in the database
                {
                    Intent customerActivity = new Intent(getApplicationContext(), CustomerHome.class);
                    //TODO: get customer object from database, then put in in intent
                    SetUserPrefFromDatabase(dataSnapshot);
                    startActivity(customerActivity);
                } else//if user not in database add user and get information
                {
                    //init view for collecting user data.
                    initiateSignUp(uid);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "checkRoles:onCancelled", databaseError.toException());
            }
        };
        currentUserRef = DBhandler.users.child(uid);
        currentUserRef.addListenerForSingleValueEvent(rolesListener);

    }

    private void SetUserPrefFromDatabase(DataSnapshot dataSnapshot) {
        User value = dataSnapshot.getValue(User.class);
        saveUserPreference(user.getUid(),value);

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

                newUser  = new User(firstName.getText().toString(),lastName.getText().toString());
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
        Map<String, Object> update = new HashMap<String, Object>();
        DatabaseReference uidRef = DBhandler.users.child(uid);
        update.put(db.pathCreator(uidRef),newUser);
        /*uidRef.setValue(newUser);*/
       /* uidRef.child("firstName").setValue(user.getFirstName());
        uidRef.child("lastName").setValue(user.getLastName());*/

        //store roles
        DatabaseReference rolesRef = uidRef.child("roles");
        for (int i = 0; i < user.getRoles().size(); i++)
        {
            String currentRole = user.getRoles().get(i);
            rolesRef.child(currentRole).setValue(true);
            if(i>=1) //no need to add customer ; customer is always first; every user is a customer by default
            {
                String rolePlural = currentRole + "s";
                DatabaseReference currentRoleRef = DBhandler.DB.child(rolePlural).child(uid);
                update.put(db.pathCreator(currentRoleRef.child("firstName")),user.getFirstName());
                update.put(db.pathCreator(currentRoleRef.child("lastName")),user.getLastName());
              /*  currentRoleRef.child("firstName").setValue(user.getFirstName());
                currentRoleRef.child("lastName").setValue(user.getLastName());*/
            }


        }
        saveUserPreference(uid,user);
        DBhandler.DB.updateChildren(update);
    }



    private void saveUserPreference(String uid, User user)
    {
        editor.putString("uid",uid);
        editor.putString("firstName",user.getFirstName());
        editor.putString("lastName",user.getLastName());
        editor.putStringSet("roles", makeSet(user.getRoles()));
        editor.commit();
    }

    private void goToHome()
    {
        ArrayList<String> rolesList = newUser.getRoles();
        if(rolesList.size() == 1)
        {
            //launch customer home
            Intent customerActivity = new Intent(this,CustomerHome.class);
            startActivity(customerActivity);
        }
        else //if other roles exist
        {
            chooseDestination(rolesList);
            /*Intent customerActivity = new Intent(getApplicationContext(),CustomerHome.class);
            startActivity(customerActivity);*/
        }

    }

    private void chooseDestination(ArrayList<String> rolesList)
    {
        //the first role is always customer
        if(rolesList.get(1).equals(User.ROLE_OWNER))//if the second listing is a owner role
        {
            //go to owner home
            Intent ownerActivity = new Intent(getApplicationContext(),OwnerHome.class);;
            startActivity(ownerActivity);
        }
        else
        {
            //open stylist home
            Intent stylistActivity = new Intent(this,StylistHome.class);
            Bundle bundle = new Bundle();
            bundle.putString("firstName",newUser.getFirstName());
            bundle.putString("lastName",newUser.getLastName());
            bundle.putString("uid",user.getUid());
            bundle.putStringArray("roles",rolesList.toArray(new String[rolesList.size()]));
           /* stylistActivity.putExtra("firstName",newUser.getFirstName());
            stylistActivity.putExtra("lastName",newUser.getLastName());
            stylistActivity.putExtra("uid",newUser.getUid());
            stylistActivity.putExtra("roles",rolesList);*/
           stylistActivity.putExtras(bundle);
            /*SharedPreferences pref = getApplicationContext().getSharedPreferences("user_info", MODE_PRIVATE);*/
            /*SharedPreferences.Editor editor = pref.edit();*/
            /*editor.putStringSet("roles",makeSet(rolesList));*/
            startActivity(stylistActivity);
        }
    }

    private Set<String> makeSet(ArrayList<String> rolesList) {
        Set<String> result = new HashSet<String>();
        for (int i = 0; i <rolesList.size() ; i++) {
            result.add(rolesList.get(i));
        }
        return result;
    }


}
