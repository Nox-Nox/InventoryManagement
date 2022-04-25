package InventoryManagement.Controllers;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import InventoryManagement.DBConnect;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddController implements Initializable {

    @FXML
    private AnchorPane addProduct;

    @FXML
    private TextField barcodeField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField productField;

    @FXML
    private TextField quantityField;

    String barcode;
    String productName;
    int quantity;
    float price;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> barcodeField.requestFocus());

    }

    public void Submit() throws ClassNotFoundException, SQLException {
        barcode = barcodeField.getText();
        productName = productField.getText();
        quantity = Integer.parseInt(quantityField.getText());
        price = Float.parseFloat(priceField.getText());

        DBConnect co = new DBConnect();
        String query = "INSERT INTO product (barcode, productname, quantity, price) " + "VALUES(?, ?, ?, ?)";
        PreparedStatement prep = co.connectToDB().prepareStatement(query);
        prep.setString(1, barcode);
        prep.setString(2, productName);
        prep.setInt(3, quantity);
        prep.setFloat(4, price);
        prep.execute();
        co.connectToDB().close();
        barcodeField.clear();
        productField.clear();
        quantityField.clear();
        priceField.clear();
        
    }
    

}
