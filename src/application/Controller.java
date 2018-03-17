package application;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TreeItem;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Set;

public class Controller {

    @FXML
    private JFXListView<Constraint> constraintListView;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton removeButton;

    @FXML
    private JFXTreeTableView<Table> logTableView;

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

    // Dynamically added nodes
    private JFXTreeTableColumn<Table, String> startTimeColumn;
    private JFXTreeTableColumn<Table, String> titleColumn;
    private JFXTreeTableColumn<Table, String> applicationColumn;

    // Observable lists
    private ObservableList<Constraint>  constraintObservableList;
    private ObservableList<PieChart.Data> pieChartData;
    private ObservableList<Table> logTableObservableList;

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

    public void setDatabase(Database database) {
        this.database = database;
        setInitialData(1);
    }

    // Sets initial data in pie chart and constraint
    private void setInitialData(int days){
        // Get pie chart data
        try {
            Hashtable<String,Integer> initialdata = database.fillPieChart(days);
            Set<String> keys = initialdata.keySet();
            for(String key: keys)
                Platform.runLater(()-> getPieDataObject(key).setPieValue(initialdata.get(key)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Constraint data
//        Platform.runLater(()-> {
//            try {
//                constraintObservableList.addAll(database.sendConstraints());
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        });
    }



}
