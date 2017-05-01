package novapplications.goodlooks.datamodels;

/**
 * Created by Nova on 5/1/2017.
 */

public class CustomerAppointment
{
    private String stylistFirstName, stylistLastName, stylistUid;
    private long startTime, endTime;

    public CustomerAppointment()
    {

    }

    public CustomerAppointment(Appointment appointment)
    {
        stylistFirstName = appointment.getStylistFirstName();
        stylistLastName = appointment.getStylistLastName();
        stylistUid = appointment.getStlylistUid();
        startTime = appointment.getStartTime();
        endTime = appointment.getEndTime();
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

    public String getStylistUid() {
        return stylistUid;
    }

    public void setStylistUid(String stylistUid) {
        this.stylistUid = stylistUid;
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

}
