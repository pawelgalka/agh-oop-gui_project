package sample;

import dataframe.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;


public class Controller {

    static DataFrame dataFrame;
    static File file;
    static int index=0;
    static String[] names;
    static Class<? extends Value>[] types;

    HashMap<String, Object> hashMap = new HashMap<>();
    private Stage myStage,columnsOrHeader=new Stage(),type=new Stage();

    public void setStage(Stage stage) {
        myStage = stage;
        System.out.println(myStage);
    }


    Pane pane=new Pane();

    @FXML
    AnchorPane mainPane;

    @FXML
    ScrollPane spane;

    @FXML
    Label fileInfo,info,info1;

    @FXML
    Button nextButton,valinteger,valdouble,valfloat,valboolean,valstring,valdate;

    @FXML
    Button max,min,sum,std,var;

    @FXML
    Menu stat;




    String[] options = {"Max","Min","Sum","Var","Std"};
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

    public File chooseFile(){
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(myStage);
        return file;
    }

    public void readFile(ActionEvent event){
        t=false;
        this.file = chooseFile();
        System.out.println(file.getPath());
        System.out.println(1);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("columnsOrHeader.fxml"));
        try {
            Parent root = FXMLLoader.load(getClass().getResource("columnsOrHeader.fxml"));
            columnsOrHeader.setScene(new Scene(root,300,313));

            columnsOrHeader.showAndWait();
            types = new Class[numberOfColumns];
            System.out.println("XXXX"+numberOfColumns);
            for (int i=0; i<numberOfColumns; ++i){
                index = i;
                fxmlLoader = new FXMLLoader(getClass().getResource("TypeChoose.fxml"));
                root = fxmlLoader.load();
                Controller controller = fxmlLoader.<Controller>getController();
                type.setScene(new Scene(root,600,140));
                controller.info.setText("Choose type for "+names[i]+" column.");
                type.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(2);
        if(t && types[numberOfColumns-1]!=null){
            fileInfo.setText("Loaded file: "+file.getName());

            fileInfo.setDisable(false);
            info1.setDisable(false);
            groupBy.setDisable(false);
            max.setDisable(false);
            min.setDisable(false);
            std.setDisable(false);
            sum.setDisable(false);
            var.setDisable(false);
            for (String str:names){
                MenuItem add = new MenuItem(str);
                add.setOnAction(new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent t) {
                                        stats(str);
                                    }});
                stat.getItems().add(add);

            }
            one_width=spane.getWidth();
            dataFrame = new DataFrame(file.getPath(),types,names);
            hashMap.put("dataframe",dataFrame);
        }

    }
    Value getMax(ArrayList<Value> listOfIntegers) throws Exception {
//        if (listOfIntegers.get(0).getClass() == ValBoolean.class || listOfIntegers.get(0).getClass() == ValString.class || listOfIntegers.get(0).getClass() == ValDateTime.class)
//            return null;
//        Value mean = getMean(listOfIntegers);
//        Value square = listOfIntegers.get(0).sub(mean).pow(new ValInteger(2));
        return Collections.max(listOfIntegers);
    }

    Value getMin(ArrayList<Value> listOfIntegers) throws Exception {
//        if (listOfIntegers.get(0).getClass() == ValBoolean.class || listOfIntegers.get(0).getClass() == ValString.class || listOfIntegers.get(0).getClass() == ValDateTime.class)
//            return null;
//        Value mean = getMean(listOfIntegers);
//        Value square = listOfIntegers.get(0).sub(mean).pow(new ValInteger(2));
        return Collections.min(listOfIntegers);
    }

    Value getSum(ArrayList<Value> listOfIntegers) throws Exception {
        if (listOfIntegers.get(0).getClass() == ValBoolean.class || listOfIntegers.get(0).getClass() == ValString.class || listOfIntegers.get(0).getClass() == ValDateTime.class)
            return null;
//        Value mean = getMean(listOfIntegers);
//        Value square = listOfIntegers.get(0).sub(mean).pow(new ValInteger(2));
        return getMean(listOfIntegers).mul(new ValInteger(listOfIntegers.size()));
    }
    Value getMean(ArrayList<Value> listOfIntegers) throws Exception
    {
        Value sum = listOfIntegers.get(0);
        for(int i=1; i<listOfIntegers.size(); ++i)
            sum = sum.add(listOfIntegers.get(i));
        return sum.div(new ValInteger(listOfIntegers.size()));
    }

    Value getVariance(ArrayList<Value> listOfIntegers) throws Exception
    {
        if (listOfIntegers.get(0).getClass()==ValBoolean.class || listOfIntegers.get(0).getClass()==ValString.class || listOfIntegers.get(0).getClass()==ValDateTime.class) return null;
        Value mean = getMean(listOfIntegers);
        Value square = listOfIntegers.get(0).sub(mean).pow(new ValInteger(2));
        for(int i=1; i<listOfIntegers.size(); ++i)
            square = square.add(listOfIntegers.get(i).sub(mean).pow(new ValInteger(2)));
        return square.div(new ValInteger(listOfIntegers.size()));
    }
    Value getStd(ArrayList<Value> list) throws Exception{
        if (list.get(0).getClass()==ValBoolean.class || list.get(0).getClass()==ValString.class || list.get(0).getClass()==ValDateTime.class) return null;

        return getVariance(list).pow(new ValDouble(0.5));
    }
    public void stats(String colname){
        pane = new Pane();
        System.out.println(one_width);
        pane.setBackground(new Background(new BackgroundFill(Color.web("#" + "635B5B" ), CornerRadii.EMPTY, Insets.EMPTY)));
        groupby = groupBy.getText().split(",");
        System.out.println(groupBy.getText().length());
        one_width = spane.getWidth()/options.length;
        if (groupBy.getText().length()==0){
            try {
                if (!hashMap.containsKey("groupby")) {
                    hashMap.put("groupby", dataFrame.groupby());
                }
                for (int i = 0; i < 5; ++i) {
                    TextField value = new TextField(options[i]);
                    value.setPrefWidth(one_width);
                    value.setPrefHeight(27);
                    value.setMaxWidth(one_width);
                    value.setLayoutX(one_width*i);
                    pane.getChildren().add(value);
                    switch (i){
                        case 0:
                            value = new TextField(getMax(dataFrame.get(colname).getArrayList()).toString());
                            break;
                        case 1:
                            value = new TextField(getMin(dataFrame.get(colname).getArrayList()).toString());
                            break;
                        case 2:
                            if (getSum(dataFrame.get(colname).getArrayList())!=null)
                                value = new TextField(getSum(dataFrame.get(colname).getArrayList()).toString());
                            else
                                value = new TextField("Unavailable");
                            break;
                        case 3:
                            if (getVariance(dataFrame.get(colname).getArrayList())!=null)
                                value = new TextField(getVariance(dataFrame.get(colname).getArrayList()).toString());
                            else
                                value = new TextField("Unavailable");
                            break;
                        case 4:
                            if (getStd(dataFrame.get(colname).getArrayList())!=null)
                                value = new TextField(getStd(dataFrame.get(colname).getArrayList()).toString());
                            else
                                value = new TextField("Unavailable");
                            break;
                            default:value = new TextField(" ");
                    }
//                    value = new TextField( ((DataFrame) hashMap.get("max")).getRecord(0)[i].toString());
                    value.setPrefWidth(one_width);
                    value.setMaxWidth(one_width);
                    value.setPrefHeight(27);
                    value.setLayoutX(one_width*i);
                    value.setLayoutY(27);
                    pane.getChildren().add(value);

                }
            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }
        }else{
            try {
                if (!hashMap.containsKey("groupby"+concatArray(groupby))) {
                    hashMap.put("groupby"+concatArray(groupby), dataFrame.groupby(groupby));
                }
                DataFrame.GroupByDataFrame groupByDataFrame= (DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby));
                for (int i = 0; i < 5; ++i) {
                    TextField value = new TextField(options[i]);
                    value.setPrefWidth(one_width);
                    value.setPrefHeight(27);
                    value.setMaxWidth(one_width);
                    value.setLayoutX(one_width * i);
                    pane.getChildren().add(value);
                    int j=1;
                    for (DataFrame df:groupByDataFrame.getGroupDataFrameList()){
                        switch (i) {
                            case 0:
                                value = new TextField(getMax(df.get(colname).getArrayList()).toString());
                                break;
                            case 1:
                                value = new TextField(getMin(df.get(colname).getArrayList()).toString());
                                break;
                            case 2:
                                if (getSum(df.get(colname).getArrayList()) != null)
                                    value = new TextField(getSum(df.get(colname).getArrayList()).toString());
                                else
                                    value = new TextField("Unavailable");
                                break;
                            case 3:
                                if (getVariance(df.get(colname).getArrayList()) != null)
                                    value = new TextField(getVariance(df.get(colname).getArrayList()).toString());
                                else
                                    value = new TextField("Unavailable");
                                break;
                            case 4:
                                if (getStd(df.get(colname).getArrayList()) != null)
                                    value = new TextField(getStd(df.get(colname).getArrayList()).toString());
                                else
                                    value = new TextField("Unavailable");
                                break;
                            default:
                                value = new TextField(" ");
                        }
//                    value = new TextField( ((DataFrame) hashMap.get("max")).getRecord(0)[i].toString());
                        value.setPrefWidth(one_width);
                        value.setMaxWidth(one_width);
                        value.setPrefHeight(27);
                        value.setLayoutX(one_width * i);
                        value.setLayoutY(27*(j++));
                        pane.getChildren().add(value);
                    }

                }




            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }}
        spane.setContent(pane);
        one_width = spane.getWidth();
    }


    public void handle(ActionEvent event) {
            Stage stage = new Stage();
                /*if (col1.getText().isEmpty() || col2.getText().isEmpty()){
                    errorDisplayGroupby(new InvalidParameterException(), "Cannot create chart, one col empty");
                }*/
//            final CategoryAxis xAxis = new CategoryAxis();
//            final NumberAxis yAxis = new NumberAxis();
//            xAxis.setLabel("x");
//
//            final LineChart<String,Number> lineChart =
//                    new LineChart<String,Number>(xAxis,yAxis);
//            lineChart.setTitle("Stock Monitoring, 2010");
//
//            XYChart.Series series = new XYChart.Series();
//            series.setName("Line chart of ");
//
//            series.getData().add(new XYChart.Data("Jan", 23));
//        series.getData().add(new XYChart.Data("Jan", 14));
//            series.getData().add(new XYChart.Data("Mar", 15));
//            series.getData().add(new XYChart.Data("Apr", 24));
//            series.getData().add(new XYChart.Data("May", 34));
//            series.getData().add(new XYChart.Data("Jun", 36));
//            series.getData().add(new XYChart.Data("Jul", 22));
//            series.getData().add(new XYChart.Data("Aug", 45));
//            series.getData().add(new XYChart.Data("Sep", 43));
//            series.getData().add(new XYChart.Data("Oct", 17));
//            series.getData().add(new XYChart.Data("Nov", 29));
//            series.getData().add(new XYChart.Data("Dec", 25));
//
//
//            Scene scene  = new Scene(lineChart,800,600);
//            lineChart.getData().add(series);
//
//            plotStage.setScene(scene);
//            plotStage.show();
        stage.setTitle("Scatter Chart");
        
        final NumberAxis xAxis = new NumberAxis(0, 10, 1);
        final NumberAxis yAxis = new NumberAxis(-100, 500, 100);
        final ScatterChart<Number,Number> sc = new
                ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("Age (years)");
        yAxis.setLabel("Returns to date");
        sc.setTitle("Investment Overview");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Equities");
        series1.getData().add(new XYChart.Data(4.2, 193.2));
        series1.getData().add(new XYChart.Data(2.8, 33.6));
        series1.getData().add(new XYChart.Data(6.2, 24.8));
        series1.getData().add(new XYChart.Data(1, 14));
        series1.getData().add(new XYChart.Data(1.2, 26.4));
        series1.getData().add(new XYChart.Data(4.4, 114.4));
        series1.getData().add(new XYChart.Data(8.5, 323));
        series1.getData().add(new XYChart.Data(6.9, 289.8));
        series1.getData().add(new XYChart.Data(9.9, 287.1));
        series1.getData().add(new XYChart.Data(0.9, -9));
        series1.getData().add(new XYChart.Data(3.2, 150.8));
        series1.getData().add(new XYChart.Data(4.8, 20.8));
        series1.getData().add(new XYChart.Data(7.3, -42.3));
        series1.getData().add(new XYChart.Data(1.8, 81.4));
        series1.getData().add(new XYChart.Data(7.3, 110.3));
        series1.getData().add(new XYChart.Data(2.7, 41.2));
        sc.getData().addAll(series1);
        Scene scene  = new Scene(sc, 500, 400);
        stage.setScene(scene);
        stage.show();
        }

    @FXML
    CheckBox header;
    @FXML
    TextArea columnsNames,groupBy;
    static int numberOfColumns;
    public void closeWindow(ActionEvent event){
        Stage stage = (Stage) nextButton.getScene().getWindow();
        try{
            System.out.println(file);
            FileInputStream fstream = new FileInputStream(this.file.getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String[] firstLine = br.readLine().split(",");
            numberOfColumns= firstLine.length;
            if (!header.isSelected()) {
                if (columnsNames.getText().split(",").length != numberOfColumns) {
                    errorDisplayGroupby(new IllegalArgumentException(), "Not valid number of culumns names");
                    return;
                }
                names = columnsNames.getText().split(",");
            }
            else{
                names = firstLine;
            }
            br.close();
        }
        catch (Exception e){};
        stage.close();
        t = true;
    }
    String concatArray(String[] array){
        StringBuilder stringBuilder = new StringBuilder();
        for (String str: array) stringBuilder.append(str);
        return stringBuilder.toString();
    }
    static boolean t = false,f=false;
    public void closeWindow1(ActionEvent event){
        String source1 = event.getSource().toString();
        System.out.println(source1);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        switch (source1){
            case "Button[id=valinteger, styleClass=button]'Integer'":
                types[index] = ValInteger.class;
                break;
            case "Button[id=valdouble, styleClass=button]'Double'":
                types[index] = ValDouble.class;
                break;
            case "Button[id=valfloat, styleClass=button]'Float'":
                types[index] = ValFloat.class;
                break;
            case "Button[id=valboolean, styleClass=button]'Boolean'":
                types[index] = ValBoolean.class;
                break;
            case "Button[id=valstring, styleClass=button]'String'":
                types[index] = ValString.class;
                break;
            case "Button[id=valdate, styleClass=button]'DateTime'":
                types[index] = ValDateTime.class;
                break;

                default:
                    types[index]=Value.class;

        }
        stage.close();

    }
    @FXML
    ImageView img;
    String[] groupby;
    double one_width;
    public void max(ActionEvent event){
        pane = new Pane();

        System.out.println(one_width);
        pane.setBackground(new Background(new BackgroundFill(Color.web("#" + "635B5B" ), CornerRadii.EMPTY, Insets.EMPTY)));
        final ListView listView = new ListView();
        groupby = groupBy.getText().split(",");
        System.out.println(groupBy.getText().length());
        if (groupBy.getText().length()==0){
            try {
                if (!hashMap.containsKey("groupby")) {
                    hashMap.put("groupby", dataFrame.groupby());
                }
                if (!hashMap.containsKey("max")) {
                    hashMap.put("max", ((DataFrame.GroupByDataFrame) hashMap.get("groupby")).max());
                }
                for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    pane.getChildren().add(value);
                    value = new TextField( ((DataFrame) hashMap.get("max")).getRecord(0)[i].toString());
                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    value.setLayoutY(27);
                    pane.getChildren().add(value);

                }
            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }
        }else{
            try {
                if (!hashMap.containsKey("groupby"+concatArray(groupby))) {
                    hashMap.put("groupby"+concatArray(groupby), dataFrame.groupby(groupby));
                }
                System.out.println(1);
                System.out.println(hashMap.containsKey("max"+concatArray(groupby)));
                for(String str:hashMap.keySet()) System.out.println(str);
                if (!hashMap.containsKey("max"+concatArray(groupby))) {
                    hashMap.put("max"+concatArray(groupby), ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).max());
                }
                System.out.println(2);

                for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);

                    pane.getChildren().add(value);
                    DataFrame current = ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).max();

                    for (int j=0; j<current.size(); ++j) {
                        value = new TextField(current.getRecord(j)[i].toString());
                        value.setMaxWidth(one_width/numberOfColumns);
                        value.setLayoutX(one_width/numberOfColumns);
                        value.setLayoutX(one_width/numberOfColumns*i);
                        value.setLayoutY(27*(j+1));
                        pane.getChildren().add(value);

                    }

                }
            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }}
        spane.setContent(pane);
    }

    public void std(ActionEvent event){

        pane = new Pane();        pane.setBackground(new Background(new BackgroundFill(Color.web("#" + "635B5B" ), CornerRadii.EMPTY, Insets.EMPTY)));

        final ListView listView = new ListView();
        groupby = groupBy.getText().split(",");
        System.out.println(groupBy.getText().length());
        if (groupBy.getText().length()==0){
            try {
                if (!hashMap.containsKey("groupby")){
                    hashMap.put("groupby",dataFrame.groupby());
                }
                if (!hashMap.containsKey("std")) {
                    hashMap.put("std",((DataFrame.GroupByDataFrame) hashMap.get("groupby")).std());
                }
                int groupedIndex = 0;
                for (int i=0;i<dataFrame.getColumns().length; ++i){
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    pane.getChildren().add(value);
                    if(!Objects.equals(dataFrame.getColumns()[i],((DataFrame)hashMap.get("std")).getColumns()[groupedIndex])){
                        value = new TextField("Unavailable");
                    }
                    else{
                        value = new TextField(((DataFrame)hashMap.get("std")).getRecord(0)[groupedIndex].toString());
                        groupedIndex++;
                    }

                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    value.setLayoutY(27);
                    pane.getChildren().add(value);

                }
            }
            catch (Exception e) {
                e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }
        }
        else{
            try {
                if (!hashMap.containsKey("groupby"+concatArray(groupby))) {
                    hashMap.put("groupby"+concatArray(groupby), dataFrame.groupby(groupby));
                }
//            System.out.println(1);
                if (!hashMap.containsKey("std"+concatArray(groupby))) {

                    hashMap.put("std"+concatArray(groupby), ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).std());
                }
//            System.out.println(2);

                for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    pane.getChildren().add(value);
                }
                DataFrame current = ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).std();

                for (int j=0; j<current.size(); ++j) {
                    int groupedIndex = 0;
                    TextField value;
                    for(int i = 0; i < dataFrame.getColumns().length; ++i) {
                        if (!Objects.equals(dataFrame.getColumns()[i], current.getColumns()[groupedIndex])) {
                            value = new TextField("Unavailable");
                        } else {
                            value = new TextField(current.getRecord(j)[groupedIndex].toString());
                            groupedIndex++;
                        }
                        value.setMaxWidth(one_width/numberOfColumns);
                        value.setLayoutX(one_width/numberOfColumns);
                        value.setLayoutX(one_width/numberOfColumns* i);
                        value.setLayoutY(27 * (j + 1));
                        pane.getChildren().add(value);
                    }
                }

            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }}
        spane.setContent(pane);
    }

    public void var(ActionEvent event){

        pane = new Pane();         pane.setBackground(new Background(new BackgroundFill(Color.web("#" + "635B5B" ), CornerRadii.EMPTY, Insets.EMPTY)));

        final ListView listView = new ListView();
        groupby = groupBy.getText().split(",");
        System.out.println(groupBy.getText().length());
        if (groupBy.getText().length()==0){
            try {
                if (!hashMap.containsKey("groupby")){
                    hashMap.put("groupby",dataFrame.groupby());
                }
                if (!hashMap.containsKey("var")) {
                    hashMap.put("var",((DataFrame.GroupByDataFrame) hashMap.get("groupby")).var());
                }
                int groupedIndex = 0;
                for (int i=0;i<dataFrame.getColumns().length; ++i){
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    pane.getChildren().add(value);
                    if(!Objects.equals(dataFrame.getColumns()[i],((DataFrame)hashMap.get("var")).getColumns()[groupedIndex])){
                        value = new TextField("Unavailable");
                    }
                    else{
                        value = new TextField(((DataFrame)hashMap.get("var")).getRecord(0)[groupedIndex].toString());
                        groupedIndex++;
                    }

                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    value.setLayoutY(27);
                    pane.getChildren().add(value);

                }
            }
            catch (Exception e) {
                e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }
        }
        else{
            try {
                if (!hashMap.containsKey("groupby"+concatArray(groupby))) {
                    hashMap.put("groupby"+concatArray(groupby), dataFrame.groupby(groupby));
                }
//            System.out.println(1);
                if (!hashMap.containsKey("var"+concatArray(groupby))) {
                    hashMap.put("var"+concatArray(groupby), ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).var());
                }
//            System.out.println(2);

                for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    pane.getChildren().add(value);
                }
                DataFrame current = ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).var();

                for (int j=0; j<current.size(); ++j) {
                    int groupedIndex = 0;
                    TextField value;
                    for(int i = 0; i < dataFrame.getColumns().length; ++i) {
                        if (!Objects.equals(dataFrame.getColumns()[i], current.getColumns()[groupedIndex])) {
                            value = new TextField("Unavailable");
                        } else {
                            value = new TextField(current.getRecord(j)[groupedIndex].toString());
                            groupedIndex++;
                        }
                        value.setMaxWidth(one_width/numberOfColumns);
                        value.setLayoutX(one_width/numberOfColumns);
                        value.setLayoutX(one_width/numberOfColumns * i);
                        value.setLayoutY(27 * (j + 1));
                        pane.getChildren().add(value);
                    }
                }

            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }}
        spane.setContent(pane);
    }
    public void sum(ActionEvent event){

        pane = new Pane();         pane.setBackground(new Background(new BackgroundFill(Color.web("#" + "635B5B" ), CornerRadii.EMPTY, Insets.EMPTY)));

        final ListView listView = new ListView();
        groupby = groupBy.getText().split(",");
        System.out.println(groupBy.getText().length());
        if (groupBy.getText().length()==0){
            try {
                if (!hashMap.containsKey("groupby")){
                    hashMap.put("groupby",dataFrame.groupby());
                }
                if (!hashMap.containsKey("sum")) {
                    hashMap.put("sum",((DataFrame.GroupByDataFrame) hashMap.get("groupby")).sum());
                }
                int groupedIndex = 0;
                for (int i=0;i<dataFrame.getColumns().length; ++i){
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    pane.getChildren().add(value);
                    if(!Objects.equals(dataFrame.getColumns()[i],((DataFrame)hashMap.get("sum")).getColumns()[groupedIndex])){
                        value = new TextField("Unavailable");
                    }
                    else{
                        value = new TextField(((DataFrame)hashMap.get("sum")).getRecord(0)[groupedIndex].toString());
                        groupedIndex++;
                    }

                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    value.setLayoutY(27);
                    pane.getChildren().add(value);

                }
            }
            catch (Exception e) {
                e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }
        }
        else{
            try {
                if (!hashMap.containsKey("groupby"+concatArray(groupby))) {
                    hashMap.put("groupby"+concatArray(groupby), dataFrame.groupby(groupby));
                }
//            System.out.println(1);
                if (!hashMap.containsKey("sum"+concatArray(groupby))) {
                    hashMap.put("sum"+concatArray(groupby), ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).min());
                }
//            System.out.println(2);

                for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    pane.getChildren().add(value);
                }
                    DataFrame current = ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).sum();

                    for (int j=0; j<current.size(); ++j) {
                    int groupedIndex = 0;
                    TextField value;
                    for(int i = 0; i < dataFrame.getColumns().length; ++i) {
                        if (!Objects.equals(dataFrame.getColumns()[i], current.getColumns()[groupedIndex])) {
                            value = new TextField("Unavailable");
                        } else {
                            value = new TextField(current.getRecord(j)[groupedIndex].toString());
                            groupedIndex++;
                        }
                        value.setMaxWidth(one_width/numberOfColumns);
                        value.setLayoutX(one_width/numberOfColumns);
                        value.setLayoutX(one_width/numberOfColumns * i);
                        value.setLayoutY(27 * (j + 1));
                        pane.getChildren().add(value);
                    }
                }

        } catch (Exception e) {e.printStackTrace();
            errorDisplayGroupby(e, "An error occured while grouping dataframe");
        }}
        spane.setContent(pane);
    }

        public void min(ActionEvent event){
        pane = new Pane();         pane.setBackground(new Background(new BackgroundFill(Color.web("#" + "635B5B" ), CornerRadii.EMPTY, Insets.EMPTY)));

            final ListView listView = new ListView();
        groupby = groupBy.getText().split(",");
        System.out.println(groupBy.getText().length());
        if (groupBy.getText().length()==0){
            try {
                if (!hashMap.containsKey("groupby")) {
                    hashMap.put("groupby", dataFrame.groupby());
                }
                if (!hashMap.containsKey("min")) {
                    hashMap.put("min", ((DataFrame.GroupByDataFrame) hashMap.get("groupby")).min());
                }
                for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    pane.getChildren().add(value);
                    value = new TextField( ((DataFrame) hashMap.get("min")).getRecord(0)[i].toString());
                    value.setPrefWidth(one_width/numberOfColumns);
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setPrefHeight(27);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    value.setLayoutY(27);
                    pane.getChildren().add(value);

                }
            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }
        }else{
        try {
            if (!hashMap.containsKey("groupby"+concatArray(groupby))) {
                hashMap.put("groupby"+concatArray(groupby), dataFrame.groupby(groupby));
            }
            System.out.println(1);
            if (!hashMap.containsKey("min"+concatArray(groupby))) {
                hashMap.put("min"+concatArray(groupby), ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).min());
            }
            System.out.println(2);

            for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                TextField value = new TextField(dataFrame.getColumns()[i]);
                value.setMaxWidth(one_width/numberOfColumns);
                value.setLayoutX(one_width/numberOfColumns*i);

                pane.getChildren().add(value);
                DataFrame current = ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby))).min();

                for (int j=0; j<current.size(); ++j) {
                    value = new TextField(current.getRecord(j)[i].toString());
                    value.setMaxWidth(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns);
                    value.setLayoutX(one_width/numberOfColumns*i);
                    value.setLayoutY(27*(j+1));
                    pane.getChildren().add(value);

                }

            }
        } catch (Exception e) {e.printStackTrace();
            errorDisplayGroupby(e, "An error occured while grouping dataframe");
        }}
        spane.setContent(pane);
    }
    private void errorDisplayGroupby(Exception e, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(s);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

}

