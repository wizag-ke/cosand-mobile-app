package wizag.com.supa.ModelResults;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 15/05/2018.
 */

public class PaymentResults {
    @SerializedName("company")
    private String  company;

    @SerializedName("day_date")
    private String  day_date;

    @SerializedName("amount")
    private String  amount;

    public PaymentResults() {
    }

    public PaymentResults(String company, String day_date, String amount){
        this.company = company;
        this.day_date = day_date;
        this.amount = amount;


    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDayDate() {
        return day_date;
    }

    public void setDayDate(String day_date) {
        this.day_date = day_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
