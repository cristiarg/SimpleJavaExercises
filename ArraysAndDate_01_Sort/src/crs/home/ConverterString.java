package crs.home;

// nop specialization
class ConverterString implements Converter<String> {
  public String get(String s) throws NumberFormatException {
    // nop
    return s;
  }
}
