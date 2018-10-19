package application;

import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class AddNewConstraintController {


    @FXML
    private JFXTextField constraintNameTextField;

    @FXML
    private JFXTextField applicationTextField;

    @FXML
    private JFXToggleButton blockToggleButton;

    @FXML
    private JFXSlider setLimitSlider;

    @FXML
    private JFXChipView<String> keywordChipView;

    public Constraint constraint;

    public void initialize(){
        keywordChipView.getChips().addAll("Hi","Hello","Whatsup");
    }

    @FXML
    private void addPressed(ActionEvent event) {
        String tags = String.join(":",keywordChipView.getChips());
        int limit = (int)setLimitSlider.getValue();
        if(blockToggleButton.isSelected())
            limit=0;
        constraint = new Constraint(
                constraintNameTextField.getText(),
                applicationTextField.getText(),
                0,
                limit,
                tags);
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void blockToggleAction(ActionEvent event) {
        if(!blockToggleButton.isSelected())
            setLimitSlider.setDisable(false);
        else
            setLimitSlider.setDisable(true);
    }
}
