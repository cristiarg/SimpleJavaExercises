package crs.home;

class ListDomains implements ConsoleMenu.MenuActionable {

  @Override
  public ConsoleMenu.ActionResult execute( String[] _arguments , Object _clientData ) {
    Storage storage = (Storage)_clientData;
    StorageHelper.listDomains( storage );
    return ConsoleMenu.ActionResult.Success; // TODO
  }

  @Override
  public String getName() {
    return "ListDomains";
  }

  @Override
  public String getUsage() {
    return "";
  }
}



