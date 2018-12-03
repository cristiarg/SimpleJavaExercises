package crs.home.model;

public interface IPrice extends Comparable< IPrice > {
  double getValue();

  IPrice getDiscountValue( double _percent);
  IPrice getNetValue( double _percent);

  String representation();

  @Override
  default int compareTo( IPrice _that) {
    if ( _that == null ) {
      return 1; // TODO: can null be considered 'less' than any instance?
    } else {
      return Double.compare( this.getValue() , _that.getValue() );
    }
  }
}
