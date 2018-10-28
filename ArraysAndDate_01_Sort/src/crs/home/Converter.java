package crs.home;

// sort-of generic interface
interface Converter<T> {
  T fromString(String s) throws NumberFormatException;
  String toString(T v);
}
