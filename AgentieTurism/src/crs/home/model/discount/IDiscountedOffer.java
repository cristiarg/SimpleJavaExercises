package crs.home.model.discount;

import crs.home.model.IOffer;
import crs.home.model.IPrice;

// TODO: or inherit from IOffer>
public interface IDiscountedOffer extends IOffer {
  IPrice getGrossValue();
  IPrice getDiscountValue();
  IPrice getNettoValue();
}

