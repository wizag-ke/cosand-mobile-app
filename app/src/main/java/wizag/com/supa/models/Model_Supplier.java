package wizag.com.supa.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import wizag.com.supa.BR;

public class Model_Supplier {

    int id_material,id_detail,id_class,id_unit;
    String material_name,details_name,class_name,units_name,cost,location;




    public Model_Supplier() {
    }


    public Model_Supplier(int id_material, int id_detail, int id_class, int id_unit, String material_name, String details_name, String class_name, String units_name, String cost, String location) {
        this.id_material = id_material;
        this.id_detail = id_detail;
        this.id_class = id_class;
        this.id_unit = id_unit;
        this.material_name = material_name;
        this.details_name = details_name;
        this.class_name = class_name;
        this.units_name = units_name;
        this.cost = cost;
        this.location = location;
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

    public String getCost() { return cost; }
    public String getLocation() { return location; }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("material_item_id",id_material );
            obj.put("material_detail_id", id_detail);
            obj.put("material_class_id", id_class);
            obj.put("material_unit_id", id_unit);
            obj.put("material_unit_price",cost);
        } catch (JSONException e) {
            e.printStackTrace();
               }
        return obj;
    }

}
