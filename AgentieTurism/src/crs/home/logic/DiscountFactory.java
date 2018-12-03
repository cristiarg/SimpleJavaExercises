package crs.home.logic;

import crs.home.model.Price;
import crs.home.model.HolidayType;
import crs.home.model.discount.IDiscount;
import crs.home.model.discount.ConditionOnDays;
import crs.home.model.discount.ConditionOnPrice;
import crs.home.model.discount.ConditionOnType;
import crs.home.model.discount.Discount;

import java.util.List;
import java.util.Arrays;

public class DiscountFactory {
  // for testing purposes only
  public static IDiscount getDiscountSejur0Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( HolidayType.Stay ),
            new ConditionOnPrice( new Price(0) , new Price(1000) ) ),
        0.0 );
  }

  public static IDiscount getDiscountSejur10Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( HolidayType.Stay ),
            new ConditionOnPrice( new Price(1000) ) ),
        10.0 );
  }

  // for testing purposes only
  public static IDiscount getDiscountCircuit0Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( HolidayType.Circuit ),
            new ConditionOnPrice(new Price(0), new Price(1000)) ),
        0.0 );
  }

  public static IDiscount getDiscountCircuit5Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( HolidayType.Circuit ) ,
            new ConditionOnPrice( new Price( 1000 ) ) ),
        5.0 );
  }

  public static IDiscount getDiscountCircuit15Procente() {
    return new Discount(
        Arrays.asList(
            new ConditionOnType( HolidayType.Circuit ),
            new ConditionOnPrice( new Price( 1000 ) ),
            new ConditionOnDays( 10 ) ),
        15.0 );
  }

  public static List< IDiscount > getProductionDiscountList() {
    return Arrays.asList(
        DiscountFactory.getDiscountSejur10Procente(),
        DiscountFactory.getDiscountCircuit5Procente(),
        DiscountFactory.getDiscountCircuit15Procente() );
  }
}
