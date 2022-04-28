package InventoryManagement.Controllers;

import InventoryManagement.DBConnect;
import InventoryManagement.Product;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SellModeController implements Initializable {
    private ObservableList<Product> productListView = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> barcodeCol;

    @FXML
    private TableColumn<Product, Float> priceCol;

    @FXML
    private TableColumn<Product, String> productCol;

    @FXML
    private TableColumn<Product, Integer> quantityCol;

    @FXML
    private TableColumn<Product, Integer> stockCol;

    @FXML
    private TextField searchID;

    String search;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> searchID.requestFocus());

        barcodeCol.setCellValueFactory(
                new PropertyValueFactory<Product, String>("Barcode"));
        productCol.setCellValueFactory(
                new PropertyValueFactory<Product, String>("ProductName"));
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Product, Float>("Price"));
        quantityCol.setCellValueFactory(
                new PropertyValueFactory<Product, Integer>("Quantity1"));
        stockCol.setCellValueFactory(
                new PropertyValueFactory<Product, Integer>("Quantity"));
    }

    public void getProductByBarcode()
            throws ClassNotFoundException, SQLException {
        search = searchID.getText();
        System.out.println(search);
        DBConnect co = new DBConnect();
        String query = "SELECT* FROM product WHERE barcode=?";
        PreparedStatement prep = co.connectToDB().prepareStatement(query);
        prep.setString(1, search);
        ResultSet res = prep.executeQuery();
        if (res.next()) {
            productListView.add(
                    new Product(
                            res.getString("barcode"),
                            res.getString("productName"),
                            res.getInt("quantity"),
                            res.getFloat("price")));
        }
        productTable.setItems(productListView);
        prep.close();
    }

    public void editPrice(TableColumn.CellEditEvent<Product, Float> e)
            throws ClassNotFoundException, SQLException {
        String barcode = productTable
                .getSelectionModel()
                .getSelectedItem()
                .getBarcode();
        String newPrice = e.getNewValue().toString();
        DBConnect co = new DBConnect();
        String query = "UPDATE product SET price=? WHERE barcode=?";
        PreparedStatement prep = co.connectToDB().prepareStatement(query);
        prep.setString(1, newPrice);
        prep.setString(2, barcode);
        prep.execute();
        prep.close();
    }

    public void editQuantity(TableColumn.CellEditEvent<Product, Integer> e)
            throws ClassNotFoundException, SQLException {
        String oldBarcode = productTable
                .getSelectionModel()
                .getSelectedItem()
                .getBarcode();
        String newBarcode = e.getNewValue().toString();
        DBConnect co = new DBConnect();
        String query = "UPDATE product SET quantity=? WHERE barcode=?";
        PreparedStatement prep = co.connectToDB().prepareStatement(query);
        prep.setString(1, newBarcode);
        prep.setString(2, oldBarcode);
        prep.execute();
        prep.close();
    }
}
