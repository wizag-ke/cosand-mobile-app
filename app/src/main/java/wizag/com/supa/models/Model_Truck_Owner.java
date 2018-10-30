package wizag.com.supa.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Model_Truck_Owner {
    private String model, make, axle_count, plate_no, tonnage_id;
    int tonnage_id_value;

    public Model_Truck_Owner() {
    }

    public Model_Truck_Owner(String model, String make, String axle_count, String plate_no, String tonnage_id, int tonnage_id_value) {
        this.model = model;
        this.make = make;
        this.axle_count = axle_count;
        this.plate_no = plate_no;
        this.tonnage_id = tonnage_id;
        this.tonnage_id_value = tonnage_id_value;
    }

    public String getModel() {
        return model;
    }

    public String getMake() {
        return make;
    }

    public String getAxle_count() {
        return axle_count;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public String getTonnage_id() {
        return tonnage_id;
    }

    public int getTonnage_id_value() {
        return tonnage_id_value;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("model", model);
            obj.put("make", make);
            obj.put("axle_count", axle_count);
            obj.put("plate_no", plate_no);
            obj.put("tonnage_id", tonnage_id_value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
