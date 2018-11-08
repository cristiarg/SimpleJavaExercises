package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;

// a runnable class that performs some work and
// dispatches result as a text on a label
// in a thread-safe way
public class Worker implements Runnable {
  private Label label;
  int value;
  int waitIntervalMillis;

  public Worker(Label _l)
  {
    // receive a reference to the label on which we'll perform the update
    label = _l;
    resetWork();
    waitIntervalMillis = 1000;
  }

  public void resetWork() {
    final int newValue = 0;
    setValue(newValue);
    // make sure the reset value is visible
    dispatchWork();
  }

  private synchronized void setValue(int _v) {
    value = _v;
  }

  private synchronized int getValue() {
    return value;
  }

  public synchronized void setWaitIntervalMillis(int _v) {
    waitIntervalMillis = _v;
  }

  private synchronized int getWaitIntervalMillis() {
    return waitIntervalMillis;
  }

  private void dispatchWork() {
    // gather the work to be submitted
    // since this function is to be called from both
    // internal and external code, it should be thread-safe
    // wrt to accessing work
    final var v = getValue();

    // this seems to be the JavaFX way of submitting actions
    // to be executed on the UI thread
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        label.setText(Integer.toString(v));
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

        Thread.sleep(getWaitIntervalMillis());
        setValue(getValue() + 1);
      } catch (InterruptedException e) {
        break;
      }
    }
  }
}
