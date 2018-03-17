package application;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class Table extends RecursiveTreeObject<Table> {
    private SimpleStringProperty startTime;
    private SimpleStringProperty title;
    private SimpleStringProperty application;

    public Table(String startTime, String title, String application) {
        this.startTime = new SimpleStringProperty(startTime);
        this.title = new SimpleStringProperty(title);
        this.application = new SimpleStringProperty(application);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public SimpleStringProperty startTimeProperty() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getApplication() {
        return application.get();
    }

    public SimpleStringProperty applicationProperty() {
        return application;
    }

    public void setApplication(String application) {
        this.application.set(application);
    }
}
