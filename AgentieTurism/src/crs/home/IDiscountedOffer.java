package crs.home;

// TODO: or inherit from IOffer>
interface IDiscountedOffer {
  IOffer getOffer();

  boolean discounted();

  IPrice getGrossValue();
  IPrice getDiscountValue();
  IPrice getNettoValue();

  String representation();
}

