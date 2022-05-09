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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class AppController implements Initializable {

    private ObservableList<Product> productListView = FXCollections.observableArrayList();
    private FilteredList<Product> filteredList = new FilteredList<>(productListView, b -> true);
    private SortedList<Product> productSortedList = new SortedList<>(filteredList);

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
    private TableColumn<Product, Integer> stockCol;

    @FXML
    private TextField searchProd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        barcodeCol.setCellValueFactory(new PropertyValueFactory<Product, String>("Barcode"));
        productCol.setCellValueFactory(new PropertyValueFactory<Product, String>("ProductName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Product, Float>("Price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Stock"));

        try {
            getProduct(productListView, productTable);
        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
        }

        searchProd.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(Product -> {
            if (newValue.isBlank() || newValue.isEmpty()) {
                return true;
            }
            String searchWord = newValue.toLowerCase();
            if (Product.getBarcode().toString().toLowerCase().contains(searchWord)) {
                return true;
            } else if (Product.getProductName().toString().toLowerCase().contains(searchWord)) {
                return true;
            } else if (String.valueOf(Product.getStock()).contains(searchWord)) {
                return true;
            } else
                return String.valueOf(Product.getPrice()).contains(searchWord);
        }));
        productSortedList.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(productSortedList);

        productTable.setEditable(true);
        stockCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        productCol.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public void refresh() throws ClassNotFoundException, SQLException {
        productListView.clear();
        productTable.getItems().clear();
        getProduct(productListView, productTable);
        productSortedList.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(productSortedList);
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

    public void sellMode() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/sellMode.fxml"));
        stage.setTitle("Sell mode");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene((new Scene(root)));
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
        prep.close();
        productListView.remove(index);
        productTable.refresh();

    }

    private void getProduct(ObservableList<Product> productList, TableView<Product> productTable)
            throws SQLException, ClassNotFoundException {
        DBConnect co = new DBConnect();
        String query = "SELECT* FROM PRODUCT";
        Statement stat = co.connectToDB().createStatement();
        ResultSet res = stat.executeQuery(query);
        int q = 1;
        while (res.next()) {
            productList.add(new Product(res.getString("barcode"), res.getString("productName"),
                    res.getInt("stock"), q, res.getFloat("price")));
        }
        productTable.setItems(productListView);
        stat.close();
    }

    public void editBarcode(TableColumn.CellEditEvent<Product, String> e) throws ClassNotFoundException, SQLException {
        String oldBarcode = productTable.getSelectionModel().getSelectedItem().getBarcode();
        String newBarcode = e.getNewValue().toString();
        DBConnect co = new DBConnect();
        String query = "UPDATE product SET barcode=? WHERE barcode=?";
        PreparedStatement prep = co.connectToDB().prepareStatement(query);
        prep.setString(1, newBarcode);
        prep.setString(2, oldBarcode);
        prep.execute();
        prep.close();

    }

    public void editProduct(TableColumn.CellEditEvent<Product, String> e) throws ClassNotFoundException, SQLException {
        String barcode = productTable.getSelectionModel().getSelectedItem().getBarcode();
        String newProduct = e.getNewValue().toString();
        DBConnect co = new DBConnect();
        String query = "UPDATE product SET productName=? WHERE barcode=?";
        PreparedStatement prep = co.connectToDB().prepareStatement(query);
        prep.setString(1, newProduct);
        prep.setString(2, barcode);
        prep.execute();
        prep.close();
    }

    public void editPrice(TableColumn.CellEditEvent<Product, Float> e) throws ClassNotFoundException, SQLException {
        String barcode = productTable.getSelectionModel().getSelectedItem().getBarcode();
        String newPrice = e.getNewValue().toString();
        DBConnect co = new DBConnect();
        String query = "UPDATE product SET price=? WHERE barcode=?";
        PreparedStatement prep = co.connectToDB().prepareStatement(query);
        prep.setString(1, newPrice);
        prep.setString(2, barcode);
        prep.execute();
        prep.close();
    }

    public void editStock(TableColumn.CellEditEvent<Product, Integer> e)
            throws ClassNotFoundException, SQLException {
        String oldBarcode = productTable.getSelectionModel().getSelectedItem().getBarcode();
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
