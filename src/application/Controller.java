package application;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
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
        /*
        ** Keep it in mind: If you want the observable list to invoke event when any
        ** attribute of the element object is change, use this.
        */
        constraintObservableList = FXCollections.observableArrayList(
                constraintObservableList -> new Observable[]{constraintObservableList.usageProperty()}
        );

        constraintObservableList.addAll(
//                new Constraint("Facebook",10,60,""),
//                new Constraint("VLC",20,60,""),
//                new Constraint("Youtube",30,60,""),
//                new Constraint("Music",50,60,""),
//                new Constraint("Stackoverflow",35,60,""),
//                new Constraint("Idea",5,60,""),
//                new Constraint("Chutiyap",60,60,"")
        );

        constraintListView.setItems(constraintObservableList);
        constraintListView.setCellFactory(constraintListView -> new ConstraintListViewCell());

        //setting table data
        logTableObservableList = FXCollections.observableArrayList();
        //fill some sample data
        logTableObservableList.add(new Table("now","sam","Intellij"));
        logTableObservableList.add(new Table("now","sam","Intellij"));
        logTableObservableList.add(new Table("now","sam","Intellij"));
        logTableObservableList.add(new Table("now","sam","Intellij"));
        logTableObservableList.add(new Table("now","sam","Intellij"));


        //JFXTreeTable specific things
        initializeTreeTableColumns();
        TreeItem<Table> root = new RecursiveTreeItem<>(logTableObservableList, RecursiveTreeObject::getChildren);
        logTableView.getColumns().setAll(startTimeColumn, applicationColumn, titleColumn);
        logTableView.setRoot(root);
        logTableView.setShowRoot(false);

        Main.setController(this);

    }

    //to initialize data source of columns in log tree table
    public void initializeTreeTableColumns(){
        startTimeColumn = new JFXTreeTableColumn<>("Start Time");
        startTimeColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Table, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Table, String> param) {
                return param.getValue().getValue().startTimeProperty();
            }
        });
        titleColumn = new JFXTreeTableColumn<>("Title");
        titleColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Table, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Table, String> param) {
                return param.getValue().getValue().titleProperty();
            }
        });
        applicationColumn = new JFXTreeTableColumn<>("Application");
        applicationColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Table, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Table, String> param) {
                return param.getValue().getValue().applicationProperty();
            }
        });

    }

    // Method to get instance of PieChart.Data from application name
    private PieChart.Data getPieDataObject(final String name){
        // if there is no entry for this application then add one
        if(!pieChartData.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent())
            pieChartData.add(new PieChart.Data(name, 0));
        return pieChartData.stream().filter(o -> o.getName().equals(name)).findFirst().get();
    }

    // Get constraint object from title string
    private Constraint getConstraintObject(final String title){
        if(constraintObservableList.stream().filter(o -> o.getTitle().equals(title)).findFirst().isPresent())
            return constraintObservableList.stream().filter(o -> o.getTitle().equals(title)).findFirst().get();
        else
            return null;
    }

    @FXML
    void addButtonPressed(ActionEvent event) throws SQLException {
        // Open new window
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../resources/fxml/AddNewConstraint.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Unable to load fxml file");
            e.printStackTrace();
        }
        AddNewConstraintController controller=fxmlLoader.<AddNewConstraintController>getController();
        Stage stage = new Stage();
        stage.setTitle("Add constraint - SAM");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        if(controller.constraint!=null) {
            // If user has clicked add button this bock will run
            System.out.println("Constraint added");
            System.out.println(controller.constraint.getTitle());
            System.out.println(controller.constraint.getLimit());
            System.out.println(controller.constraint.getTags());
            constraintObservableList.add(controller.constraint);
            database.addconstraint(controller.constraint);
        }
    }

    @FXML
    private void removeButtonPressed(ActionEvent event) {
        Constraint temp = constraintListView.getSelectionModel().getSelectedItem(); // Just for testing
        if(temp!=null)
            temp.setUsage(temp.getUsage()+5);
    }

    // This method get info about usage from Database class
    void updateInfo(String startTime,String title, String application, int duration){
        //if(getPieDataObject(application)!=null)
        Platform.runLater(()-> getPieDataObject(application).setPieValue(getPieDataObject(application).getPieValue()+duration));
        if(getConstraintObject(application)!=null)
            getConstraintObject(application).setUsage(duration);
        updateLogsTable(startTime,title,application);
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
        Platform.runLater(()-> {
            try {
                constraintObservableList.addAll(database.sendConstraints());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateLogsTable(String startTime, String title, String application){
        Table element  = new Table(startTime, title, application);
        if(logTableObservableList.size()<5){
            logTableObservableList.add(0, element);
        }else{
            logTableObservableList.remove(4);
            logTableObservableList.add(0, element);
        }
    }

    //  Add/Week/Month button action
    @FXML
    private void IntervalButtonPressed(ActionEvent event) {
        // Button background colors09
        String selected = "-fx-background-color: #71b6f2";
        String unselected = "-fx-background-color: #b1b5bc";;

        dayButton.setStyle(unselected);
        monthButton.setStyle(unselected);
        weekButton.setStyle(unselected);
        if(event.getSource().equals(dayButton)) {
            dayButton.setStyle(selected);
            setInitialData(1);
        }
        if(event.getSource().equals(monthButton)) {
            monthButton.setStyle(selected);
            setInitialData(30);
        }
        if(event.getSource().equals(weekButton)) {
            weekButton.setStyle(selected);
            setInitialData(7);
        }
    }

}
