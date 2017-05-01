package novapplications.goodlooks.models;

import java.util.AbstractList;
import java.util.Date;


/**
 * Created by Nova on 4/10/2017.
 */

public class Appointment
{
    private User provider,customer;
    private Date startTime, estimatedEndTime, actualEndTime;
    private AbstractList<Service> servicesToBePerformed;//services to be performed.


    public Appointment() {
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEstimatedEndTime() {
        return estimatedEndTime;
    }

    public void setEstimatedEndTime(Date estimatedEndTime) {
        this.estimatedEndTime = estimatedEndTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public AbstractList<Service> getServicesToBePerformed() {
        return servicesToBePerformed;
    }

    public void setServicesToBePerformed(AbstractList<Service> servicesToBePerformed) {
        this.servicesToBePerformed = servicesToBePerformed;
    }
}
