package novapplications.goodlooks;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import novapplications.goodlooks.datamodels.Appointment;

/**
 * Created by Nova on 4/10/2017.
 */

public class CustomerAppointmentAdaptar extends ArrayAdapter<Appointment>{

    Context context;
    int resource;
    ArrayList<Appointment> appointments = null;

    public CustomerAppointmentAdaptar(@NonNull Context context, @LayoutRes int resource, ArrayList<Appointment> appointments) {
        super(context, resource, appointments);
        this.context =context;
        this.resource = resource;
        this.appointments = appointments;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Appointment appointment = appointments.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.customer_list_item,parent,false);
        }
        bindView(convertView);
        return convertView;
    }

    private void bindView(View convertView) {
        TextView description = (TextView) convertView.findViewById(R.id.descriptionTextView);
        TextView date = (TextView) convertView.findViewById(R.id.dateTimeTextView);

    }
}
