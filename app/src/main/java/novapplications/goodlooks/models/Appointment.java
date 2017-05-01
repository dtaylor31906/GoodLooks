package novapplications.goodlooks.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class Appointment
{
    private String customerFirstName,customerLastName, stylistFirstName, stylistLastName, customerUid, stlylistUid;
    private long startTime, endTime;
    private ArrayList<Service> servicesToBePerformed;//services to be performed.


    public Appointment()
    {

    }

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public String getStlylistUid() {
        return stlylistUid;
    }

    public void setStlylistUid(String stlylistUid) {
        this.stlylistUid = stlylistUid;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getStylistFirstName() {
        return stylistFirstName;
    }

    public void setStylistFirstName(String stylistFirstName) {
        this.stylistFirstName = stylistFirstName;
    }

    public String getStylistLastName() {
        return stylistLastName;
    }

    public void setStylistLastName(String stylistLastName) {
        this.stylistLastName = stylistLastName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Service> getServicesToBePerformed() {
        return servicesToBePerformed;
    }

    public void setServicesToBePerformed(ArrayList<Service> servicesToBePerformed) {
        this.servicesToBePerformed = servicesToBePerformed;
    }

    public Date getStartDateTime()
    {
        return new Date(startTime);
    }
    public Date getEndDateTime()
    {
        return new Date(endTime);
    }
}
