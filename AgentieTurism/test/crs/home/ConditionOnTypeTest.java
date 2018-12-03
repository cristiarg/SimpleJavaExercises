package crs.home;

import crs.home.model.HolidayType;
import crs.home.model.Circuit;
import crs.home.model.Price;
import crs.home.model.Stay;
import crs.home.model.discount.ConditionOnType;
import org.junit.Test;
import org.junit.Assert;

public class ConditionOnTypeTest {
  @Test
  public void test() {
    final var sej = new Stay( "DestSejur" , new Price(0), 5 );
    final var circ = new Circuit("DestCircuit", new Price(0), 12, "Autocar");

    final var critSejur = new ConditionOnType( HolidayType.Stay );
    final var critCircuit = new ConditionOnType( HolidayType.Circuit );

    Assert.assertTrue(critSejur.isValid(sej));
    Assert.assertFalse(critSejur.isValid(circ));

    Assert.assertFalse(critCircuit.isValid(sej));
    Assert.assertTrue(critCircuit.isValid(circ));
  }
}