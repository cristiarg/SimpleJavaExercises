package crs.home;

final class ConditionOnDays implements ICondition {
  private int min;
  private int max;

  ConditionOnDays( int _min, int _max) {
    min = _min;
    max = _max;
  }

  ConditionOnDays( int _min) {
    this(_min, Integer.MAX_VALUE);
  }

  @Override
  public boolean isValid( IOffer _offer ) {
    if ( _offer instanceof IStay ) {
      final var d = (( IStay ) _offer ).getDays();
      final var greaterThanOrEqualToMin = (min < d);
      final var lessThanMax = (d <= max);
      return (greaterThanOrEqualToMin && lessThanMax);
    }
    return false; // TODO: maybe throw?
  }
}


