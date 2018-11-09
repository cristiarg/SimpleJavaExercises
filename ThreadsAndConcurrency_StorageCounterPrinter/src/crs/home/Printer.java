package crs.home;

import java.time.LocalTime;
import java.util.Random;

class Printer<T> {
  private IStorage storage;
  private Thread printing;
  private final int updateIntervalMillis;

  Printer(IStorage<T> _s, int _i) {
    storage = _s;
    updateIntervalMillis = _i;
  }

  void start() {
    if (printing == null) {
      printing = new Thread( new Runnable() {
        @Override
        public void run() {
          try {
            final var rand = new Random(LocalTime.now().getNano());

            while(!Thread.interrupted()) {
              var v = storage.retrieve();
              System.out.println("Value: " + v.toString());

              Thread.sleep( rand.nextInt( updateIntervalMillis ) );
            }

            System.out.println( "PRINTER: interrupted." );

          } catch ( InterruptedException e ) {
            System.out.println( "PRINTER: InterruptedException: " + e.getMessage() );
            return;
          }
        }
      } );

      printing.start();
      System.out.println( "PRINTER:CONTROL: started worker thread." );
    }
  }

  void stop() throws InterruptedException {
    if(printing != null && printing.isAlive()) {
      System.out.println( "PRINTER:CONTROL: interrupting worker thread.." );
      printing.interrupt();
      printing.join();
      printing = null;
      System.out.println( "PRINTER:CONTROL: joined worker thread." );
    }
  }
}
