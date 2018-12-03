package crs.home;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ConsoleMenu {
  enum ActionResult {
    NotFound,
    Success,
    Failure,
    //Fatal,
    Halt
  }

  interface MenuActionable {
    ActionResult execute(String[] _arguments, Object _clientData);
    String getName();
    String getUsage();
  }

  private Map<String, Integer> nameIndexMap = new HashMap<>();
  private Map<Integer, MenuActionable> indexActionMap = new HashMap<>();

  ActionResult add(MenuActionable _action) {
    String name = _action.getName().toLowerCase();
    if (!nameIndexMap.containsKey(name)) {
      final int c = nameIndexMap.size();
      nameIndexMap.put(name, c);
      indexActionMap.put(c, _action);
      return ActionResult.Success;
    } else {
      System.out.println( "Cannot add menu action '" + name + "'. It already exists." );
      return ActionResult.Failure;
    }
  }

  void display() {
    for(final int index : indexActionMap.keySet()) {
      MenuActionable action = indexActionMap.get(index);
      System.out.println( Integer.toString(index + 1) + "\t" +
          action.getName() + " " + action.getUsage());
    }
    System.out.print("> ");
  }

  private int decodeCommand(String _commandOrPartOfCommand) {
    int index = -1;
    String commLower = _commandOrPartOfCommand.toLowerCase();
    boolean ambiguous = false;
    for(final var commandName : nameIndexMap.keySet()) {
      if (commandName.toString().startsWith( commLower )) {
        if (index == -1) {
          index = nameIndexMap.get(commandName);
        } else {
          ambiguous = true;
          break;
        }
      }
    }

    if ( index == -1 ) {
      System.out.println( "Menu action '" + _commandOrPartOfCommand + "' was not found." );
    } else if(ambiguous) {
      System.out.println( "Menu action '" + _commandOrPartOfCommand + "' is ambiguous." );
      return -1;
    }
    return index;
  }

  ActionResult execute(String _readLine, Object _clientData) {
    final String[] tokenArray = _readLine.split("\\s+");
    if (tokenArray.length > 0) {
      final int menuIndex = decodeCommand( (tokenArray[0]) );
      if (menuIndex != -1) {
        MenuActionable action = indexActionMap.get(menuIndex);
        String[] argumentArr = Arrays.copyOfRange(tokenArray, 1, tokenArray.length);
        return action.execute(argumentArr, _clientData);
      } else {
        // either not found or ambiguous
        return ActionResult.NotFound;
      }
    } else {
      System.out.println( "ERROR: empty command" );
      return ActionResult.NotFound;
    }
  }
}
