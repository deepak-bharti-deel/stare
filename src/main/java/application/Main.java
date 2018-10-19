package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class Main extends Application {

    private static Database db;
    private static Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{

        db = new Database();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Unable to load fxml file");
            e.printStackTrace();
        }
        controller=fxmlLoader.<Controller>getController();
        controller.setDatabase(db);
        db.setController(controller);

        primaryStage.setTitle("STARE - System Tracking & Activity Restrictions");
        primaryStage.setScene(new Scene(root, 1000, 700));

        primaryStage.setOnCloseRequest(event -> {
            Calendar c = Calendar.getInstance();
            Date currentDate = c.getTime();
            try {
                db.setDuration(currentDate);           //update last activity in dtatbase
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        primaryStage.show();    // now we are on Inteliij , so when a switch in activity occur the graph changes
    }

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));
        launch(args);
    }

    public static Database getDb() {
        return db;
    }

    public static void setController(Controller mController){
        controller=mController;
        ActivityThread activityThread = new ActivityThread(controller);
        Thread t = new Thread(activityThread);
        t.start();
    }

}
