package wizag.com.supa;

import java.util.List;


public class MaterialModel {
    private String material_name;
    private List<String> supplier_name;

    public MaterialModel(String material_name, List<String> supplier_name) {
        this.material_name = material_name;
        this.supplier_name = supplier_name;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public List<String> getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(List<String> supplier_name) {
        this.supplier_name = supplier_name;
    }
}


