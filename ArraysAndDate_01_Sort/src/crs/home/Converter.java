package crs.home;

// sort-of generic interface
interface Converter<T> {
  T get(String s) throws NumberFormatException;
}
