package crs.home.console;

import crs.home.logic.Offers;

public class MenuActionExit implements IMenuAction {
  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    System.out.println( "Exiting application .." );
    return EMenuActionResult.Halt;
  }

  @Override
  public String getUsage() {
    return "";
  }
}
