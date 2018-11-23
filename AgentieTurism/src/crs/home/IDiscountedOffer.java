package crs.home;

// TODO: or inherit from IOffer>
interface IDiscountedOffer extends IOffer {
  boolean discounted();

  IPrice getGrossValue();
  IPrice getDiscountValue();
  IPrice getNettoValue();

  String representation();
}

