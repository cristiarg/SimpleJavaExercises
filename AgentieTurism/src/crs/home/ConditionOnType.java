package crs.home;

class ConditionOnType implements ICondition {
  private EType tip;

  ConditionOnType( EType _tip) {
    tip = _tip;
  }

  @Override
  public boolean isValid( IOffer _offer ) {
    final var t = _offer.getType();
    return (t == tip);
  }
}


