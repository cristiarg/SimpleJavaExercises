package crs.home;

// TODO: or inherit from IOffer>
interface IDiscountedOffer {
  IOffer getOffer();

  IPrice getGrossValue();
  IPrice getDiscountValue();
  IPrice getNettoValue();
}

