package InventoryManagement.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import InventoryManagement.DBConnect;
import InventoryManagement.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AppController implements Initializable {

    private ObservableList<Product> productListView = FXCollections.observableArrayList();
    private final FilteredList<Product> filteredList = new FilteredList<>(productListView, b -> true);
    private final SortedList<Product> productSortedList = new SortedList<>(filteredList);

    @FXML
    private Button addProd;

    @FXML
    private Button deleteProd;

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

        searchProd.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(Product -> {
            if (newValue.isBlank() || newValue.isEmpty()) {
                return true;
            }
            String searchWord = newValue.toLowerCase();
            if (Product.getBarcode().toLowerCase().contains(searchWord)) {
                return true;
            } else if (Product.getProductName().toLowerCase().contains(searchWord)) {
                return true;
            } else if (String.valueOf(Product.getQuantity()).contains(searchWord)) {
                return true;
            } else
                return String.valueOf(Product.getPrice()).contains(searchWord);
        }));
        productSortedList.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(productSortedList);

    }

    public void refresh() throws ClassNotFoundException, SQLException {
        productListView.clear();
        getProduct();
        productTable.refresh();
    }

    public void addItem() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/add.fxml"));
        stage.setTitle("Add item");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void deleteItem() throws ClassNotFoundException, SQLException {
        String barcode = productTable.getSelectionModel().getSelectedItem().getBarcode();
        int index = productTable.getSelectionModel().getSelectedIndex();
        String query = "DELETE FROM product WHERE barcode = ?";
        DBConnect co = new DBConnect();
        PreparedStatement prep = co.connectToDB().prepareStatement(query);
        prep.setString(1, barcode);
        prep.execute();
        productListView.remove(index);
        productTable.refresh();

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
