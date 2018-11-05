package wizag.com.supa.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Model_Buy {

int material_id,material_unit_id,material_class_id,material_detail_id;
String material_quantity;

    public Model_Buy(int material_id, int material_unit_id, int material_class_id, int material_detail_id, String material_quantity) {
        this.material_id = material_id;
        this.material_unit_id = material_unit_id;
        this.material_class_id = material_class_id;
        this.material_detail_id = material_detail_id;
        this.material_quantity = material_quantity;
    }


    public int getMaterial_id() {
        return material_id;
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

    public String getMaterial_quantity() {
        return material_quantity;
    }


    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("material_id",material_id );
            obj.put("material_detail_id", material_detail_id);
            obj.put("material_class_id", material_class_id);
            obj.put("material_unit_id", material_unit_id);
            obj.put("material_quantity",material_quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
