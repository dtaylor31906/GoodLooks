package novapplications.goodlooks.models;

import java.util.ArrayList;

/**
 * Created by Nova on 4/10/2017.
 */

public class Stylist
{
    private String firstName, lastName;
    private double longitude, latitude;
    private ArrayList<Service> servicesOffered;
    public Stylist()
    {

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
