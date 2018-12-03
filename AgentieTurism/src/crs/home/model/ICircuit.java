package crs.home.model;

public interface ICircuit extends IStay {
  @Override
  default HolidayType getType() {
    return HolidayType.Circuit;
  }

  String getTransport();
}
