package wizag.com.supa.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Model_Buy {

    int material_type_id, material_item_id, material_unit_id, material_class_id, material_detail_id;
    String material_quantity;

    public Model_Buy(int material_type_id, int material_item_id, int material_unit_id, int material_class_id, int material_detail_id, String material_quantity) {
        this.material_type_id = material_type_id;
        this.material_item_id = material_item_id;
        this.material_unit_id = material_unit_id;
        this.material_class_id = material_class_id;
        this.material_detail_id = material_detail_id;
        this.material_quantity = material_quantity;
    }


    public int getMaterial_item_id() {
        return material_item_id;
    }

    public int getMaterial_unit_id() {
        return material_unit_id;
    }

    public int getMaterial_class_id() {
        return material_class_id;
    }

    public int getMaterial_detail_id() {
        return material_detail_id;
    }

    public int getMaterial_type_id() {
        return material_type_id;
    }

    public String getMaterial_quantity() {
        return material_quantity;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("material_type_id", material_type_id);
            obj.put("material_item_id", material_item_id);
            obj.put("material_detail_id", material_detail_id);
            obj.put("material_class_id", material_class_id);
            obj.put("material_unit_id", material_unit_id);
            obj.put("material_quantity", material_quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
