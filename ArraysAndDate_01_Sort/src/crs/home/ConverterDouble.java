package crs.home;

// double floating specialization
class ConverterDouble implements Converter<Double> {
  @Override
  public Double fromString(String s) throws NumberFormatException {
    double res = Double.parseDouble(s);
    return res;
  }

  @Override
  public String toString(Double v) {
    return v.toString();
  }
}
