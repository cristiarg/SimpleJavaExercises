package crs.home;

import java.util.Arrays;

class TestUtils {
  static IDiscount getDiscountSejur0Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( EType.Stay ),
            new ConditionOnPrice( new Price(0) , new Price(1000) ) ),
        0.0 );
  }

  static IDiscount getDiscountSejur10Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( EType.Stay ),
            new ConditionOnPrice( new Price(1000) ) ),
        10.0 );
  }

  static IDiscount getDiscountCircuit0Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( EType.Circuit ),
            new ConditionOnPrice(new Price(0), new Price(1000)) ),
        0.0 );
  }

  static IDiscount getDiscountCircuit5Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( EType.Circuit ) ,
            new ConditionOnPrice( new Price( 1000 ) ) ),
        5.0 );
  }

  static IDiscount getDiscountCircuit15Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( EType.Circuit ),
            new ConditionOnPrice( new Price( 1000 ) ),
            new ConditionOnDays( 10 ) ),
        15.0 );
  }

}
