package crs.home.model.discount;

import crs.home.model.IOffer;

import java.util.List;

public class Discount implements IDiscount {
  private List< ICondition > listCondition;
    // TODO: also add interval; bounded or unbounded
  private double percent;
    // TODO: maybe more formalization for this?

  public Discount( List< ICondition > _lc, double _percent ) {
    assert _lc != null && _lc.size() > 0;
    assert 0.0 <= _percent && _percent <= 100.0;

    listCondition = _lc;
    percent = _percent;
  }

  @Override
  public double getPercent() {
    return percent;
  }

  @Override
  public boolean isApplicableFor( IOffer _offer ) {
    boolean v = true;
    for ( int i = 0 ; v && i < listCondition.size() ; i++ ) {
      ICondition c = listCondition.get(i);
      v = v && c.isValid( _offer );
    }
    return v;
  }

//  @Override
//  public IPrice getNetValue( IOffer _o ) {
//    return null;
//  }

//  @Override
//  public IPrice getDiscountValue( IOffer _o ) {
//    boolean satisface = true;
//    for ( int i = 0 ; satisface && i < listCondition.size() ; i++ ) {
//      ICondition c = listCondition.get(i);
//      satisface = satisface && c.isValid( _o );
//    }
//
//    if ( satisface ) {
//      IStay s = (Stay)_o;
//      return s.getPrice().getDiscountValue(percent);
//    } else {
//
//    }
//  }
}
