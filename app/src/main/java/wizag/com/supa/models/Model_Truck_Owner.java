package wizag.com.supa.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Model_Truck_Owner {
    private String dl_image, plate_no, driver_id_no;


    public Model_Truck_Owner() {
    }

    public Model_Truck_Owner(String dl_image, String plate_no, String driver_id_no) {
        this.dl_image = dl_image;
        this.plate_no = plate_no;
        this.driver_id_no = driver_id_no;
    }

    public String getDl_image() {
        return dl_image;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public String getDriver_id_no() {
        return driver_id_no;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dl_image", dl_image);
            obj.put("plate_no", plate_no);
            obj.put("driver_id_no", driver_id_no);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
