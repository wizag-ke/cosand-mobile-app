package wizag.com.supa;

import java.util.List;

/**
 * Created by Abhi on 03 Jan 2018 003.
 */

public class Model_Material {
    private String material;
    private List<String> supplier;

    public Model_Material(String material, List<String> supplier) {
        this.material = material;
        this.supplier = supplier;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<String> getSupplier() {
        return supplier;
    }

    public void setSupplier(List<String> supplier) {
        this.supplier = supplier;
    }
}
