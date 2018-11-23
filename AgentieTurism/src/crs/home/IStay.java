package crs.home;

// TODO: extract a new interface IDiscountable
interface IStay extends IOffer {
  @Override
  default EType getType() {
    return EType.Stay;
  }

  @Override
  default String getName() {
    return getDestination();
  }

  String getDestination();
  IPrice getPrice();
  int getDays();
}
