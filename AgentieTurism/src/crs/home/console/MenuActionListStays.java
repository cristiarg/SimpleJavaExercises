package crs.home.console;

import crs.home.logic.Offers;

public class MenuActionListStays implements IMenuAction {
  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    System.out.println( "Following stays are available:" );
    _offers.displayStays();
    System.out.println();
    return EMenuActionResult.Success;
  }

  @Override
  public String getUsage() {
    return "";
  }
}
