package application;

import com.jfoenix.controls.JFXListCell;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;


public class ConstraintListViewCell extends ListCell<Constraint> {

    @FXML
    private Label title;

    @FXML
    private JFXProgressBar progressBar;

    @FXML
    private Label percentageLabel;

    @FXML
    private FXMLLoader loader;

    @FXML
    private JFXListCell<?> body;

    @FXML
    private Label usageLabel;

    @Override
    protected void updateItem(Constraint constraint,boolean empty){
        super.updateItem(constraint, empty);
        if(empty || constraint == null) {   //If empty, skip it
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {           //set message body FXML
                loader = new FXMLLoader(getClass().getResource("../resources/fxml/ConstraintCell.fxml"));
                loader.setController(this); //and set this class as it's controller
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            // Code for cell
            IntegerProperty x = constraint.usageProperty();
            Double progress = ((double)x.getValue())/constraint.getLimit();
            //Double progress = ((double)(constraint.getUsage()))/constraint.getLimit();
            title.setText(constraint.getTitle());
            usageLabel.setText(getDurationString(constraint));
            progressBar.setProgress(progress);
            percentageLabel.setText(Integer.toString((int)(progress*100)));

            setText(null);
            setGraphic(body);
        }

    }

    private String getDurationString(Constraint constraint){
        return Integer.toString(constraint.getUsage())+"/"+Integer.toString(constraint.getLimit());
    }

}
