package sample;

import dataframe.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
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

    public Controller() {
//        System.out.println("first");
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
    public void pressButton(ActionEvent event){
        System.out.println("Hello");
    }

    public File chooseFile(){
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(myStage);
        return file;
    }
    String[] tmp;
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
        if(t){
            fileInfo.setText("Loaded file: "+file.getName());

            fileInfo.setDisable(false);
            info1.setDisable(false);
            groupBy.setDisable(false);
            max.setDisable(false);
            min.setDisable(false);
            std.setDisable(false);
            sum.setDisable(false);
            var.setDisable(false);
            for(String s:names){
                System.out.println(s);
            }
            for (Class c:types){
                System.out.println(c);
            }
            dataFrame = new DataFrame(file.getPath(),types,names);
//        dataFrame.print();
            hashMap.put("dataframe",dataFrame);
        }

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
    static boolean t = false,f=false;
    public void closeWindow1(ActionEvent event){
        String source1 = event.getSource().toString();
        System.out.println(source1);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        switch (source1){
            case "Button[id=valinteger, styleClass=button]'Integer'":
                types[index] = ValInteger.class;
                System.out.println(types[index]);
                System.out.println("III");
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
    String[] groupby;
    public void max(ActionEvent event){
        pane = new Pane();
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
                    value.setPrefWidth(100);
                    value.setPrefHeight(27);
                    value.setMaxWidth(100);
                    value.setLayoutX(100*i);
                    pane.getChildren().add(value);
                    value = new TextField( ((DataFrame) hashMap.get("max")).getRecord(0)[i].toString());
                    value.setPrefWidth(100);
                    value.setMaxWidth(100);
                    value.setPrefHeight(27);
                    value.setLayoutX(100*i);
                    value.setLayoutY(27);
                    pane.getChildren().add(value);

                }
            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }
        }else{
            try {
                if (!hashMap.containsKey("groupby"+groupby.toString())) {
                    hashMap.put("groupby"+groupby.toString(), dataFrame.groupby(groupby));
                }
                System.out.println(1);
                if (!hashMap.containsKey("max"+groupby.toString())) {
                    hashMap.put("max"+groupby.toString(), ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+groupby.toString())).max());
                }
                System.out.println(2);

                for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                    TextField value = new TextField(dataFrame.getColumns()[i]);
                    value.setMaxWidth(100);
                    value.setLayoutX(100*i);

                    pane.getChildren().add(value);
                    DataFrame current = ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+groupby.toString())).max();

                    for (int j=0; j<current.size(); ++j) {
                        value = new TextField(current.getRecord(j)[i].toString());
                        value.setMaxWidth(100);
                        value.setLayoutX(100);
                        value.setLayoutX(100*i);
                        value.setLayoutY(27*(j+1));
                        pane.getChildren().add(value);

                    }

                }
            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }}
        spane.setContent(pane);
    }

    public void sum(ActionEvent event){

        pane = new Pane();
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
                    TextField value;
                    if(!Objects.equals(dataFrame.getColumns()[i],((DataFrame)hashMap.get("sum")).getColumns()[groupedIndex])){
                        value = new TextField("Unavailable");
                    }
                    else{
                        value = new TextField(((DataFrame)hashMap.get("sum")).getRecord(0)[groupedIndex].toString());
                        groupedIndex++;
                    }

                }
            }
            catch (Exception e) {

                e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }
        }else{
        try {
            if (!hashMap.containsKey("groupby"+groupby.toString())) {
                hashMap.put("groupby"+groupby.toString(), dataFrame.groupby(groupby));
            }
            System.out.println(1);
            if (!hashMap.containsKey("min"+groupby.toString())) {
                hashMap.put("min"+groupby.toString(), ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+groupby.toString())).min());
            }
            System.out.println(2);

            for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                TextField value = new TextField(dataFrame.getColumns()[i]);
                value.setMaxWidth(100);
                value.setLayoutX(100*i);

                pane.getChildren().add(value);
                DataFrame current = ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+groupby.toString())).min();

                for (int j=0; j<current.size(); ++j) {
                    value = new TextField(current.getRecord(j)[i].toString());
                    value.setMaxWidth(100);
                    value.setLayoutX(100);
                    value.setLayoutX(100*i);
                    value.setLayoutY(27*(j+1));
                    pane.getChildren().add(value);

                }

            }
        } catch (Exception e) {e.printStackTrace();
            errorDisplayGroupby(e, "An error occured while grouping dataframe");
        }}
        spane.setContent(pane);
    }

        public void min(ActionEvent event){
        pane = new Pane();
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
                    value.setPrefWidth(100);
                    value.setPrefHeight(27);
                    value.setMaxWidth(100);
                    value.setLayoutX(100*i);
                    pane.getChildren().add(value);
                    value = new TextField( ((DataFrame) hashMap.get("min")).getRecord(0)[i].toString());
                    value.setPrefWidth(100);
                    value.setMaxWidth(100);
                    value.setPrefHeight(27);
                    value.setLayoutX(100*i);
                    value.setLayoutY(27);
                    pane.getChildren().add(value);

                }
            } catch (Exception e) {e.printStackTrace();
                errorDisplayGroupby(e, "An error occured while grouping dataframe");
            }
        }else{
        try {
            if (!hashMap.containsKey("groupby"+groupby.toString())) {
                hashMap.put("groupby"+groupby.toString(), dataFrame.groupby(groupby));
            }
            System.out.println(1);
            if (!hashMap.containsKey("min"+groupby.toString())) {
                hashMap.put("min"+groupby.toString(), ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+groupby.toString())).min());
            }
            System.out.println(2);

            for (int i = 0; i < dataFrame.getColumns().length; ++i) {
                TextField value = new TextField(dataFrame.getColumns()[i]);
                value.setMaxWidth(100);
                value.setLayoutX(100*i);

                pane.getChildren().add(value);
                DataFrame current = ((DataFrame.GroupByDataFrame) hashMap.get("groupby"+groupby.toString())).min();

                for (int j=0; j<current.size(); ++j) {
                    value = new TextField(current.getRecord(j)[i].toString());
                    value.setMaxWidth(100);
                    value.setLayoutX(100);
                    value.setLayoutX(100*i);
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

