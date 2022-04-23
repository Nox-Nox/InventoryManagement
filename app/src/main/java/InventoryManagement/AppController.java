package InventoryManagement;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AppController implements Initializable {

    private ObservableList<Product> productListView = FXCollections.observableArrayList();

    @FXML
    private Button addProd;

    @FXML
    private Button deleteProd;

    @FXML
    private Button editProd;

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
    private TextField searchProd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        barcodeCol.setCellValueFactory(new PropertyValueFactory<>("Barcode"));
        productCol.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

        try {
            getProduct();
        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
        }
    }

    private void getProduct() throws SQLException, ClassNotFoundException {
        DBConnect co = new DBConnect();
        String query = "SELECT* FROM PRODUCT";
        Statement stat = co.connectToDB().createStatement();
        ResultSet res = stat.executeQuery(query);
        while (res.next()) {
            productListView.add(new Product(res.getString("barcode"), res.getString("productName"),
                    res.getInt("quantity"), res.getFloat("price")));
        }
        productTable.setItems(productListView);
    }

}
