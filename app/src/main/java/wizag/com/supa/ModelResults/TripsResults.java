package wizag.com.supa.ModelResults;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 08/05/2018.
 */

public class TripsResults {
    @SerializedName("order_no")

    private String  order_no;

    @SerializedName("from_loc")
    private String  from_loc;

    @SerializedName("to_loc")
    private String  to_loc;

    @SerializedName("start_time")
    private String  start_time;

    @SerializedName("end_time")
    private String  end_time;

    @SerializedName("duration")
    private String  duration;

    @SerializedName("name")
    private String  name;

    @SerializedName("status")
    private String  status;

    @SerializedName("material")
    private String  material;

    @SerializedName("payment")
    private String  payment;

    public TripsResults() {
    }

    public TripsResults(String order_no, String from_loc, String to_loc, String start_time,
                        String end_time, String duration, String name, String status,
                        String material, String payment){
        this.order_no = order_no;
        this.from_loc = from_loc;
        this.to_loc = to_loc;
        this.start_time = start_time;
        this.end_time = end_time;
        this.duration = duration;
        this.name = name;
        this.status = status;
        this.material = material;
        this.payment = payment;

    }

    public String getOrderNo() {
        return order_no;
    }

    public void setOrderNo(String order_no) {
        this.order_no = order_no;
    }

    public String getFromLoc() {
        return from_loc;
    }

    public void setFromLoc(String from_loc) {
        this.from_loc = from_loc;
    }

    public String getToLoc() {
        return to_loc;
    }

    public void setToLoc(String to_loc) {
        this.to_loc = to_loc;
    }

    public String getStartTime() {
        return start_time;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

}
