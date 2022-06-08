package InventoryManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    public Connection conn;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String connArg = "jdbc:h2:/home/tahmid/Documents/Projects/InventoryManagement/app/src/main/H2DB/H2;SCHEMA=ITEMS";
        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection(connArg, "root", "pass");
        return conn;
    }

    public Connection connectToDB() throws ClassNotFoundException, SQLException {
        DBConnect dbconnect = new DBConnect();
        return dbconnect.getConnection();
    }

}
