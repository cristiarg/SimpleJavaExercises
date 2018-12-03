package crs.home;

class AddStudent implements ConsoleMenu.MenuActionable {

  @Override
  public ConsoleMenu.ActionResult execute( String[] _arguments , Object _userData ) {
    // expecting at least 'name' and 'code', if not also 'phone'
    if (_arguments.length < 2) {
      System.err.println( "ERROR: Expecting argument(s): " + getUsage() + ". Student was not added." );
      return ConsoleMenu.ActionResult.Failure;
    } else {
      String name = _arguments[0];
      String code = _arguments[1];
      String phone = (_arguments.length > 2) ? _arguments[2] : "";
      Storage storage = (Storage)_userData;
      StorageHelper.addStudent(storage, name, code, phone);
    }
    return ConsoleMenu.ActionResult.Failure;
  }

  @Override
  public String getName() {
    return "AddStudent";
  }

  @Override
  public String getUsage() {
    return "<student name> <code> <phone>";
  }
}


