package crs.home;

// nop specialization
class ConverterString implements Converter<String> {
  public String fromString(String s) throws NumberFormatException {
    // silly .. but no
    return s;
  }
  public String toString(String v) {
    // silly .. but no
    return v;
  }
}
