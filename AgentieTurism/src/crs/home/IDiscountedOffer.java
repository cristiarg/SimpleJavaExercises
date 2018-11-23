package crs.home;

// TODO: or inherit from IOffer>
interface IDiscountedOffer extends IOffer {
  IPrice getGrossValue();
  IPrice getDiscountValue();
  IPrice getNettoValue();
}

