package crs.home;

import java.util.List;
import java.util.Arrays;

class DiscountFactory {
  // for testing purposes only
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

  // for testing purposes only
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

  static List<IDiscount> getProductionDiscountList() {
    return Arrays.asList(
        DiscountFactory.getDiscountSejur10Procente(),
        DiscountFactory.getDiscountCircuit5Procente(),
        DiscountFactory.getDiscountCircuit15Procente() );
  }
}
