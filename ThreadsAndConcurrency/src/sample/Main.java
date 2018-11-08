package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class Main extends Application {
  Button buttonStart;
  Button buttonStop;
  Button buttonReset;
  Label labelValue;
  Spinner<Integer> spinnerInterval;

  // separate the worker from the thread
  // we'll recycle the worker, but not the thread
  Worker worker;
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
        Button b = (Button)event.getSource();
        b.setDisable(true);
        doStartWorker();
        b.setDisable(false);
      }
    });

    buttonStop = new Button();
    buttonStop.setText("Stop work");
    buttonStop.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Button b = (Button)event.getSource();
        b.setDisable(true);
        doStopWorker();
        b.setDisable(false);
      }
    });

    buttonReset = new Button();
    buttonReset.setText("Reset work");
    buttonReset.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Button b = (Button)event.getSource();
        b.setDisable(true);
        doResetWorker();
        b.setDisable(false);
      }
    });

    labelValue = new Label();
    labelValue.setStyle("-fx-background-color: lightblue;");
    labelValue.setPrefWidth(100);

    spinnerInterval = new Spinner<Integer>(100, 1000, 1000, 25);
    spinnerInterval.setEditable(true);
    spinnerInterval.valueProperty().addListener(new ChangeListener<Integer>() {
      @Override
      public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
        doSetWaitInterval(newValue);
      }
    });

    var tilePane = new TilePane();
    tilePane.setHgap(5);
    tilePane.setVgap(5);
    tilePane.setTileAlignment(Pos.TOP_LEFT);
    tilePane.getChildren().add(buttonStart);
    tilePane.getChildren().add(buttonStop);
    tilePane.getChildren().add(buttonReset);
    tilePane.getChildren().add(labelValue);
    tilePane.getChildren().add(spinnerInterval);

    var scene = new Scene(tilePane, 300, 250);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private Worker getWorker() {
    if (worker == null) {
      worker = new Worker(labelValue);
    }
    return worker;
  }

  private Thread getWorkerThread() {
    if (workerThread == null) {
      workerThread = new Thread(getWorker());
    }
    return workerThread;
  }

  private void doStartWorker() {
    Thread th = getWorkerThread();
    if (!th.isAlive()) {
      getWorkerThread().start();
    }
  }

  private void doStopWorker() {
    if (workerThread != null) {
      if (workerThread.isAlive()) {
        workerThread.interrupt();
        try {
          workerThread.join();
        } catch (InterruptedException e) {
          // TODO: for the moment, nothing
        }
      }
      workerThread = null;
    }
  }

  private void doResetWorker() {
    getWorker().resetWork();
  }

  private void doSetWaitInterval(int _newValue) {
    getWorker().setWaitIntervalMillis(_newValue);
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

