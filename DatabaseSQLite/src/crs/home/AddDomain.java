package crs.home;

class AddDomain implements ConsoleMenu.MenuActionable {

  @Override
  public ConsoleMenu.ActionResult execute( String[] _arguments , Object _userData ) {
    // expecting one argument: 'name'
    if (_arguments.length != 1) {
      System.err.println( "ERROR: Expecting argument(s): " + getUsage() + ". Domain was not added." );
      return ConsoleMenu.ActionResult.Failure;
    } else {
      String name = _arguments[0];
      Storage storage = (Storage)_userData;
      StorageHelper.addDomain(storage, name);
      return ConsoleMenu.ActionResult.Success;
    }
  }

  @Override
  public String getName() {
    return "AddDomain";
  }

  @Override
  public String getUsage() {
    return "<domain name>";
  }
}



