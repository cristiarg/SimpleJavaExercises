package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;


/**
 * a runnable class that performs some work and dispatches result as a text
 * on a supplied label reference in a jfx-correct way
 */
class Worker implements Runnable {
  private Label label;

  /**
   * value is accessed from synchronized methods only
   */
  private int value;
  /**
   * alternative to the above - finer grained; can be accessed
   * from either synchronized or unsynchronized methods
   */
  private volatile int waitIntervalMillis;

  Worker(Label _l)
  {
    // receive a reference to the label on which we'll perform the update
    label = _l;
    resetWork();
    waitIntervalMillis = 1000;
  }

  void resetWork() {
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

  void setWaitIntervalMillis(int _v) {
    waitIntervalMillis = _v;
  }

  private int getWaitIntervalMillis() {
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
