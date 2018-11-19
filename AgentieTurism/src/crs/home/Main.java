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
}

class MenuActionAddStay implements IMenuAction {
  // minimal caching to not perform parsing twice
  private String destination;
  private double price;
  private int days;

  private boolean validateParams(String[] _params) {
    destination = "";
    price = Double.NaN;
    days = -1;
    if (_params.length != 4) {
      System.out.println( "ERROR: addStay: incorrect number of parameters; expected: 3" );
      return false;
    } else {
      destination = _params[1];

      final var priceAsString = _params[2];
      try {
        price = Double.parseDouble( priceAsString );
      } catch (NumberFormatException e) {
        System.out.println( "ERROR: addStay: value for 'price' is not a number" );
        return false;
      }

      final var daysAsString = _params[3];
      try {
        days = Integer.parseInt( daysAsString );
      } catch (NumberFormatException e) {
        System.out.println( "ERROR: addStay: value for 'days' is not a number" );
        return false;
      }

      return true;
    }
  }

  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    if (validateParams( _params )) {
      final var stay = new Stay( destination, new Price(price), days);
      try {
        _offers.add(stay);
        return EMenuActionResult.Success;
      } catch (TooManyOffersException e) {
        System.out.println( "ERROR: addStay: " + e.getMessage() );
      }
    }
    return EMenuActionResult.Failure;
  }
}

class MenuActionAddCircuit implements IMenuAction {
  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    System.out.println( "ERROR: 'addCircuit' not implemented" );
    return EMenuActionResult.Success;
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
}

class MenuActionListStays implements IMenuAction {
  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    System.out.println( "Following stays are available:" );
    _offers.displayStays();
    System.out.println();
    return EMenuActionResult.Success;
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
}

class MenuActionDelete implements IMenuAction {
  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    System.out.println( "ERROR: 'delete' not implemented" );
    return EMenuActionResult.Success;
  }
}

class MenuActionExit implements IMenuAction {
  @Override
  public EMenuActionResult act( String[] _params , Offers _offers ) {
    System.out.println( "Exiting application .." );
    return EMenuActionResult.Halt;
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
    for(final var comm : commandMap.keySet()) {
      _console.writeLine(comm.toString());
    }
  }

  private static EMenuActionResult decodeAndDispatchCommand(String _line, Offers _offers) {
    EMenuActionResult result = EMenuActionResult.NotFound;
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
      //final String[] tokenSubArray = Arrays.copyOfRange(tokenArray, 1, tokenArray.length - 1);
      final var res = menuAction.act(tokenArray, _offers);
      return res;
    }
  }

  public static void main(String[] args) throws IOException {
    // if ( tip == sejur && pret > 1000 ) { 10% }
    // if ( tip == circuit && pret > 1000 ) { 5% }
    // if ( tip == circuit && pret > 1000 && durata > 10 ) { 15% }

    ConsoleHelper consoleHelper = new ConsoleHelper();

    Offers offers = new Offers();

    boolean halt = false;
    while(!halt) {
      displayMenu( consoleHelper );
      final var line = consoleHelper.readLine();
      final var res = decodeAndDispatchCommand( line, offers );
      halt = (res == EMenuActionResult.Halt);
//      if (res == EMenuActionResult.Failure) {
//        System.out.println( "ERROR: action failed" );
//      }
    }
  }
}
