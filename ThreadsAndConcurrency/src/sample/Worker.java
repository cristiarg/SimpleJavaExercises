package sample;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Worker implements Runnable {
  private Label label;
  int value;

  public Worker(Label _l)
  {
    // receive a reference to the label on which we'll perform the update
    label = _l;
    value = 0;
  }

  @Override
  public void run() {
    while(true) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          label.setText(Integer.toString(value));
        }
      });

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // TODO: for the moment, nothing
        final var v = e;
      }

      ++value;
    }
  }
}
