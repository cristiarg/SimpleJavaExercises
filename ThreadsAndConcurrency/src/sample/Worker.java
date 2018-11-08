package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;

// a runnable class that performs some work and
// dispatches result as a text on a label
// in a thread-safe way
public class Worker implements Runnable {
  private Label label;
  int value;

  public Worker(Label _l)
  {
    // receive a reference to the label on which we'll perform the update
    label = _l;
    reset();
  }

  public synchronized void reset() {
    value = 0;
    // make sure the reset value is visible
    dispatchWork();
  }

  private void dispatchWork() {
    // this seems to be the JavaFX way of submitting actions
    // to be executed on the UI thread
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        label.setText(Integer.toString(value));
      }
    });
  }

  @Override
  public void run() {
    while(true) {
      try {
        dispatchWork();

        //if (Thread.interrupted()) {
        //  break;
        //}

        Thread.sleep(1000);
        ++value;
      } catch (InterruptedException e) {
        break;
      }
    }
  }
}
