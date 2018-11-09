package crs.home;

import java.time.LocalTime;
import java.util.Random;

class Counter<T> {
  private IStorage storage;
  private Thread counting;
  private final int updateIntervalMillis;

  Counter(IStorage<T> _s, int _i) {
    storage = _s;
    updateIntervalMillis = _i;
  }

  void start() {
    if (counting == null) {
      counting = new Thread( new Runnable() {
        @Override
        public void run() {
          try {
            long v = 0;
            final var rand = new Random(LocalTime.now().getNano());

            while(!Thread.interrupted()) {
              storage.store( v );
              // TODO: for the line above:
              //  Warning:(22, 28) java: unchecked call to store(T) as a member of the raw type crs.home.IStorage

              ++v;
              Thread.sleep( rand.nextInt( updateIntervalMillis ) );
            }

            System.out.println( "COUNTER: interrupted" );

          } catch ( InterruptedException e ) {
            System.out.println( "COUNTER: InterruptedException: " + e.getMessage() );
            return;
          }
        }
      } );

      counting.start();
      System.out.println( "COUNTER:CONTROL: started worker thread." );
    }
  }

  void stop() throws InterruptedException {
    if(counting != null && counting.isAlive()) {
      System.out.println( "COUNTER:CONTROL: interrupting worker thread.." );
      counting.interrupt();
      counting.join();
      counting = null;
      System.out.println( "COUNTER:CONTROL: worker thread joined." );
    }
  }
}
