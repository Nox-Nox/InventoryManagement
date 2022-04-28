package InventoryManagement;

public class Product {
  String barcode;
  String productName;
  Integer quantity;
  Float price;

  public Product(
    String barcode,
    String productName,
    Integer quantity,
    Float price
  ) {
    this.barcode = barcode;
    this.productName = productName;
    this.quantity = quantity;
    this.price = price;
  }

  public Integer getQuantity1() {
    return 1;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return (
      "{" + barcode + "=" + productName + " " + quantity + " " + price + "}"
    );
  }
}
