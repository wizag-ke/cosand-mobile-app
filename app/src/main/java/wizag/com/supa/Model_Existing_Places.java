package wizag.com.supa;

public class Model_Existing_Places {
    private String name;
    private int id;
    private String cordinates;

    public Model_Existing_Places() {
    }

    public Model_Existing_Places(String name, int id, String cordinates) {
        this.name = name;
        this.id = id;
        this.cordinates = cordinates;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCordinates() {
        return cordinates;
    }

    public void setCordinates(String cordinates) {
        this.cordinates = cordinates;
    }
}
