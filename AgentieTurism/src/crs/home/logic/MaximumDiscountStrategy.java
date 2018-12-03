package crs.home.logic;

import crs.home.model.discount.IDiscount;
import crs.home.model.discount.IDiscountedOffer;
import crs.home.model.IOffer;
import crs.home.model.discount.DiscountedOffer;

import java.util.List;

/**
 * discount strategy that aims for the greatest
 * reduction in price for an offer
 */
public class MaximumDiscountStrategy implements IDiscountStrategy {
  private List< IDiscount > discountList;

  public MaximumDiscountStrategy(List< IDiscount > _discountList) {
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

