package wizag.com.supa;

public class User {
    private  String email,f_name,l_name,id_no,password,phone;
    private  int role_id;

    public User(String email, String f_name, String l_name, String id_no, String password, String phone, int role_id) {
        this.email = email;
        this.f_name = f_name;
        this.l_name = l_name;
        this.id_no = id_no;
        this.password = password;
        this.phone = phone;
        this.role_id = role_id;
    }

    public String getEmail() {
        return email;
    }

    public String getF_name() {
        return f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public String getId_no() {
        return id_no;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public int getRole_id() {
        return role_id;
    }
}
