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
  EMenuActionResult act(String _inputLine, Offers _offers);
}

class MenuActionAddStay implements IMenuAction {
  @Override
  public EMenuActionResult act( String _inputLine , Offers _offers ) {
    System.out.println( "Excuting 'addStay'" );
    return EMenuActionResult.Success;
  }
}

class MenuActionAddCircuit implements IMenuAction {
  @Override
  public EMenuActionResult act( String _inputLine , Offers _offers ) {
    System.out.println( "Excuting 'addCircuit'" );
    return EMenuActionResult.Success;
  }
}

class MenuActionListAll implements IMenuAction {
  @Override
  public EMenuActionResult act( String _inputLine , Offers _offers ) {
    System.out.println( "Excuting 'listAll'" );
    return EMenuActionResult.Success;
  }
}

class MenuActionListStays implements IMenuAction {
  @Override
  public EMenuActionResult act( String _inputLine , Offers _offers ) {
    System.out.println( "Excuting 'listStays'" );
    return EMenuActionResult.Success;
  }
}

class MenuActionListCircuits implements IMenuAction {
  @Override
  public EMenuActionResult act( String _inputLine , Offers _offers ) {
    System.out.println( "Excuting 'listCircuits'" );
    return EMenuActionResult.Success;
  }
}

class MenuActionDelete implements IMenuAction {
  @Override
  public EMenuActionResult act( String _inputLine , Offers _offers ) {
    System.out.println( "Excuting 'delete'" );
    return EMenuActionResult.Success;
  }
}

class MenuActionExit implements IMenuAction {
  @Override
  public EMenuActionResult act( String _inputLine , Offers _offers ) {
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
      final var res = menuAction.act(_line, _offers);
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
    }
  }
}
