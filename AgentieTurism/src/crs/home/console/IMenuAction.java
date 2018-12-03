package crs.home.console;

import crs.home.console.EMenuActionResult;
import crs.home.logic.Offers;

public interface IMenuAction {
  EMenuActionResult act( String[] _params , Offers _offers );
  String getUsage();
}
