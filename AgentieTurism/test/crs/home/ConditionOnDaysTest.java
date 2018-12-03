package crs.home;

import crs.home.model.Circuit;
import crs.home.model.Price;
import crs.home.model.Stay;
import crs.home.model.discount.ConditionOnDays;
import org.junit.Test;
import org.junit.Assert;

public class ConditionOnDaysTest {
  @Test
  public void test() {
    final var sejur10Zile = new Stay( "Dest1" , new Price( 0 ) , 10 );
    final var sejur11Zile = new Stay( "Dest1" , new Price( 1001 ) , 11 );

    final var circuit10Zile = new Circuit("DestCircuit", new Price(1), 10, "Autocar");
    final var circuit11Zile = new Circuit("DestCircuit", new Price(1), 11, "Tramvai");

    final var crit_0_10 = new ConditionOnDays( 0 , 10 );
    final var crit_11_MAX = new ConditionOnDays( 10 );

    Assert.assertTrue(crit_0_10.isValid( sejur10Zile ) );
    Assert.assertFalse(crit_0_10.isValid( sejur11Zile ) );
    Assert.assertTrue(crit_0_10.isValid( circuit10Zile ) );
    Assert.assertFalse( crit_0_10.isValid( circuit11Zile ) );

    Assert.assertFalse(crit_11_MAX.isValid( sejur10Zile ) );
    Assert.assertTrue(crit_11_MAX.isValid( sejur11Zile ) );
    Assert.assertFalse( crit_11_MAX.isValid( circuit10Zile ) );
    Assert.assertTrue( crit_11_MAX.isValid( circuit11Zile ) );
  }
}