package wizag.com.supa.ModelResults;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 18/05/2018.
 */

public class MaterialResults {
    @SerializedName("Country")
    private String Country;

    public MaterialResults() {
    }

    public MaterialResults(String Country){
        this.Country = Country;


    }

    public String getCompany() {
        return Country;
    }

    public void setCompany(String Country) {
        this.Country = Country;
    }

}
