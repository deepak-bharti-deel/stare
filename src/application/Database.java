package application;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

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

    public Hashtable<String, Integer> fillPieChart(int numberOfDays) throws Exception{  // fill the pie chart initially

        numberOfDays *= (1 * 60 * 60);                                       // number of seconds in n days
        Hashtable<String, Integer> activities = new Hashtable<>();
        String query = "SELECT application,day,month,year,hour,minute,second,Duration  FROM logs ";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        java.util.Date currentDate = new java.util.Date();
        int temp = 0;
        while (rs.next()) {
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(rs.getString("year")),Integer.parseInt(rs.getString("month"))-1, Integer.parseInt(rs.getString("day")),Integer.parseInt(rs.getString("hour")), Integer.parseInt(rs.getString("minute")), Integer.parseInt(rs.getString("second")));
            Date activityDate = c.getTime();
            if (calculateDuration(currentDate, activityDate) <= numberOfDays) {
                if(activities.get(rs.getString("application")) == null)
                    temp = rs.getInt("Duration");
                else
                    temp = rs.getInt("Duration") + activities.get(rs.getString("application"));
            }
            activities.put(rs.getString("application"), temp);
        }
        System.out.println(activities);
        return activities;
    }
    private int calculateDuration(Date curdate, Date prevdate) {
        // calculate seconds between current time and previous time
        int  minutes = Math.toIntExact((curdate.getTime() - prevdate.getTime()) / 1000);
        return minutes;
    }

}
