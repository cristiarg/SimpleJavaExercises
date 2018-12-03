package crs.home;

import java.util.Optional;

class AddMark implements ConsoleMenu.MenuActionable {
  private static Optional<Integer> tryParse(String _number) {
    try {
      final int v = Integer.parseInt( _number );
      return Optional.of(v);
    } catch( NumberFormatException _e) {
      return Optional.empty();
    }
  }

  @Override
  public ConsoleMenu.ActionResult execute( String[] _arguments , Object _userData ) {
    if (_arguments.length != 3) {
      System.err.println( "ERROR: Expecting argument(s): " + getUsage() + ". Mark was not added." );
      return ConsoleMenu.ActionResult.Failure;
    }

    final Optional<Integer> optMark = tryParse( _arguments[2] );
    if (optMark.isEmpty()) {
      System.err.println( "ERROR: Mark is not an integer value." );
      return ConsoleMenu.ActionResult.Failure;
    }

    int mark = optMark.get();
    if (mark < 1 || mark > 10) {
      System.err.println( "ERROR: Invalid mark value." );
      return ConsoleMenu.ActionResult.Failure;
    }

    Storage storage = (Storage)_userData;

    final String studentName = _arguments[0];
    final Optional<Integer> studentId = StorageHelper.getStudentIdByName( storage, studentName );
    if (studentId.isEmpty()) {
      System.err.println( "ERROR: A student with the name '" + studentName + "' was not found, or the name is ambiguous." );
      System.err.println( "Please consult the list of registered students.");
      return ConsoleMenu.ActionResult.Failure;
    }

    final String domainName = _arguments[1];
    final Optional<Integer> domainId = StorageHelper.getDomainIdByName( storage, domainName );
    if (domainId.isEmpty()) {
      System.err.println( "ERROR: A domain with the name '" + domainName + "' was not found, or the name is ambiguous." );
      System.err.println( "Please consult the list of registered domains.");
      return ConsoleMenu.ActionResult.Failure;
    }

    final boolean insRes = StorageHelper.addMark( storage, studentId.get(), domainId.get(), mark );
    return (insRes ? ConsoleMenu.ActionResult.Success : ConsoleMenu.ActionResult.Failure);
  }

  @Override
  public String getName() {
    return "AddMark";
  }

  @Override
  public String getUsage() {
    return "<student name> <domain name> <mark>";
  }
}
