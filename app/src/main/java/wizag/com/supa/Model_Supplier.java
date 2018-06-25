package wizag.com.supa;

public class Model_Supplier {
    private int id;
    private String name;

    public Model_Supplier() {
    }

    public Model_Supplier(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Model_Supplier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
