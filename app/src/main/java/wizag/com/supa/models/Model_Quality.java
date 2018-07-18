package wizag.com.supa.models;

public class Model_Quality {
    private String value;
    private int id;

    public Model_Quality() {

    }

    public Model_Quality(String value, int id) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
