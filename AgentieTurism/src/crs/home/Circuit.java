package crs.home;

class Circuit implements ICircuit {
  private String destination;
  private IPrice price;
  private int days;
  private String transport;

  Circuit( String _destination , IPrice _price , int _days , String _transport) {
    destination = _destination;
    price = _price;
    days = _days;
    transport = _transport;
  }

  @Override
  public String getDestination() {
    return destination;
  }

  @Override
  public IPrice getPrice() {
    return price;
  }

  @Override
  public int getDays() {
    return days;
  }

  @Override
  public String getTransport() {
    return transport;
  }

}
