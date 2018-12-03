package crs.home.logic;

import crs.home.model.IOffer;
import crs.home.model.discount.IDiscountedOffer;

/**
 * that allows to implement a logic to apply none/one/some of a list
 * of discount possibilities onto an offer
 */
public interface IDiscountStrategy {
  IDiscountedOffer apply( IOffer _offer );
}

