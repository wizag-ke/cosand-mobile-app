package wizag.com.supa.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import wizag.com.supa.BR;

public class Model_Supplier {

    int id_material,id_detail,id_class,id_unit;
    String material_name,details_name,class_name,units_name,cost;




    public Model_Supplier() {
    }


    public Model_Supplier(int id_material, int id_detail, int id_class, int id_unit, String material_name, String details_name, String class_name, String units_name, String cost) {
        this.id_material = id_material;
        this.id_detail = id_detail;
        this.id_class = id_class;
        this.id_unit = id_unit;
        this.material_name = material_name;
        this.details_name = details_name;
        this.class_name = class_name;
        this.units_name = units_name;
        this.cost = cost;
    }

    public int getId_material() {
        return id_material;
    }

    public int getId_detail() {
        return id_detail;
    }

    public int getId_class() {
        return id_class;
    }

    public int getId_unit() {
        return id_unit;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public String getDetails_name() {
        return details_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getUnits_name() {
        return units_name;
    }

    public String getCost() {
        return cost;
    }
}
