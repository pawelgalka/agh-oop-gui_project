package sample;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setResizable(false);
        controller.setStage(this.stage);
        primaryStage.setTitle("DataFrame Graphic Environment");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styl.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
