package crs.home.console;

import crs.home.console.EMenuActionResult;
import crs.home.console.MenuActionAddStay;
import crs.home.logic.Offers;
import crs.home.logic.TooManyOffersException;
import crs.home.model.Circuit;
import crs.home.model.IOffer;
import crs.home.model.Price;

public final class MenuActionAddCircuit extends MenuActionAddStay {
  private String transport;

  @Override
  protected void invalidateParams() {
    super.invalidateParams();
    transport = "";
  }

  private boolean validateTransport(String _transport) {
    transport = _transport;
    return true;
  }

  @Override
  protected boolean validateParams(String[] _params) {
    if (_params.length != 5) {
      System.out.println( "ERROR: addCircuit: incorrect number of parameters; expected: 4" );
      return false;
    } else {
      boolean valid = validateDestination( _params[1] );
      if (valid) {
        valid = validatePrice( _params[2] );
      }
      if (valid) {
        valid = validateDays( _params[3] );
      }
      if (valid) {
        valid = validateTransport( _params[4] );
      }
      return valid;
    }
  }

  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    invalidateParams();
    if (validateParams( _params )) {
      final IOffer offer = new Circuit( destination, new Price(price), days, transport);
      try {
        if ( !_offers.add(offer) ) {
          System.out.println( "ERROR: addCircuit: failed to add" );
          return EMenuActionResult.Failure;
        }
        return EMenuActionResult.Success;
      } catch ( TooManyOffersException e) {
        System.out.println( "ERROR: addStay: " + e.getMessage() );
      }
    }
    return EMenuActionResult.Failure;
  }

  @Override
  public String getUsage() {
    return "<destination> <price> <days> <transport>";
  }
}
