package crs.home;

// TODO: two implementations: minimum price AND price interval

final class ConditionOnPrice implements ICondition {
  private IPrice min;
  private IPrice max;

  ConditionOnPrice( IPrice _min, IPrice _max) {
    assert _min.compareTo(_max) <= 0;
    min = _min;
    max = _max;
  }

  ConditionOnPrice( IPrice _min) {
    this(_min, Price.MAX());
  }

  @Override
  public boolean isValid( IOffer _offer ) {
    if ( _offer instanceof IStay ) {
      final var p = (( IStay ) _offer ).getPrice();

      final var greaterOrEqualThanMin = (min.compareTo( p ) < 0);
      final var lessThanMax = (p.compareTo( max ) <= 0);
      return (greaterOrEqualThanMin && lessThanMax);
    }
    return false;
  }
}

