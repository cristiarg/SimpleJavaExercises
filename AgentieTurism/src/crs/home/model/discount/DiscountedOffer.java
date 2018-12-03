package crs.home.model.discount;

import crs.home.model.HolidayType;
import crs.home.model.IOffer;
import crs.home.model.IPrice;
import crs.home.model.IStay;
import crs.home.model.Price;

// TODO: this looks too much like an workaround; should just be another implementation of IOffer/IStay
public class DiscountedOffer implements IDiscountedOffer {
  private IOffer offer;
  private IDiscount discount;
  private IStay stay; // TODO: workaround

  public DiscountedOffer( IOffer _offer , IDiscount _discount) {
    offer = _offer;
    discount = _discount;

    assert _offer instanceof IStay;
    stay = ( IStay ) _offer;
  }

  public DiscountedOffer( IOffer _offer ) {
    this(_offer, null);
  }

  private boolean discounted() {
    return (discount != null);
  }

  @Override
  public IPrice getGrossValue() {
    return stay.getPrice();
  }

  @Override
  public IPrice getDiscountValue() {
    if (discounted()) {
      return stay.getPrice().getDiscountValue(discount.getPercent());
    } else {
      return Price.ZERO();
    }
  }

  @Override
  public IPrice getNettoValue() {
    if (discounted()) {
      return stay.getPrice().getNetValue(discount.getPercent());
    } else {
      return stay.getPrice();
    }
  }

  @Override
  public HolidayType getType() {
    return offer.getType();
  }

  @Override
  public String getName() {
    return offer.getName();
  }

  @Override
  public String representation() {
    final var sb = new StringBuilder();
    sb.append("[[ ");
    sb.append(offer.representation());
    sb.append(" ]]");
    if (discounted()) {
      sb.append("\tDiscount: -");
      sb.append(discount.getPercent());
      sb.append("% (-");
      sb.append(getDiscountValue().representation());
      sb.append(")");
      sb.append("\tNet: ");
      sb.append(getNettoValue().representation());
    }
    return sb.toString();
  }
}
