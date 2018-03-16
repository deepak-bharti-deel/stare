package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private Connection conn;
    private int prev_id;

    public Database() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sam", "root", "");
            System.out.println("Connection to the database has been established!!");
            prev_id = -1;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("problem in connection with the database");
        }
    }

}
