package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private Connection conn;
    private int prev_id;
    private Controller controller;

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

    public void setController(Controller controller){
        this.controller=controller;
    }

    public void addconstraint(String name,String keywords,String application,int time_limit,int is_enabled) throws SQLException {
        String query = "INSERT INTO ActivityConstraints values (?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1,name);
        stmt.setString(2,keywords);
        stmt.setString(3,application);
        stmt.setInt(4,time_limit);
        stmt.setInt(5,is_enabled);
        stmt.executeUpdate();
    }

}
