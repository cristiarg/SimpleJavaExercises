package crs.home;

/**
 * that allows to implement a logic to apply none/one/some of a list
 * of discount possibilities onto an offer
 */
interface IDiscountStrategy {
  IDiscountedOffer apply( IOffer _offer );
}

