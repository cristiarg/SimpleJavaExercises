package crs.home;

import java.util.Optional;

class ListAverage implements ConsoleMenu.MenuActionable {

  @Override
  public ConsoleMenu.ActionResult execute( String[] _arguments , Object _clientData ) {
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

    if (studentCount.get() > 1) {
      System.out.println( "More than one student identified by '" + studentName + "'. Please consult the list of registered students.");
      return ConsoleMenu.ActionResult.Success;
    }

    if (studentCount.get() < 1) {
      System.out.println( "No student identified by '" + studentName + "'. Please consult the list of registered students.");
      return ConsoleMenu.ActionResult.Success;
    }

    final Optional<Integer> markCount = StorageHelper.countMarks( storage, studentName );
    if (markCount.isEmpty()) {
      return ConsoleMenu.ActionResult.Failure;
    } else {
      if (markCount.get() == 0) {
        System.out.println( "No mark for student identified by '" + studentName + "'." );
        return ConsoleMenu.ActionResult.Success;
      } else {
        final Optional<Double> average = StorageHelper.averageMarks( storage, studentName );
        if (average.isEmpty()) {
          return ConsoleMenu.ActionResult.Failure;
        } else {
          System.out.println( "Average mark for student '" + studentName + "' is "
              + average.get() + " out of " + markCount.get() + " marks." );
          return ConsoleMenu.ActionResult.Success;
        }
      }
    }
  }

  @Override
  public String getName() {
    return "ListAverage";
  }

  @Override
  public String getUsage() {
    return "<student name>";
  }
}
