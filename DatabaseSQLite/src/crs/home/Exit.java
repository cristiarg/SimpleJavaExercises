package crs.home;

class Exit implements ConsoleMenu.MenuActionable {

  @Override
  public ConsoleMenu.ActionResult execute( String[] _arguments , Object _clientData ) {
    return ConsoleMenu.ActionResult.Halt;
  }

  @Override
  public String getName() {
    return "Exit";
  }

  @Override
  public String getUsage() {
    return "";
  }
}




