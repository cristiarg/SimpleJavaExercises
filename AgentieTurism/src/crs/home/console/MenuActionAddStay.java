package crs.home.console;

import crs.home.logic.Offers;
import crs.home.logic.TooManyOffersException;
import crs.home.model.IOffer;
import crs.home.model.Price;
import crs.home.model.Stay;

public class MenuActionAddStay implements IMenuAction {
  // minimal caching to not perform parsing twice
  protected String destination;
  protected double price;
  protected int days;

  protected void invalidateParams() {
    destination = "";
    price = Double.NaN;
    days = -1;
  }

  protected boolean validateDestination(String _destination) {
    destination = _destination;
    return true;
  }

  protected boolean validatePrice(String _price) {
    try {
      price = Double.parseDouble( _price );
      return true;
    } catch (NumberFormatException e) {
      System.out.println( "ERROR: value for 'price' is not a number" );
      return false;
    }
  }

  protected boolean validateDays(String _days) {
    try {
      days = Integer.parseInt( _days );
      return true;
    } catch (NumberFormatException e) {
      System.out.println( "ERROR: value for 'days' is not a number" );
      return false;
    }
  }

  protected boolean validateParams(String[] _params) {
    if (_params.length != 4) {
      System.out.println( "ERROR: incorrect number of parameters; expected: 3" );
      return false;
    } else {
      boolean valid = validateDestination( _params[1] );
      if (valid) {
        valid = validatePrice( _params[2] );
      }
      if (valid) {
        valid = validateDays( _params[3] );
      }
      return valid;
    }
  }

  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    invalidateParams();
    if (validateParams( _params )) {
      final IOffer offer = new Stay( destination, new Price(price), days);
      try {
        if ( !_offers.add(offer) ) {
          System.out.println( "ERROR: addStay: failed to add" );
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
    return "<destination> <price> <days>";
  }
}
