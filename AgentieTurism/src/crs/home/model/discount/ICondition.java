package crs.home.model.discount;

import crs.home.model.IOffer;

public interface ICondition {
  boolean isValid( IOffer _offer);
}


