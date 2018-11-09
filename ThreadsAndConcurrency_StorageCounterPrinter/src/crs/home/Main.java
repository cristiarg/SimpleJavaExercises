package crs.home;

import java.io.IOException;

public class Main {
  // arbitrary palindromic primes
  private static int COUNTER_UPDATE_INTERVAL_MILLIS = 191;
  private static int PRINTER_UPDATE_INTERVAL_MILLIS = 373;

  public static void main(String[] args) throws InterruptedException, IOException {
    IStorage<Long> storage = new Storage<>(0L);

    Counter<Long> counter = new Counter<>( storage , COUNTER_UPDATE_INTERVAL_MILLIS );
    counter.start();

    Printer<Long> printer = new Printer<>( storage , PRINTER_UPDATE_INTERVAL_MILLIS );
    printer.start();

    // waiting for a key hit on the console
    System.out.println( "MAIN: waiting for use input to stop.." );
    System.in.read();

    System.out.println( "MAIN: stopping counter.." );
    counter.stop();
    System.out.println( "MAIN: stopped counter." );

    System.out.println( "MAIN: stopping printer.." );
    printer.stop();
    System.out.println( "MAIN: stopped printer." );

    //Thread.sleep(1000000);
  }
}
