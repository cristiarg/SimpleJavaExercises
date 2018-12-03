package crs.home.console;

import crs.home.console.EMenuActionResult;
import crs.home.logic.Offers;

public class MenuActionDelete implements IMenuAction {
  private String name;

  private boolean validateParams(String[] _params) {
    if (_params.length != 2) {
      System.out.println( "ERROR: incorrect number of parameters; expected: 1" );
      return false;
    } else {
      name = _params[ 1 ];
      return true;
    }
  }

  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    if (validateParams( _params )) {
      final boolean removed = _offers.deleteOffer( name );
      if (removed) {
        System.out.println( "Offer was deleted." );
        return EMenuActionResult.Success;
      } else {
        System.out.println( "Could not delete offer." );
      }
    }
    return EMenuActionResult.Failure;
  }

  @Override
  public String getUsage() {
    return "<destination>";
  }
}
