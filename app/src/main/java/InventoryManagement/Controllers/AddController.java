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
    private TextField stockField;

    String barcode;
    String productName;
    int stock;
    float price;
    DBConnect co = new DBConnect();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> barcodeField.requestFocus());

    }

    public void Submit() throws ClassNotFoundException, SQLException {
        barcode = barcodeField.getText();
        productName = productField.getText();
        stock = Integer.parseInt(stockField.getText());
        price = Float.parseFloat(priceField.getText());
        String query = "INSERT INTO product (barcode, productname, stock, price) " + "VALUES(?, ?, ?, ?)";
        PreparedStatement prep = co.connectToDB().prepareStatement(query);
        prep.setString(1, barcode);
        prep.setString(2, productName);
        prep.setInt(3, stock);
        prep.setFloat(4, price);
        prep.execute();
        co.connectToDB().close();
        barcodeField.clear();
        productField.clear();
        stockField.clear();
        priceField.clear();
        Platform.runLater(() -> barcodeField.requestFocus());

    }

}
