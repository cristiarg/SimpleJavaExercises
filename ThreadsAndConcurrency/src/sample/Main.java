package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class Main extends Application {
  Button buttonStart;
  Label labelValue;

  Thread workerThread;

  @Override
  public void start(Stage primaryStage) throws Exception {
    //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
    primaryStage.setTitle("Threading update");

    buttonStart = new Button();
    buttonStart.setText("Start work");
    buttonStart.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        doStartWorker();
      }
    });

    labelValue = new Label();
    labelValue.setStyle("-fx-background-color: lightblue;");
    labelValue.setPrefWidth(100);

    var tilePane = new TilePane();
    tilePane.setHgap(5);
    tilePane.setVgap(5);
    tilePane.setTileAlignment(Pos.TOP_LEFT);
    tilePane.getChildren().add(buttonStart);
    tilePane.getChildren().add(labelValue);

    var scene = new Scene(tilePane, 300, 250);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private void doStartWorker() {
    if (workerThread == null) {
      workerThread = new Thread(new Worker(labelValue));
      workerThread.start();
    }
  }

  private void doStopWorker() {
    if (workerThread != null) {
      final var st = workerThread.getState();
      if (st != Thread.State.NEW && st != Thread.State.TERMINATED) {
        workerThread.interrupt();
      }
    }
  }

  @Override
  public void stop()
  {
    doStopWorker();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

// NOTE: OpenJDK 11 + OpenJFX 11
//    https://stackoverflow.com/questions/52467561/intellij-cant-recognize-javafx-11-with-openjdk-11

// https://dzone.com/articles/javafx-on-jdk-11

