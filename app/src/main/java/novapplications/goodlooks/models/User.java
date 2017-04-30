package novapplications.goodlooks.models;


import java.io.Serializable;

public class User implements Serializable{
    private String firstName, lastName, uid;
    private String[] roles;
    private int roleCount;
    public static final String ROLE_CUSTOMER = "customer";
    public static final String ROLE_STYLIST = "stylist";
    public static final String ROLE_OWNER = "owner";
    private final int MAX_NUMBER_OF_ROLES = 3;




    public User() {

    }

    public User(String firstName, String lastName,String uid)
    {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.uid = uid;
        roles = new String[MAX_NUMBER_OF_ROLES];
        roleCount = 0;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String[] getRoles() {
        String[] result = new String[roleCount];
        for (int i = 0; i < roleCount; i++) {
            result[i] = roles[i];
        }
        return result;

    }

    public void setRoles(String[] roles) {
        this.roles = roles;
        roleCount= roles.length;
    }
    public void addToRoles(String role)
    {
        for (int i = 0; i < roles.length; i++)
        {
            if(roles[i] == null)//if the current slot being explored is empty
            {
                //add role
                roles[i] = role;
                //incrment the counter
                roleCount++;
                return;
            }
        }
    }
    public int getNumberOfRoles()
    {
     return roleCount;
    }

    public int getRoleCount() {
        return roleCount;
    }

    public void setRoleCount(int roleCount) {
        this.roleCount = roleCount;
    }

    public static String getRoleCustomer() {
        return ROLE_CUSTOMER;
    }

    public static String getRoleStylist() {
        return ROLE_STYLIST;
    }

    public static String getRoleOwner() {
        return ROLE_OWNER;
    }

    public int getMAX_NUMBER_OF_ROLES() {
        return MAX_NUMBER_OF_ROLES;
    }
}
