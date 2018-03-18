package application;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Time;

public class ConstraintUpdateThread implements Runnable {

    private Controller controller;
    private ObservableList<Constraint> constraintObservableList;
    private String title;
    private String application;
    int time=0;
    int close=0;
    public ConstraintUpdateThread(Controller controller, ObservableList<Constraint> constraintObservableList, String title, String application){
        this.controller = controller;
        this.constraintObservableList = constraintObservableList;
        this.title=title;
        this.application = application;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(60000);
                check();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (close == 1)
                return;
        }
    }

    public void check() throws IOException {
        for(Constraint c : constraintObservableList){
            String tags[] = c.getTags().split(":");
            for(String tag : tags){
                if(title.toLowerCase().contains(tag.toLowerCase())){
                    if(c.getLimit()<=c.getUsage()+time){
                        //close the application
                        if(title.split(" - ")[1].equalsIgnoreCase(application)) {
                            String scriptName;
                            if(application.equalsIgnoreCase("google chrome") || application.equalsIgnoreCase("firefox"))
                                scriptName = "close_browser_tab.sh";
                            else
                                scriptName = "close_active_application.sh";

                            String[] command = {"src/scripts/"+scriptName};
                            Process process = Runtime.getRuntime().exec(command);
                            close = 1;
                        }
                    }
                    controller.updateConstraintProgress(c, c.getUsage()+time);
                }
            }
        }
    }
}
