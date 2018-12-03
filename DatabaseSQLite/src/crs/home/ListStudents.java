package crs.home;

class ListStudents implements ConsoleMenu.MenuActionable {

  @Override
  public ConsoleMenu.ActionResult execute( String[] _arguments , Object _clientData ) {
    Storage storage = (Storage)_clientData;
    StorageHelper.listStudents( storage );
    return ConsoleMenu.ActionResult.Success; // TODO
  }

  @Override
  public String getName() {
    return "ListStudents";
  }

  @Override
  public String getUsage() {
    return "";
  }
}


