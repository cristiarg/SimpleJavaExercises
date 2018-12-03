package crs.home.model;

/**
 * TODO: interface definition is too thin
 */
public interface IOffer {
  HolidayType getType();

  String getName();

  String representation();
    // TODO: there should be another class [hierarchy] that performs this adaptation
}

