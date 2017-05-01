package novapplications.goodlooks.models;

import java.util.ArrayList;

/**
 * Created by Nova on 5/1/2017.
 */

public class customerAppointment
{
    private String stylistFirstName, stylistLastName, stlylistUid;
    private long startTime, endTime;
    private ArrayList<Service> servicesToBePerformed;//services to be performed.

    public customerAppointment()
    {

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

    public String getStlylistUid() {
        return stlylistUid;
    }

    public void setStlylistUid(String stlylistUid) {
        this.stlylistUid = stlylistUid;
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
}
