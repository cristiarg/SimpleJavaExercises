package crs.home;

/**
 * an interface to specify a percent discounted value and whether
 * or not it applies to a certain offer;
 */
interface IDiscount {
  double getPercent();
  boolean isApplicableFor( IOffer _offer );
}

// TODO: shortcoming: a discount should be applicable to other aspects
// of an offer beside the price (e.g.: extra days, ..) but this is not
// possible in the current structure
