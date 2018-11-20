package sample;

import dataframe.*;
import javafx.scene.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class Main extends Application {
    static int dfindex=0;
    Stage stage;
    enum operation{MAX,MIN,STD,SUM,VAR};
    TextArea fileInfo = new TextArea();
    FileChooser fileChooser = new FileChooser();
    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Select CSV file");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"),"/csv")
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );

    }
    HashMap<String, Object> hashMap = new HashMap<>();
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        System.out.println(controller);
        controller.setStage(this.stage);
        primaryStage.setTitle("DataFrame Graphic Environment");
        configureFileChooser(fileChooser);


        Button button = new Button("Open CSV");
        Button countMin = new Button("Count Min");
        Button countMax = new Button("Count Max");
        Button countSum = new Button("Count Sum");
        Button countVar = new Button("Count Var");
        Button countStd = new Button("Count Std");
        Button plot = new Button("Plot");
        Group group = new Group();
        Label label = new Label();

//        FileChooser fileChooser = new FileChooser();
//        GridPane layout = new GridPane();
//        layout.getChildren().add(button);
//        layout.getChildren().addAll(button/*,countMin,countMax,countSum,countVar,countStd);

        TextArea col1 = new TextArea();
        col1.setMaxWidth(10);
        col1.setMaxHeight(4);
        col1.setPrefHeight(4);
        TextArea col2 = new TextArea();
        col2.setMaxWidth(10);
        col2.setMaxHeight(4);
        TextArea col3 = new TextArea();
        col3.setMaxWidth(7);
        col3.setMaxHeight(7);
        AtomicInteger a = new AtomicInteger(-1);
        AtomicInteger b = new AtomicInteger(-1);
        AtomicInteger c = new AtomicInteger(2);

//        Scene scene = new Scene(layout, 800, 600);*/
        GridPane layout = new GridPane();
        /*Pane firstRow = new Pane();
        GridPane row = new GridPane();
*/
       // Button  button = new Button("A");

        layout.add(button,1,1);
        layout.add(fileInfo,2,1,2,1);
        fileInfo.setText("Loaded file: -----------");
        fileInfo.setMaxHeight(7);
        fileInfo.setPrefWidth(400);
        layout.add(new TextField("first column"),2,2);
        layout.add(new TextField("second column"), 2, 3);
        layout.add(col1,3,2);
        layout.add(col2,3,3);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.file = fileChooser.showOpenDialog(primaryStage);
                if (controller.file != null) {
                    try {
                        controller.dataFrame = new DataFrame(controller.file.getPath(),new Class[]{ValString.class, ValDateTime.class, ValDouble.class, ValDouble.class});
                        hashMap.put("dataframe",controller.dataFrame);

                    }
                    catch (Exception e) {
                        errorDisplayGroupby(e, "An error occured while reading file");
                    }

                    //System.out.println(controller.dataFrame.getRecord(0));
                if (controller.dataFrame.getRecord(0)[0]!=null){
                    fileInfo.setText("Loaded file: "+controller.file.getName());



                    layout.add(countMin,1,2);
                    layout.add(countMax,1,3);
                    layout.add(countSum,1,4);
                    layout.add(countStd,1,5);
                    layout.add(countVar,1,6);
                    layout.add(plot,1,7);
                }
            }
        }});

        countMin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                layout.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 2 && GridPane.getRowIndex(node)>3);

                if (col1.getText().length() > 0 && col2.getText().length() > 0) {
                    /*String concat = col1.getText()+col2.getText();
                    try {
                        if (!hashMap.containsKey("groupby"+concat)) {
                            hashMap.put("groupby"+concat, controller.dataFrame.groupby(col1.getText(),col2.getText()));
                        }
                        if (!hashMap.containsKey("min"+col1.getText()+col2.getText())) {
                            hashMap.put("min"+concat, ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concat)).min());
                        }
                        for (int i = 0; i < controller.dataFrame.getColumns().length; ++i) {
                            TextField value = new TextField(controller.dataFrame.getColumns()[i] + ": " + ((DataFrame) hashMap.get("min"+concat)).getRecord(0)[i].toString());
//                        value.setLayoutX(100);
                            layout.add(value, 2, i + 4);
                        }
                    } catch (Exception e) {
                        errorDisplayGroupby(e, "An error occured while grouping dataframe");
                    }
                }*/
                } else if (col1.getText().length() > 0) {
                } else if (col2.getText().length() > 0) {
                } else {
                    try {
                        if (!hashMap.containsKey("groupby")) {
                            hashMap.put("groupby", controller.dataFrame.groupby());
                        }
                        if (!hashMap.containsKey("min")) {
                            hashMap.put("min", ((DataFrame.GroupByDataFrame) hashMap.get("groupby")).min());
                        }
                        for (int i = 0; i < controller.dataFrame.getColumns().length; ++i) {
                            TextField value = new TextField(controller.dataFrame.getColumns()[i] + ": " + ((DataFrame) hashMap.get("min")).getRecord(0)[i].toString());
//                        value.setLayoutX(100);
                            layout.add(value, 2, i + 4);
                        }
                    } catch (Exception e) {
                        errorDisplayGroupby(e, "An error occured while grouping dataframe");
                    }
                }
            }
        });

        countMax.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                layout.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 2 && GridPane.getRowIndex(node)>3);

                try {
                    if (!hashMap.containsKey("groupby")){
                        hashMap.put("groupby",controller.dataFrame.groupby());
                    }
                    if (!hashMap.containsKey("max")) {
                        hashMap.put("max",((DataFrame.GroupByDataFrame) hashMap.get("groupby")).max());
                    }
                    for (int i=0;i<controller.dataFrame.getColumns().length; ++i){
                        TextField value = new TextField(controller.dataFrame.getColumns()[i]+": "+((DataFrame)hashMap.get("max")).getRecord(0)[i].toString());
//                        value.setLayoutX(100);
                        layout.add(value,2,i+4);
                    }
                }
                catch (Exception e) {

                    errorDisplayGroupby(e, "An error occured while grouping dataframe");
                }

            }
        });

        countSum.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!hashMap.containsKey("groupby")){
                        hashMap.put("groupby",controller.dataFrame.groupby());
                    }
                    if (!hashMap.containsKey("sum")) {
                        hashMap.put("sum",((DataFrame.GroupByDataFrame) hashMap.get("groupby")).sum());
                    }
                    int groupedIndex = 0;
                    for (int i=0;i<controller.dataFrame.getColumns().length; ++i){
                        TextField value;
                        if(!Objects.equals(controller.dataFrame.getColumns()[i],((DataFrame)hashMap.get("sum")).getColumns()[groupedIndex])){
                            value = new TextField(controller.dataFrame.getColumns()[i]+": "+"Column type doens't support such operation");
                        }
                        else{
                            value = new TextField(controller.dataFrame.getColumns()[i]+": "+((DataFrame)hashMap.get("sum")).getRecord(0)[groupedIndex].toString());
                            groupedIndex++;
                        }
                        layout.add(value,2,i+4);
                    }
                }
                catch (Exception e) {

                    e.printStackTrace();
                    errorDisplayGroupby(e, "An error occured while grouping dataframe");
                }

            }
        });

       countStd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                layout.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 2 && GridPane.getRowIndex(node)>3);
                try {
                    if (!hashMap.containsKey("groupby")){
                        hashMap.put("groupby",controller.dataFrame.groupby());
                    }
                    if (!hashMap.containsKey("std")) {
                        hashMap.put("std",((DataFrame.GroupByDataFrame) hashMap.get("groupby")).std());
                    }
                    int groupedIndex = 0;
                    for (int i=0;i<controller.dataFrame.getColumns().length; ++i){
                        TextField value;
                        if(!Objects.equals(controller.dataFrame.getColumns()[i],((DataFrame)hashMap.get("std")).getColumns()[groupedIndex])){
                            value = new TextField(controller.dataFrame.getColumns()[i]+": "+"Column type doens't support such operation");
                        }
                        else{
                            value = new TextField(controller.dataFrame.getColumns()[i]+": "+((DataFrame)hashMap.get("std")).getRecord(0)[groupedIndex].toString());
                            groupedIndex++;
                        }
                        layout.add(value,2,i+4);
                    }
                }
                catch (Exception e) {
                    errorDisplayGroupby(e, "An error occured while grouping dataframe");
                }
            }
        });

        countVar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                layout.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 2 && GridPane.getRowIndex(node)>3);
                try {
                    if (!hashMap.containsKey("groupby")){
                        hashMap.put("groupby",controller.dataFrame.groupby());
                    }
                    if (!hashMap.containsKey("var")) {
                        hashMap.put("var",((DataFrame.GroupByDataFrame) hashMap.get("groupby")).var());
                    }
                    int groupedIndex = 0;
                    for (int i=0;i<controller.dataFrame.getColumns().length; ++i){
                        TextField value;
                        if(!Objects.equals(controller.dataFrame.getColumns()[i],((DataFrame)hashMap.get("var")).getColumns()[groupedIndex])){
                            value = new TextField(controller.dataFrame.getColumns()[i]+": "+"Column type doens't support such operation");
                        }
                        else{
                            value = new TextField(controller.dataFrame.getColumns()[i]+": "+((DataFrame)hashMap.get("var")).getRecord(0)[groupedIndex].toString());
                            groupedIndex++;
                        }
                        layout.add(value,2,i+4);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    errorDisplayGroupby(e, "An error occured while grouping dataframe");
                }

            }
        });

        plot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage plotStage = new Stage();
                /*if (col1.getText().isEmpty() || col2.getText().isEmpty()){
                    errorDisplayGroupby(new InvalidParameterException(), "Cannot create chart, one col empty");
                }*/
                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("x");

                final LineChart<String,Number> lineChart =
                        new LineChart<String,Number>(xAxis,yAxis);
                lineChart.setTitle("Stock Monitoring, 2010");

                XYChart.Series series = new XYChart.Series();
                series.setName("Line chart of "+col1.getText()+" "+col2.getText());

                series.getData().add(new XYChart.Data("Jan", 23));
                series.getData().add(new XYChart.Data("Feb", 14));
                series.getData().add(new XYChart.Data("Mar", 15));
                series.getData().add(new XYChart.Data("Apr", 24));
                series.getData().add(new XYChart.Data("May", 34));
                series.getData().add(new XYChart.Data("Jun", 36));
                series.getData().add(new XYChart.Data("Jul", 22));
                series.getData().add(new XYChart.Data("Aug", 45));
                series.getData().add(new XYChart.Data("Sep", 43));
                series.getData().add(new XYChart.Data("Oct", 17));
                series.getData().add(new XYChart.Data("Nov", 29));
                series.getData().add(new XYChart.Data("Dec", 25));


                Scene scene  = new Scene(lineChart,800,600);
                lineChart.getData().add(series);

                plotStage.setScene(scene);
                plotStage.show();
            }
        });



        //layo
        //ut.add(new Button(),2,2);
       /* layout.add(col1, 1, 3);
        layout.add(col2, 3, 3);
        layout.add(col3, 5, 3);*/
        Scene scene = new Scene(root,600,400);

        //setting color to the scene
        /*button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                button.setText("Accepted");}});*/
        primaryStage.setScene(scene);
        primaryStage.show();
        }

    private void errorDisplayGroupby(Exception e, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(s);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
