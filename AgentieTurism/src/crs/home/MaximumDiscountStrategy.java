package crs.home;

import java.util.List;

/**
 * discount strategy that aims for the greatest
 * reduction in price for an offer
 */
class MaximumDiscountStrategy implements IDiscountStrategy {
  @Override
  public IDiscountedOffer apply( IOffer _offer , List<IDiscount> _discountList ) {
    IDiscount maximumDiscount = null;
    for(IDiscount d : _discountList ) {
      if(d.isApplicableFor( _offer )) {
        if (maximumDiscount == null) {
          maximumDiscount = d;
        } else if (d.getPercent() > maximumDiscount.getPercent()) {
          maximumDiscount = d;
        }
      }
    }
    if (maximumDiscount != null) {
      return new DiscountedOffer( _offer , maximumDiscount );
    }
    return null;
      // TODO: or return the same offer if no discount?
  }
}

