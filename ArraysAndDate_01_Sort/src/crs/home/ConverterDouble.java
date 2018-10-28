package crs.home;

// double floating specialization
class ConverterDouble implements Converter<Double> {
  public Double fromString(String s) throws NumberFormatException {
    double res = Double.parseDouble(s);
    return res;
  }
  public String toString(Double v) {
    return v.toString();
  }
}
