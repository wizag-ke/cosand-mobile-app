package wizag.com.supa.models;

public class Model_Trucks {
    private String model, make, axle_count, plate_no, tonnage_id;
    int tonnage_id_value;

    public Model_Trucks() {
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setAxle_count(String axle_count) {
        this.axle_count = axle_count;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public void setTonnage_id(String tonnage_id) {
        this.tonnage_id = tonnage_id;
    }

    public void setTonnage_id_value(int tonnage_id_value) {
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
}
