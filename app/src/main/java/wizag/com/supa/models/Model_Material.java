package wizag.com.supa.models;

public class Model_Material {

    public String id, name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return "Model_Material{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Model_Material) {
            Model_Material c = (Model_Material) obj;
            if (c.getName().equals(name) && c.getId() == id) return true;
        }

        return false;
    }
}


