package novapplications.goodlooks.models;

/**
 * Created by Nova on 4/10/2017.
 */
//Class the describes a service that will be offered at an establishment
public class Service
{
    private String name //name of the serivce e.g temp fade
            , price// price in USD  written as a string
            , description// brief description of service (optional for user entry)
            , category;// mandoroty field categorizes the service based on the chosen constants in this class
    public final String CATEGORY_MALE_HAIRCUT = "male haircut",CATEGORY_FEMALE_HAIRCUT = "female haircut",
    CATEGORY_OTHER = "other";

    public Service(String name, String price, String description, String category)
    {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public Service() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
