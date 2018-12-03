package crs.home;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    ConsoleHelper consoleHelper = new ConsoleHelper();

    if (!Storage.testDriver()) {
      System.out.println( "ERROR: could not find or load driver" );
      return;
    }

    String workingDirectory = System.getProperty("user.dir");
    Storage storage = new Storage(workingDirectory);

    ConsoleMenu consoleMenu = new ConsoleMenu();
    consoleMenu.add(new AddStudent());
    consoleMenu.add(new AddDomain());
    consoleMenu.add(new AddMark());
    consoleMenu.add(new ListStudents());
    consoleMenu.add(new ListDomains());
    consoleMenu.add(new ListMarks());
    consoleMenu.add(new ListAverage());
    consoleMenu.add(new Exit());

    ConsoleMenu.ActionResult actionResult = ConsoleMenu.ActionResult.Success;
    while(actionResult != ConsoleMenu.ActionResult.Halt) {
      consoleMenu.display();
      final String line = consoleHelper.readLine();
      actionResult = consoleMenu.execute( line, storage );
    }
  }
}
