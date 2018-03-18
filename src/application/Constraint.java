package application;

import javafx.beans.property.*;
import java.util.ArrayList;
import java.util.List;

public class Constraint {
    private StringProperty title = new SimpleStringProperty();
    private StringProperty application = new SimpleStringProperty();

    public String getApplication() {
        return application.get();
    }

    public StringProperty applicationProperty() {
        return application;
    }

    public void setApplication(String application) {
        this.application.set(application);
    }

    public String getTags() {
        return tags.get();
    }

    public StringProperty tagsProperty() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags.set(tags);
    }

    private StringProperty tags = new SimpleStringProperty();
    private IntegerProperty limit = new SimpleIntegerProperty();    // in minutes
    private IntegerProperty usage = new SimpleIntegerProperty();    // amount of time completed
    public static List<Constraint> constraintList = new ArrayList<>();    // Contains current constarints
    private BooleanProperty isEnabled = new SimpleBooleanProperty();// is constraint active

    public Constraint(String title,String application,int usage,int limit,String tags){
        this.title.set(title);
        this.application.set(application);
        this.usage.set(usage);
        this.limit.set(limit);
        this.tags.set(tags);
    }

    public boolean isEnabled() {
        return isEnabled.get();
    }

    public BooleanProperty isEnabledProperty() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled.set(isEnabled);
    }

    public List<Constraint> getConstraintList() {
        return constraintList;
    }

    public void setConstraintList(List<Constraint> constraintList) {
        this.constraintList = constraintList;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getLimit() {
        return limit.get();
    }

    public IntegerProperty limitProperty() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit.set(limit);
    }

    public int getUsage() {
        return usage.get();
    }

    public IntegerProperty usageProperty() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage.set(usage);
    }
}
