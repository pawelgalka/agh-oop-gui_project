package sample;
import dataframe.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;


import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main1 extends Application {

    Button button;
    Scene sc1, sc2;
    Stage window;
    Label label;
    static DataFrame x;

    public static void main(String[] args) throws IOException {

        try {
            launch(args);


//            DataFrame check2 = new DataFrame("io/groupby.csv", new String[]{"NewString", "NewDateTime", "NewDouble", "NewDouble"},
//                    true);
//
//            ArrayList<DataFrame> c;
//            ArrayList<Value> maxes;
//            ArrayList<Value> maxes2;
//            ArrayList<Value> maxes3;
//
//            c = check2.groupby(0);
//            maxes = DataFrame.getVar(c, 2);
//            for (Value data : maxes) {
//                data.print();
//                System.out.println();
//            }
//            System.out.println();
//            System.out.println();
//            System.out.println();
//            maxes = DataFrame.getStd(c, 2);
//            for (Value data : maxes) {
//                data.print();
//                System.out.println();
//            }
//            check2.dfprint();
//
//            check2.mul(2, new NewDouble.Builder().val(2.0).build());
//            check2.sub(4, 2);
//            check2.sub(5, 2);
//            check2.dfprint();


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        window.setTitle("DataFrame Manager 0.5");

        AtomicReference<DataFrame> res = new AtomicReference<>();
        AtomicReference<ArrayList<DataFrame>> DFList = new AtomicReference<>();
        AtomicReference<ArrayList<Value>> aggregated = new AtomicReference<>();
        AtomicInteger flag = new AtomicInteger(0);
        button = new Button("Open CSV");
        Button countMin = new Button("Count Min");
        Button countMax = new Button("Count Max");
        Button countSum = new Button("Count Sum");
        Button countVar = new Button("Count Var");
        Button countStd = new Button("Count Std");
        Button plot = new Button("Plot");

        label = new Label();

        FileChooser fileChooser = new FileChooser();
        GridPane layout = new GridPane();

        TextArea col1 = new TextArea();
        col1.setMaxWidth(7);
        col1.setMaxHeight(7);
        TextArea col2 = new TextArea();
        col2.setMaxWidth(7);
        col2.setMaxHeight(7);
        TextArea col3 = new TextArea();
        col3.setMaxWidth(7);
        col3.setMaxHeight(7);
        AtomicInteger a = new AtomicInteger(-1);
        AtomicInteger b = new AtomicInteger(-1);
        AtomicInteger c = new AtomicInteger(2);

        Scene scene = new Scene(layout, 600, 600);
        button.setOnAction(e -> {
            try {

                File f = fileChooser.showOpenDialog(window);
                DataFrame check = new DataFrame(f.getPath(), new Class[]{ValString.class, ValDateTime.class, ValDouble.class});

                res.set(check);

                /*if (check.getData().get(0).get(0) != null)
                    System.out.println("File Loaded");*/
                if (col1.getText().length() > 0 && col2.getText().length() > 0) {
                    a.set(Integer.parseInt(col1.getText()));
                    b.set(Integer.parseInt(col2.getText()));
                    //DFList.set(res.get().groupby(new int[]{a.get(), b.get()}));
                } else if (col1.getText().length() > 0) {
                    a.set(Integer.parseInt(col1.getText()));
                    //DFList.set(res.get().groupby(a.get()));
                } else if (col2.getText().length() > 0) {
                    a.set(Integer.parseInt(col2.getText()));
                   // DFList.set(res.get().groupby(a.get()));
                } else {
                    ArrayList<DataFrame> tmp = new ArrayList<>();
                    tmp.add(res.get());
                    DFList.set(tmp);
                }
                if (col3.getText().length() > 0) c.set(Integer.parseInt(col3.getText()));
                if (flag.get() == 0) {
                    layout.add(countMin, 2, 2);
                    layout.add(countMax, 3, 2);
                    layout.add(countStd, 4, 2);
                    layout.add(countSum, 5, 2);
                    layout.add(countVar, 6, 2);
                    flag.set(1);
                }
                aggregate(DFList, aggregated, countMin, c.get());
                aggregate(DFList, aggregated, countSum, c.get());
                aggregate(DFList, aggregated, countStd, c.get());
                aggregate(DFList, aggregated, countVar, c.get());
                aggregate(DFList, aggregated, countMax, c.get());

            } catch (Exception ee) {
                System.out.println(ee);
            }
        });
        /*plot.setOnAction(pl -> {


            File f = fileChooser.showOpenDialog(window);
            final DataFrame check = new DataFrame(f, new String[]{"NewDouble", "NewDouble", "NewDouble"},
                    true);

            final XYSeriesDemo demo = new XYSeriesDemo("XY Series Demo", 1, 0, check);
            demo.pack();
            RefineryUtilities.centerFrameOnScreen(demo);
            demo.setVisible(true);
        });*/
        layout.add(plot, 1, 2);
        layout.add(button, 3, 1);
        layout.add(col1, 1, 3);
        layout.add(col2, 3, 3);
        layout.add(col3, 5, 3);
        layout.add(label, 1, 4, 5, 1);
        layout.setVgap(10);
        layout.setAlignment(Pos.BASELINE_CENTER);
        window.setScene(scene);
        window.show();

    }

    private void aggregate(AtomicReference<ArrayList<DataFrame>> DFList, AtomicReference<ArrayList<Value>> aggregated, Button oper, int col) {
        /*oper.setOnAction(e -> {
            try {
                switch (oper.getText()) {
                    case "Count Min":
                        aggregated.set(DataFrame.getMin(DFList.get(), col));
                        break;
                    case "Count Max":
                        aggregated.set(DataFrame.getMax(DFList.get(), col));
                        break;
                    case "Count Sum":
                        aggregated.set(DataFrame.getSum(DFList.get(), col));
                        break;
                    case "Count Std":
                        aggregated.set(DataFrame.getStd(DFList.get(), col));
                        break;
                    case "Count Var":
                        aggregated.set(DataFrame.getVar(DFList.get(), col));
                        break;
                }
                label.setText("");
                for (Value data : aggregated.get()) {
                    label.setText(label.getText() + data.get() + "\n");
                }

            } catch (Exception ee) {
                System.out.println(ee);
            }
        });*/
    }
}