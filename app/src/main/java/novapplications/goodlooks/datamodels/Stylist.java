package novapplications.goodlooks.datamodels;

import java.util.ArrayList;

/**
 * Created by Nova on 4/10/2017.
 */

public class Stylist
{
    private String firstName, lastName;
    private double longitude, latitude;
    private ArrayList<Service> servicesOffered;
    private ArrayList<StylistAppointment> appointments;
    public Stylist()
    {

    }

    public ArrayList<StylistAppointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<StylistAppointment> appointments) {
        this.appointments = appointments;
    }

    public ArrayList<Service> getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(ArrayList<Service> servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
