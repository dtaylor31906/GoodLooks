package novapplications.goodlooks.datamodels;


public class StylistAppointment
{
    private String customerFirstName,customerLastName, customerUid;
    private long startTime, endTime;

    public StylistAppointment() {
    }

    public StylistAppointment(Appointment appointment) {
        customerFirstName = appointment.getCustomerFirstName();
        customerLastName = appointment.getCustomerLastName();
        customerUid = appointment.getCustomerUid();
        startTime = appointment.getStartTime();
        endTime = appointment.getEndTime();
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

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
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
