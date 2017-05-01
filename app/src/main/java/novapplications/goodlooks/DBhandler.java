package novapplications.goodlooks;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import novapplications.goodlooks.models.Appointment;
import novapplications.goodlooks.models.Service;
import novapplications.goodlooks.models.Stylist;

public class DBhandler
{
    public static DatabaseReference DB;
    public static DatabaseReference users;
    public static DatabaseReference owners;
    public static DatabaseReference stylists;
    public static  DatabaseReference serviceCategories;
    public static DatabaseReference appointments;
    private final String TAG = "DBHandler";

    public DBhandler()
    {
        DB = FirebaseDatabase.getInstance().getReference();
        users = DB.child("users");
        owners = DB.child("owners");
        stylists = DB.child("stylists");
        serviceCategories = DB.child("serviceCategories");
        appointments = DB.child("appointments");
    }
    //creates a path from data ref
    public String pathCreator(DatabaseReference s)
    {
        String result = s.toString().split("firebaseio.com/")[1];
        return result;
    }

    public void addServiceToStylist(final Stylist stylist, Service service, String uid)
    {
        //add service object to list in the stylists node

        DatabaseReference servicesOfferedRef = stylists.child(uid).child("servicesOffered");
        String key = servicesOfferedRef.push().getKey();
        servicesOfferedRef.child(key).setValue(service);

        //Check if the stylist needs to be added to ableStylist list for a category
        DatabaseReference currentCategoryRef = servicesOfferedRef.child(service.getCategory());
        final DatabaseReference ableStylists = currentCategoryRef.child("ableStylists").child(uid);
        ableStylists.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.exists())
                {
                    //add stylist to list
                    ableStylists.setValue("firstName",stylist.getFirstName());
                    ableStylists.setValue("lastName",stylist.getLastName());
                    ableStylists.setValue("longitude",stylist.getLongitude());
                    ableStylists.setValue("latitude",stylist.getLatitude());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,databaseError.getMessage());
            }
        });
    }
    public void addAppointment(Appointment appointment)
    {
        //add Appointment to customer
        DatabaseReference appointmentsRef = users.child("appointments");
        String key = appointmentsRef.push().getKey();
        appointmentsRef.child(key).setValue(appointment);

        //add Appointment to appointmentLog;
        key = appointments.push().getKey();
        appointments.child(key).setValue(appointment);

    }

    public ValueEventListener stylistsGetAll(final ArrayList<Stylist> stylists)
    {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = data.iterator();
                    while (iterator.hasNext())
                    {
                        stylists.add(iterator.next().getValue(Stylist.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }


}
