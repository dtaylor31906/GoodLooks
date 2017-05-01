package novapplications.goodlooks;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import novapplications.goodlooks.datamodels.Appointment;
import novapplications.goodlooks.datamodels.CustomerAppointment;
import novapplications.goodlooks.datamodels.Service;
import novapplications.goodlooks.datamodels.Stylist;
import novapplications.goodlooks.datamodels.StylistAppointment;

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

    public void stylistsAddNewCustomerRequest(Appointment appointment) {
        //add appointment to log
        String appointmentID = appointments.push().getKey();
        //add stylist appointment as request
        String uid = appointment.getStlylistUid();
        if (uid != null) {
            DatabaseReference appointments = stylists.child(uid).child("appointments");
            String key = appointments.push().getKey();

            appointments.child(key).setValue(new StylistAppointment(appointment, appointmentID));
        }
        //add customer appointment
        DatabaseReference customerAppointments = users.child(appointment.getCustomerUid()).child("appointments");
        customerAppointments.push().setValue(new CustomerAppointment(appointment, appointmentID));
    }


}
