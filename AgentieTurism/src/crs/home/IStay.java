package crs.home;

// TODO: extract a new interface IDiscountable
interface IStay extends IOffer {
  @Override
  default EType getType() {
    return EType.Stay;
  }

  String getDestination();
  IPrice getPrice();
  int getDays();
}
