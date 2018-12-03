package crs.home;

import java.util.Optional;

class ListMarks implements ConsoleMenu.MenuActionable {

  @Override
  public ConsoleMenu.ActionResult execute( String[] _arguments, Object _clientData ) {
    if (_arguments.length != 1) {
      System.err.println( "ERROR: Expecting argument(s): " + getUsage() );
      return ConsoleMenu.ActionResult.Failure;
    }

    Storage storage = (Storage)_clientData;

    final String studentName = _arguments[0];

    final Optional<Integer> studentCount = StorageHelper.countStudents( storage, studentName);
    if(studentCount.isEmpty()) {
      System.err.println( "ERROR: Error upon database access." );
      return ConsoleMenu.ActionResult.Failure;
    }

    if (studentCount.get() < 1) {
      System.out.println( "No student identified by '" + studentName + "'. Please consult the list of registered students." );
      return ConsoleMenu.ActionResult.Success;
    }

    // TODO: inform if there are no students with the given name
    StorageHelper.listMarks( storage, studentName);
    return ConsoleMenu.ActionResult.Success; // TODO
  }

  @Override
  public String getName() {
    return "ListMarks";
  }

  @Override
  public String getUsage() {
    return "<student name>";
  }
}

