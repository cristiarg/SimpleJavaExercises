package crs.home.console;

import crs.home.logic.Offers;

public class MenuActionListCircuits implements IMenuAction {
  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    System.out.println( "Following circuits are available:" );
    _offers.displayCircuits();
    System.out.println();
    return EMenuActionResult.Success;
  }

  @Override
  public String getUsage() {
    return "";
  }
}
