package crs.home;

import java.util.EnumMap;
import java.util.*;
import java.io.IOException;

enum EMenuAction {
  addStay,
  addCircuit,
  listAll,
  listStays,
  listCircuits,
  delete,
  exit
}

enum EMenuActionResult {
  NotFound,
  Success,
  Failure,
  Halt
}

interface IMenuAction {
  EMenuActionResult act(String[] _params, Offers _offers);
  String getUsage();
}

class MenuActionAddStay implements IMenuAction {
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
      } catch (TooManyOffersException e) {
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

final class MenuActionAddCircuit extends MenuActionAddStay {
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
      } catch (TooManyOffersException e) {
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

class MenuActionListAll implements IMenuAction {
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

class MenuActionListStays implements IMenuAction {
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

class MenuActionListCircuits implements IMenuAction {
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

class MenuActionDelete implements IMenuAction {
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

class MenuActionExit implements IMenuAction {
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

class Main {
  private static Map<EMenuAction, IMenuAction > commandMap;
  static {
    var cm = new EnumMap<EMenuAction, IMenuAction>(EMenuAction.class);
    cm.put(EMenuAction.addStay, new MenuActionAddStay());
    cm.put(EMenuAction.addCircuit, new MenuActionAddCircuit());
    cm.put(EMenuAction.listAll, new MenuActionListAll());
    cm.put(EMenuAction.listStays, new MenuActionListStays());
    cm.put(EMenuAction.listCircuits, new MenuActionListCircuits());
    cm.put(EMenuAction.delete, new MenuActionDelete());
    cm.put(EMenuAction.exit, new MenuActionExit());
    commandMap = Collections.unmodifiableMap( cm );
  }

  private static void displayMenu(ConsoleHelper _console) throws IOException {
    _console.writeLine("Please choose a command:");
    for(final EMenuAction comm : commandMap.keySet()) {
      IMenuAction menuAction = commandMap.get(comm);
      _console.writeLine(comm.toString() + " " + menuAction.getUsage());
    }
  }

  private static EMenuActionResult decodeAndDispatchCommand(String _line, Offers _offers) {
    final var tokenArray = _line.split("\\s+");

    // try to unambiguously find a matching command
    boolean ambiguous = false;
    IMenuAction menuAction = null;
    for(final var command : commandMap.keySet()) {
      if (command.toString().startsWith( tokenArray[0] )) {
        if (menuAction == null) {
          menuAction = commandMap.get(command);
        } else {
          ambiguous = true;
          break;
        }
      }
    }

    if (menuAction == null) {
      System.out.println( "ERROR: input action did not match any of the options" );
      return EMenuActionResult.NotFound;
    } else if(ambiguous) {
      System.out.println( "ERROR: input action is ambiguous; matches more than one option" );
      return EMenuActionResult.NotFound;
    } else {
      final var res = menuAction.act(tokenArray, _offers);
      return res;
    }
  }

  public static void main(String[] args) throws IOException {
    ConsoleHelper consoleHelper = new ConsoleHelper();

    final List< IDiscount > discountList = DiscountFactory.getProductionDiscountList();
    final IDiscountStrategy discountStrategy = new MaximumDiscountStrategy( discountList );
    Offers offers = new Offers( discountStrategy );

    boolean halt = false;
    while(!halt) {
      displayMenu( consoleHelper );
      final var line = consoleHelper.readLine();
      final var res = decodeAndDispatchCommand( line, offers );
      halt = (res == EMenuActionResult.Halt);
    }
  }
}
