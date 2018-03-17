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

    public void setDuration(Date curdate) throws SQLException {
        // Update Duration column of last added row

        String query = "SELECT day,month,year,hour,minute,second FROM logs WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, prev_id);
        ResultSet rs = stmt.executeQuery();

        int minutes = 0;
        while (rs.next()) {
            Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(rs.getString("year")),Integer.parseInt(rs.getString("month"))-1, Integer.parseInt(rs.getString("day")),Integer.parseInt(rs.getString("hour")), Integer.parseInt(rs.getString("minute")), Integer.parseInt(rs.getString("second")));
            Date prevdate = c.getTime();
            minutes = calculateDuration(curdate, prevdate);
        }

        query = "UPDATE logs SET Duration = ? WHERE id = ?";
        stmt = conn.prepareStatement(query);
        stmt.setInt(1, minutes);
        stmt.setInt(2, prev_id);
        stmt.executeUpdate();
    }


    public void addconstraint(Constraint constraint) throws SQLException {
        String query = "INSERT INTO ActivityConstraints values (?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(2,constraint.getTitle());
        stmt.setString(3,constraint.getTags());
        stmt.setString(1,constraint.getApplication());
        stmt.setInt(4,constraint.getLimit());
        stmt.setInt(5,1);
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

    public int insertLog(String op) throws SQLException {

        //op is output of the script
        String arr[] = op.split(" <--> "); //now contains title and date separately
        if (arr[0].isEmpty())
            return 0;
        String date[] = arr[1].split(" ");
        //date[0] -> year
        //date[1] -> month
        //date[2] -> day
        //date[3] -> time
        String time[] = date[3].split(":");
        //time[0] -> hour
        //time[1] -> minute
        //time[2] -> second
        String app[] = arr[0].split(" - ");
        String title = app[0];
        String application = app[app.length - 1];
        //if title and application is same => window inside a window or doing nothing on some software
        //don't insert such entries
        if (application.equals(arr[0]))
            return 0;
        String query = "INSERT INTO logs VALUES (null,?,?,?,?,?,?,?,?,null)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, title);//title
        stmt.setString(2, application);//application
        stmt.setInt(3, Integer.parseInt(date[2]));//day
        stmt.setInt(4, Integer.parseInt(date[1]));//month
        stmt.setInt(5, Integer.parseInt(date[0]));//year
        stmt.setInt(6, Integer.parseInt(time[0]));//hour
        stmt.setInt(7, Integer.parseInt(time[1]));//min
        stmt.setInt(8, Integer.parseInt(time[2]));//sec
        stmt.executeUpdate();

        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
        Date curdate = c.getTime();
        if(prev_id != -1) {
            setDuration(curdate);                   // Update Duration column of last added row
            //sendUpdates();                          // notify controller class for change in application
        }

        query = "SELECT MAX(id) AS id FROM logs";
        stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        if (rs.next())
            prev_id = rs.getInt("id");
        return 1;
    }


}
