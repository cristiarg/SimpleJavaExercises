package crs.home;

class Price implements IPrice {
  private double value;
  Price( double _value ) {
    assert _value >= 0;

    value = _value;
  }

  @Override
  public double getValue() {
    return value;
  }

  @Override
  public IPrice getDiscountValue( double _percent) {
    final var ratio = _percent / 100.0;
    return new Price( getValue() * ratio );
  }

  @Override
  public IPrice getNetValue( double _percent) {
    return new Price( getValue() - getDiscountValue(_percent).getValue());
  }

  static IPrice ZERO() {
    return new Price( 0.0 );
  }

  static IPrice MAX() {
    return new Price(Double.MAX_VALUE);
  }

  @Override
  public boolean equals(Object _that) {
    if ( _that == null ) {
      return false;
    } else if (_that instanceof IPrice) {
      return compareTo( (IPrice)_that ) == 0;
    } else {
      return super.equals( _that );
    }
  }

//  @Override
//  public boolean equals(Object _obj) {
//    if (_obj == null){
//      return false;
//    } else {
//      final var p = (Price)_obj;
//      return getValue() == p.getValue();
//    }
//  }
//
//  @Override
//  int hashCode();
}