package crs.home.model;

// TODO: extract a new interface IDiscountable
public interface IStay extends IOffer {
  @Override
  default HolidayType getType() {
    return HolidayType.Stay;
  }

  @Override
  default String getName() {
    return getDestination();
  }

  String getDestination();
  IPrice getPrice();
  int getDays();
}
