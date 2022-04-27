package InventoryManagement.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import InventoryManagement.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SellModeController implements Initializable {
    public SellModeController() throws IOException {
        super();
    }

    private ObservableList<Product> productListView = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> itemsTable;

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

    FXMLLoader loader = new FXMLLoader(SellModeController.class.getResource("/main.fxml"));
    Parent sceneFXML = loader.load();
    // getting controller object
    AppController ctrl = loader.getController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
