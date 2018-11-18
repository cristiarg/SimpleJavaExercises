package crs.home;

public class Stay implements IStay {
  private String destination;
  private IPrice price;
  private int days;

  Stay( String _destination , IPrice _price , int _days ) {
    destination = _destination;
    price = _price;
    days = _days;
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

}
