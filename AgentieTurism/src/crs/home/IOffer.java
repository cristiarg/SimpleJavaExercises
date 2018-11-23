package crs.home;

/**
 * TODO: interface definition is too thin
 */
interface IOffer {
  EType getType();

  String getName();

  String representation();
    // TODO: there should be another class [hierarchy] that performs this adaptation
}

