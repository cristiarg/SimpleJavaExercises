package crs.home;

import crs.home.console.*;
import crs.home.model.discount.IDiscount;
import crs.home.logic.IDiscountStrategy;
import crs.home.logic.DiscountFactory;
import crs.home.logic.MaximumDiscountStrategy;
import crs.home.logic.Offers;

import java.util.EnumMap;
import java.util.*;
import java.io.IOException;

class Main {
  private static Map< EMenuAction, IMenuAction > commandMap;
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

  private static void displayMenu( ConsoleHelper _console) throws IOException {
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
