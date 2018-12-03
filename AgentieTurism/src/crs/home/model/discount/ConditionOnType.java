package crs.home.model.discount;

import crs.home.model.HolidayType;
import crs.home.model.IOffer;

public class ConditionOnType implements ICondition {
  private HolidayType tip;

  public ConditionOnType( HolidayType _tip) {
    tip = _tip;
  }

  @Override
  public boolean isValid( IOffer _offer ) {
    final var t = _offer.getType();
    return (t == tip);
  }
}


