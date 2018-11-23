package crs.home;

import java.util.List;

/**
 * discount strategy that aims for the greatest
 * reduction in price for an offer
 */
class MaximumDiscountStrategy implements IDiscountStrategy {
  private List< IDiscount > discountList;

  MaximumDiscountStrategy(List< IDiscount > _discountList) {
    discountList = _discountList;
  }

  @Override
  public IDiscountedOffer apply( IOffer _offer ) {
    IDiscount maximumDiscount = null;
    for( IDiscount d : discountList ) {
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
    return new DiscountedOffer(_offer);
  }
}

