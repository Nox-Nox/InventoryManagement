package InventoryManagement;

public class Product {
  String barcode;
  String productName;
  int stock;
  float price;
  int quantity;

  public Product(
      String barcode,
      String productName,
      int stock,
      int quantity,
      float price) {
    this.barcode = barcode;
    this.productName = productName;
    this.stock = stock;
    this.quantity = quantity;
    this.price = price;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
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

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return ("{" + barcode + "=" + productName + " " + stock + " " + price + "}");
  }
}
