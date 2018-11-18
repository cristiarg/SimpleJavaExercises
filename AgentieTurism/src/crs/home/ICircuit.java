package crs.home;

interface ICircuit extends IStay {
  @Override
  default EType getType() {
    return EType.Circuit;
  }

  String getTransport();
}
