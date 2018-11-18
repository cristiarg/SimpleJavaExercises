package crs.home;

import java.util.List;

/**
 * an interface that allows to implement a logic to apply none/one/some of a list
 * of discount possibilities onto an offer
 */
interface IDiscountStrategy {
  IDiscountedOffer apply( IOffer _offer , List<IDiscount> _discountList );
}

