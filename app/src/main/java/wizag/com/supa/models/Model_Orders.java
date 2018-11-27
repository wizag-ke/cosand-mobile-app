package wizag.com.supa.models;

public class Model_Orders {

  public Model_Orders(){}

    public String material_type;
    public String material_item;
    public String material_detail;
    public String material_class;
    public String material_quantity;
    public String quote;
    public String order_status;
    public String order_id;


  public String getMaterial_type() {
    return material_type;
  }

  public void setMaterial_type(String material_type) {
    this.material_type = material_type;
  }

  public String getMaterial_item() {
    return material_item;
  }

  public void setMaterial_item(String material_item) {
    this.material_item = material_item;
  }

  public String getMaterial_detail() {
    return material_detail;
  }

  public void setMaterial_detail(String material_detail) {
    this.material_detail = material_detail;
  }

  public String getMaterial_class() {
    return material_class;
  }

  public void setMaterial_class(String material_class) {
    this.material_class = material_class;
  }

  public String getMaterial_quantity() {
    return material_quantity;
  }

  public void setMaterial_quantity(String material_quantity) {
    this.material_quantity = material_quantity;
  }

  public String getQuote() {
    return quote;
  }

  public void setQuote(String quote) {
    this.quote = quote;
  }

  public String getOrder_status() {
    return order_status;
  }

  public void setOrder_status(String order_status) {
    this.order_status = order_status;
  }

  public String getOrder_id() {
    return order_id;
  }

  public void setOrder_id(String order_id) {
    this.order_id = order_id;
  }
}
