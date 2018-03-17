package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ActivityThread implements Runnable {

    private Database db;
    private Controller controller;
    public ActivityThread(Controller controller){
        this.controller = controller;
//        db = new Database(controller);
        db = Main.getDb();
    }


    @Override
    public void run() {

        try {
            String[] command = {"src/scripts/cont_detect.sh", };
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            String previous=null;
            while ((s = reader.readLine()) != null) {
//                System.out.println("Script output: " + s);
                if (s.split(" <--> ")[0].equals(previous)) {
                    // db.sendUpdates();
                    continue;
                }else {
//                    System.out.println("inserting to db");
//                    int result = db.insertLog(s);
//                    if (result == 1) {
//                        previous = s.split(" <--> ")[0];
//                        //System.out.println("previous=" + previous);
//                        System.out.println(s.split(" <--> ")[0]);
//                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
