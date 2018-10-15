package wizag.com.supa.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import wizag.com.supa.BR;

public class Model_Supplier extends BaseObservable {

    String fname, lname, email, phone, id_no, company, kra_pin, company_phone, location, password, confirm_password;

    @Bindable
    public String getFname() {
        return fname;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    @Bindable
    public String getConfirm_password() {
        return confirm_password;
    }

    public void setFname(String fname) {
        this.fname = fname;
        notifyPropertyChanged(BR.fname);
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
        notifyPropertyChanged(BR.confirm_password);
    }


    @Bindable
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
        notifyPropertyChanged(BR.lname);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
        notifyPropertyChanged(BR.id_no);
    }

    @Bindable
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
        notifyPropertyChanged(BR.company);
    }

    @Bindable
    public String getKra_pin() {
        return kra_pin;
    }

    public void setKra_pin(String kra_pin) {
        this.kra_pin = kra_pin;
        notifyPropertyChanged(BR.kra_pin);
    }

    @Bindable
    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
        notifyPropertyChanged(BR.company);
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    @Bindable({"fname"})
    public String getFnameError(){
        if(getFname().isEmpty()){
            return "Enter First name";
        }
        return "";
    }

    @Bindable({"lname"})
    public String getLnameError(){
        if(getLname().isEmpty()){
            return "Enter Last name";
        }
        return "";
    }

    @Bindable({"email"})
    public String getEmailError(){
        if(getEmail().isEmpty()){
            return "Enter Email";
        }
        return "";
    }

    @Bindable({"phone"})
    public String getPhoneError(){
        if(getPhone().isEmpty()){
            return "Enter Phone number";
        }
        return "";
    }

    @Bindable({"id_no"})
    public String getIdError(){
        if(getPhone().isEmpty()){
            return "Enter Phone number";
        }
        return "";
    }

    /*validations*/
    @Bindable({"password"})
    public String getPasswordError() {
        if (getPassword().isEmpty()) {
            return "Enter Password";
        } else {
            return "";
        }
    }

    @Bindable({"confirm_password"})
    public String getConfirmPasswordError() {
        if (!getPassword().equalsIgnoreCase(getConfirm_password())) {
            return "Passwords do not match";
        } else {
            return "";
        }
    }


}
