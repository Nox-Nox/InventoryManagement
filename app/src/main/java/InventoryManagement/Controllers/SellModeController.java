package InventoryManagement.Controllers;

import InventoryManagement.DBConnect;
import InventoryManagement.Product;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class SellModeController implements Initializable {
        private ObservableList<Product> productListView = FXCollections.observableArrayList();

        @FXML
        private AnchorPane sellModeWindow;

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

        @FXML
        private Label totalID;

        String search;
        String formattedCode = "";
        String code = "";
        DBConnect co = new DBConnect();

        @Override
        public void initialize(URL location, ResourceBundle resources) {

                sellModeWindow.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

                        @Override
                        public void handle(KeyEvent event) {
                                if (!event.getCode().equals(KeyCode.ENTER)) {
                                        code = event.getText();
                                        formattedCode = formattedCode.concat(code);
                                } else {
                                        try {
                                                getProductByBarcode(formattedCode);
                                                Total(productListView);
                                        } catch (ClassNotFoundException | SQLException e) {

                                                e.printStackTrace();
                                        }
                                        formattedCode = "";
                                }
                                event.consume();
                        }
                });

                barcodeCol.setCellValueFactory(
                                new PropertyValueFactory<Product, String>("Barcode"));
                productCol.setCellValueFactory(
                                new PropertyValueFactory<Product, String>("ProductName"));
                priceCol.setCellValueFactory(
                                new PropertyValueFactory<Product, Float>("Price"));
                quantityCol.setCellValueFactory(
                                new PropertyValueFactory<Product, Integer>("Quantity"));
                stockCol.setCellValueFactory(
                                new PropertyValueFactory<Product, Integer>("Stock"));

                productTable.setEditable(true);
                quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
                priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        }

        private void getProductByBarcode(String code)
                        throws ClassNotFoundException, SQLException {

                System.out.println(formattedCode);
                String query = "SELECT* FROM product WHERE barcode=?";
                PreparedStatement prep = co.connectToDB().prepareStatement(query);
                prep.setString(1, code);
                ResultSet res = prep.executeQuery();
                int q = 1;
                if (res.next()) {
                        productListView.add(
                                        new Product(
                                                        res.getString("barcode"),
                                                        res.getString("productName"),
                                                        res.getInt("stock"),
                                                        q,
                                                        res.getFloat("price")));
                }
                productTable.setItems(productListView);
                prep.close();
        }

        private void Total(ObservableList<Product> product) {
                float tot = 0;
                for (Product p : product) {
                        int q = p.getQuantity();
                        float pr = p.getPrice();
                        tot += (pr * q);
                }
                totalID.setText("Total: " + tot);
        }

        public void editPrice(TableColumn.CellEditEvent<Product, Float> e)
                        throws ClassNotFoundException, SQLException {
                float newPrice = e.getNewValue();
                Product p = productTable.getSelectionModel().getSelectedItem();
                p.setPrice(newPrice);
                int index = productTable.getSelectionModel().getSelectedIndex();
                productListView.set(index, p);
                productTable.refresh();
        }

        public void editQuantity(TableColumn.CellEditEvent<Product, Integer> e)
                        throws ClassNotFoundException, SQLException {
                int newQuantity = e.getNewValue();
                Product p = productTable.getSelectionModel().getSelectedItem();
                p.setQuantity(newQuantity);
                int index = productTable.getSelectionModel().getSelectedIndex();
                productListView.set(index, p);
                productTable.refresh();
        }

        public void removeItem() throws ClassNotFoundException, SQLException {
                int index = productTable.getSelectionModel().getSelectedIndex();
                productListView.remove(index);
                Total(productListView);
                productTable.refresh();
        }

        public void Submit() throws SQLException, ClassNotFoundException {
                String barcode;
                int oldStock;
                int quantity;
                int updatedStock;
                for (Product p : productListView) {
                        barcode = p.getBarcode();
                        oldStock = p.getStock();
                        quantity = p.getQuantity();
                        updatedStock = oldStock - quantity;
                        if (updatedStock < 0) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Warning");
                                alert.setHeaderText("Not enough in stock");
                                alert.setContentText("Change quantity please, we don't have that much in the stock");
                                alert.showAndWait().ifPresent(rs -> {
                                        if (rs == ButtonType.OK) {
                                                System.out.println("Pressed OK.");
                                        }
                                });
                        } else {
                                String query = "UPDATE product SET stock = ? WHERE barcode = ?";
                                PreparedStatement prep = co.connectToDB().prepareStatement(query);
                                prep.setInt(1, updatedStock);
                                prep.setString(2, barcode);
                                prep.execute();
                                prep.close();
                        }

                }
        }
}
