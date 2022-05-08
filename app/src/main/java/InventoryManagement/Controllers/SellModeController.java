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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

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

        String search;
        String code1 = "";
        String code = "";
        StringBuffer lol = new StringBuffer();

        @Override
        public void initialize(URL location, ResourceBundle resources) {

                sellModeWindow.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

                        @Override
                        public void handle(KeyEvent event) {
                                if (!event.getCode().equals(KeyCode.ENTER)) {
                                        // CODE HERE WHEN USER CLICK ENTER OR SOMETHIN LIKE THAT

                                        code = event.getText();
                                        code1 = code1.concat(code);
                                        System.out.println(code);
                                } else {
                                        // DETECT KEYCODE OTHER THAN ENTER TO GET THEM
                                        System.out.println(code1);
                                        try {
                                                getProductByBarcode(code1);
                                        } catch (ClassNotFoundException | SQLException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                        }
                                        code1 = "";

                                        // STORE kc SOMEHWERE AND USE IT

                                }
                                event.consume(); // USED TO DENY TO NOT ALLOW TEXTFIELD TO HEAR THE KEYPRESSED

                        }
                });
                System.out.println(code1);

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

        private void getProductByBarcode(String code)
                        throws ClassNotFoundException, SQLException {

                System.out.println(code1);
                DBConnect co = new DBConnect();
                String query = "SELECT* FROM product WHERE barcode=?";
                PreparedStatement prep = co.connectToDB().prepareStatement(query);
                prep.setString(1, code);
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
