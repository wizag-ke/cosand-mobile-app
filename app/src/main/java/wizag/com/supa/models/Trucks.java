package wizag.com.supa.models;

public class Trucks {

    String plate_no, tonnage, make, model, year;

    public Trucks(String plate_no, String tonnage, String make, String model, String year) {
        this.plate_no = plate_no;
        this.tonnage = tonnage;
        this.make = make;
        this.model = model;
        this.year = year;


    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public String getTonnage() {
        return tonnage;
    }

    public void setTonnage(String tonnage) {
        this.tonnage = tonnage;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


}
