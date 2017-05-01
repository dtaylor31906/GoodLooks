package novapplications.goodlooks.datamodels;


import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private ArrayList<String> roles;
    private ArrayList<CustomerAppointment>appointments;
    public static final String ROLE_CUSTOMER = "customer";
    public static final String ROLE_STYLIST = "stylist";
    public static final String ROLE_OWNER = "owner";
    private static final int MAX_NUMBER_OF_ROLES = 3;




    public User() {

    }

    public User(String firstName, String lastName)
    {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        roles = new ArrayList<String>();
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public void addToRoles(String role)
    {
        roles.add(role);
    }

    public ArrayList<CustomerAppointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<CustomerAppointment> appointments) {
        this.appointments = appointments;
    }

    public int getMAX_NUMBER_OF_ROLES() {
        return MAX_NUMBER_OF_ROLES;
    }
}
