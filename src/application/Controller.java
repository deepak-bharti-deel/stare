package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TreeItem;

public class Controller {

//    @FXML
//    private JFXListView<Constraint> constraintListView;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton removeButton;

//    @FXML
//    private JFXTreeTableView<Table> logTableView;

    @FXML
    private PieChart pieChart;

    @FXML
    private JFXButton dayButton;

    @FXML
    private JFXButton weekButton;

    @FXML
    private JFXButton monthButton;

    @FXML
    private Database database;

    private ObservableList<PieChart.Data> pieChartData;

    @FXML
    public void initialize(){
        pieChartData = FXCollections.observableArrayList();
        pieChart.setData(pieChartData);
        pieChart.setTitle("Application Usage");
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(0);
        pieChart.setLegendSide(Side.LEFT);

    }

    private PieChart.Data getPieDataObject(final String name){
        // if there is no entry for this application then add one
        if(!pieChartData.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent())
            pieChartData.add(new PieChart.Data(name, 0));
        return pieChartData.stream().filter(o -> o.getName().equals(name)).findFirst().get();
    }

}
