package crs.home;

// TODO: this looks too much like an workaround; should just be another implementation of IOffer/IStay
class DiscountedOffer implements IDiscountedOffer {
  private IOffer offer;
  private IDiscount discount;
  private IStay stay; // TODO: workaround

  DiscountedOffer( IOffer _offer , IDiscount _discount) {
    offer = _offer;
    discount = _discount;

    assert _offer instanceof IStay;
    stay = ( IStay ) _offer;
  }

  @Override
  public IOffer getOffer() {
    return offer;
  }

  @Override
  public IPrice getGrossValue() {
    return stay.getPrice();
  }

  @Override
  public IPrice getDiscountValue() {
    return stay.getPrice().getDiscountValue(discount.getPercent());
    // TODO: caching?
  }

  @Override
  public IPrice getNettoValue() {
    return stay.getPrice().getNetValue(discount.getPercent());
  }
}
