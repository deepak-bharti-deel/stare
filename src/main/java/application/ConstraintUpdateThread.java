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

        try {
            check(1);
            if(close==1)
                return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                Thread.sleep(2000);
                time++;
                check(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (close == 1)
                return;
        }
    }

    public void check(int open) throws IOException {
        back:
        for(Constraint c : constraintObservableList){
            System.out.println("Name "+c.getTitle());
            System.out.println("Tags "+c.getTags());
            System.out.println("Limit "+c.getLimit());
            System.out.println("Application "+c.getApplication());
            System.out.println("current application: "+application);
            System.out.println("time: "+time);
            String tags[] = c.getTags().split(":");
            for(String tag : tags){
                if(title.toLowerCase().contains(tag.toLowerCase())){
                    if(c.getLimit()<=c.getUsage()+1){
                        System.out.println("limit "+c.getLimit());
                        System.out.println("usage "+c.getUsage());
                        //close the application
                        if(c.getApplication().equalsIgnoreCase(application)) {
                            String scriptName;
                            if(application.equalsIgnoreCase("google chrome") || application.equalsIgnoreCase("firefox"))
                                scriptName = "close_browser_tab.sh";
                            else
                                scriptName = "close_active_application.sh";

                            String[] command = {"src/scripts/"+scriptName};
                            Process process = Runtime.getRuntime().exec(command);
                            close = 1;
                            if(open!=1)
                                controller.updateConstraintProgress(c, c.getUsage()+1,application);
                            break back;
                        }
                    }
                    System.out.println("updating progress");
                    if(open!=1)
                        controller.updateConstraintProgress(c, c.getUsage()+1,application);
                }
            }
        }
    }
}
