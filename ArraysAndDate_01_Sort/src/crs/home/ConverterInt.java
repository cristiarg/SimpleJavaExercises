package crs.home;

// int specialization
class ConverterInt implements Converter<Integer> {
  public Integer fromString(String s) throws NumberFormatException {
    int res = Integer.parseInt(s);
    return res;
  }
  public String toString(Integer v) {
    return v.toString();
  }
}
