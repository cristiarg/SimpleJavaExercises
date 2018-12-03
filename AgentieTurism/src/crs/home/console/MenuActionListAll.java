package crs.home.console;

import crs.home.logic.Offers;

public class MenuActionListAll implements IMenuAction {
  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    System.out.println( "Following offers are available:" );
    _offers.displayOffers();
    System.out.println();
    return EMenuActionResult.Success;
  }

  @Override
  public String getUsage() {
    return "";
  }
}
