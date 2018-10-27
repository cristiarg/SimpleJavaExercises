package crs.home;

// int specialization
class ConverterInt implements Converter<Integer> {
  public Integer get(String s) throws NumberFormatException {
    int res = Integer.parseInt(s);
    return res;
  }
}
