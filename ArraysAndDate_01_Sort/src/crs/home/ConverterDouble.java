package crs.home;

// double floating specialization
class ConverterDouble implements Converter<Double> {
  public Double get(String s) throws NumberFormatException {
    double res = Double.parseDouble(s);
    return res;
  }
}
