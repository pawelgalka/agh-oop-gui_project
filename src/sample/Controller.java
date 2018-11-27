package sample;

import dataframe.*;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;


public class Controller {

    private DataFrame dataFrame;
    private static File file;
    private static int index=0,i=0;
    private static String[] names;
    private static Class<? extends Value>[] types;
    private HashMap<String, Object> hashMap = new HashMap<>();
    private Stage myStage,columnsOrHeader=new Stage(),type=new Stage();
    private Pane pane=new Pane();
    private String[] groupby;
    private double one_width;
    private static boolean t = false,f=false;
    private String[] options = {"Max","Min","Sum","Var","Std"};
    private static String x,y;


    @FXML
    AnchorPane mainPane,plotChooser;

    @FXML
    ScrollPane spane;

    @FXML
    Label fileInfo,info,info1;

    @FXML
    Button nextButton,valinteger,valdouble,valfloat,valboolean,valstring,valdate;

    @FXML
    Button max,min,sum,std,var,btn;

    @FXML
    Menu stat;

    @FXML
    ImageView img;

    @FXML
    CheckBox header;

    @FXML
    TextArea columnsNames,groupBy,colChooser;

    static int numberOfColumns;

    public File chooseFile(){
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(myStage);
        return file;
    }

    public void readFile(ActionEvent event){
        t=false;
        file = chooseFile();
        System.out.println(file.getPath());
        System.out.println(1);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("columnsOrHeader.fxml"));
        try {
            Parent root = FXMLLoader.load(getClass().getResource("columnsOrHeader.fxml"));
            columnsOrHeader.setScene(new Scene(root,300,313));
            columnsOrHeader.showAndWait();
            types = new Class[numberOfColumns];
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
        if(t && types[numberOfColumns-1]!=null){
            try{
                dataFrame = new DataFrame(file.getPath(),types,names);
                hashMap.put("dataframe",dataFrame);
            }
            catch (Exception e){
                errorDisplayGroupby(e, "Error while creating dataframe");
                return;
            }
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
        }
    }

    private Value getMax(ArrayList<Value> listOfIntegers) throws Exception {
        return Collections.max(listOfIntegers);
    }

    private Value getMin(ArrayList<Value> listOfIntegers) throws Exception {
        return Collections.min(listOfIntegers);
    }

    private Value getSum(ArrayList<Value> listOfIntegers) throws Exception {
        if (listOfIntegers.get(0).getClass() == ValBoolean.class || listOfIntegers.get(0).getClass() == ValString.class || listOfIntegers.get(0).getClass() == ValDateTime.class) return null;
        return getMean(listOfIntegers).mul(new ValInteger(listOfIntegers.size()));
    }

    private Value getMean(ArrayList<Value> listOfIntegers) throws Exception {
        Value sum = listOfIntegers.get(0);
        for(int i=1; i<listOfIntegers.size(); ++i) sum = sum.add(listOfIntegers.get(i));
        return sum.div(new ValInteger(listOfIntegers.size()));
    }

    private Value getVariance(ArrayList<Value> listOfIntegers) throws Exception {
        if (listOfIntegers.get(0).getClass()==ValBoolean.class || listOfIntegers.get(0).getClass()==ValString.class || listOfIntegers.get(0).getClass()==ValDateTime.class) return null;
        Value mean = getMean(listOfIntegers);
        Value square = listOfIntegers.get(0).sub(mean).pow(new ValInteger(2));
        for(int i=1; i<listOfIntegers.size(); ++i) square = square.add(listOfIntegers.get(i).sub(mean).pow(new ValInteger(2)));
        return square.div(new ValInteger(listOfIntegers.size()));
    }

    private Value getStd(ArrayList<Value> list) throws Exception{
        if (list.get(0).getClass()==ValBoolean.class || list.get(0).getClass()==ValString.class || list.get(0).getClass()==ValDateTime.class) return null;
        return getVariance(list).pow(new ValDouble(0.5));
    }

    private void stats(String colname){
        pane = new Pane();
        System.out.println(one_width);
        pane.setBackground(new Background(new BackgroundFill(Color.web("#" + "635B5B" ), CornerRadii.EMPTY, Insets.EMPTY)));
        groupby = groupBy.getText().split(",");
        System.out.println(groupBy.getText().length());
        one_width = spane.getWidth();
        try {
            if (!hashMap.containsKey("groupby"+concatArray(groupby))) {
                hashMap.put("groupby"+concatArray(groupby), (groupBy.getText().length() == 0) ? dataFrame.groupby() : dataFrame.groupby(groupby));
            }
            DataFrame.GroupByDataFrame groupByDataFrame= (DataFrame.GroupByDataFrame) hashMap.get("groupby"+concatArray(groupby));
            for (int i = 0; i < 5; ++i) {
                TextField value = new TextField(options[i]);
                value.setPrefWidth(one_width/options.length);
                value.setPrefHeight(27);
                value.setMaxWidth(one_width/options.length);
                value.setLayoutX(one_width/options.length * i);
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
                    value.setPrefWidth(one_width/options.length);
                    value.setMaxWidth(one_width/options.length);
                    value.setPrefHeight(27);
                    value.setLayoutX(one_width/options.length * i);
                    value.setLayoutY(27*(j++));
                    pane.getChildren().add(value);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            errorDisplayGroupby(e, "An error occured while grouping dataframe");
        }
        spane.setContent(pane);
        one_width = spane.getWidth();
    }


    @FXML
    private ComboBox<String> xColumn;
    @FXML
    private ComboBox<String> yColumn;

    public void setDataFrame(DataFrame dataFrame, Controller controller) {
        this.dataFrame = dataFrame;
        controller.xColumn.setItems(FXCollections.observableArrayList(dataFrame.getColumns()));
        controller.yColumn.setItems(FXCollections.observableArrayList(dataFrame.getColumns()));
    }
private ArrayList<Integer> plotCols = new ArrayList<>();
    public void handle(ActionEvent event) {
        pane = new Pane();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ColumnsPlotChooser.fxml"));
            Parent root = fxmlLoader.load();
            Controller controller = fxmlLoader.<Controller>getController();
            setDataFrame(dataFrame,controller);
            type.setScene(new Scene(root));
            type.showAndWait();
            plotCols = dataFrame.GetIndexesOfColumns(x,y);
        } catch (Exception e){
            e.printStackTrace();
            errorDisplayGroupby(e,"XD");
            return;
        }
        String col1 = this.dataFrame.getColumns()[plotCols.get(0)], col2 = this.dataFrame.getColumns()[plotCols.get(1)];
        Column firstColumn = dataFrame.get(col1), secondColumn = dataFrame.get(col2);
        Class first_el = firstColumn.getArrayList().get(0).getClass(), second_el = secondColumn.getArrayList().get(1).getClass();

        XYChart.Series series1;

        final Axis xAxis,yAxis;
        final ScatterChart sc;
        if (first_el.isAssignableFrom(ValInteger.class) || first_el.isAssignableFrom(ValDouble.class) || first_el.isAssignableFrom(ValFloat.class)  ){
            if (second_el.isAssignableFrom(ValInteger.class) || second_el.isAssignableFrom(ValDouble.class) || second_el.isAssignableFrom(ValFloat.class)){
                xAxis = new NumberAxis();
                yAxis = new NumberAxis();
                sc = new ScatterChart<Number, Number>(xAxis,yAxis);
                series1 = new XYChart.Series<Number,Number>();

                System.out.println(1);
            }
            else {
                xAxis = new NumberAxis();
                yAxis = new CategoryAxis();
                sc = new ScatterChart<Number, String>(xAxis,yAxis);
                series1 = new XYChart.Series<Number, String>();
                System.out.println(2);

            }
        }
        else {
            if (second_el.isAssignableFrom(ValInteger.class) || second_el.isAssignableFrom(ValDouble.class) || second_el.isAssignableFrom(ValFloat.class)){
                xAxis = new CategoryAxis();
                yAxis = new NumberAxis();
                sc = new ScatterChart<String, Number>(xAxis,yAxis);
                series1 = new XYChart.Series<String,Number>();
                System.out.println(1);
            }
            else {
                xAxis = new CategoryAxis();
                yAxis = new CategoryAxis();
                sc = new
                        ScatterChart<String, String>(xAxis,yAxis);
                series1 = new XYChart.Series<String, String>();
                System.out.println(2);

            }
        }
        for (int i = 0; i < firstColumn.getArrayList().size(); i++) {
            series1.getData().add(new XYChart.Data<>(firstColumn.getArrayList().get(i).getValue(), secondColumn.getArrayList().get(i).getValue()));
        }

        xAxis.setLabel(col1);
        yAxis.setLabel(col2);
        sc.getData().addAll(series1);
        pane.getChildren().add(sc);
        spane.setContent(pane);

    }


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
        catch (Exception e){
            errorDisplayGroupby(e,"IOStream Exception");
        };
        stage.close();
        t = true;
    }

    String concatArray(String[] array){
        StringBuilder stringBuilder = new StringBuilder();
        for (String str: array) stringBuilder.append(str);
        if (stringBuilder.toString().length()==0) {
            System.out.println("Empty");return null;}
        return stringBuilder.toString();
    }

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

    public void closeWindow2(ActionEvent event){
        String source1 = event.getSource().toString();
        System.out.println(source1);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        if(xColumn.getSelectionModel().getSelectedItem() != null && yColumn.getSelectionModel().getSelectedItem() != null){
            x = xColumn.getSelectionModel().getSelectedItem();
            y = yColumn.getSelectionModel().getSelectedItem();
            stage.close();
        }

    }

    public void max(ActionEvent event){
        applyableStat("max");
    }

    public void min(ActionEvent event){
        applyableStat("min");
    }

    public void std(ActionEvent event){
        applyableStat("std");
    }

    public void var(ActionEvent event){
        applyableStat("var");
    }

    public void sum(ActionEvent event){
       applyableStat("sum");
    }


    /*------------------Methods to avoid redundancy------------------------*/

    private void errorDisplayGroupby(Exception e, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(s);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }


    private void ConfigureNotAllAvailableMethods(DataFrame current) {
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
    }

    private void SetHeadersOfStats() {
        for (int i = 0; i < dataFrame.getColumns().length; ++i) {
            TextField value = new TextField(dataFrame.getColumns()[i]);
            value.setMaxWidth(one_width/numberOfColumns);
            value.setLayoutX(one_width/numberOfColumns*i);
            pane.getChildren().add(value);
        }
    }

    private void applyableStat(String text){
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(Color.web("#" + "635B5B" ), CornerRadii.EMPTY, Insets.EMPTY)));
        groupby = groupBy.getText().split(",");
        System.out.println(groupBy.getText().length());
        try {
            if (!hashMap.containsKey("groupby"+concatArray(groupby))) {
                hashMap.put("groupby"+concatArray(groupby), (groupBy.getText().length() == 0) ? dataFrame.groupby() : dataFrame.groupby(groupby));
            }
            if (!hashMap.containsKey("std"+concatArray(groupby))) {
                Method method = DataFrame.GroupByDataFrame.class.getMethod(text);
                hashMap.put(text+concatArray(groupby), method.invoke(hashMap.get("groupby"+concatArray(groupby))));
            }
            SetHeadersOfStats();
            DataFrame current = (DataFrame) hashMap.get(text+concatArray(groupby));
            ConfigureNotAllAvailableMethods(current);

        } catch (Exception e) {e.printStackTrace();
            errorDisplayGroupby(e, "An error occured while grouping dataframe");
        }
        spane.setContent(pane);
    }

    void setStage(Stage stage) {
        myStage = stage;
        System.out.println(myStage);
        System.out.println(concatArray(new String[]{}));
    }

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
}

