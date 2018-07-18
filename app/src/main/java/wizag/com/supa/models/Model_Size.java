package wizag.com.supa.models;

public class Model_Size {
    private String size;
    private int id;

    public Model_Size() {
    }

    public Model_Size(String size, int id) {
        this.size = size;
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
