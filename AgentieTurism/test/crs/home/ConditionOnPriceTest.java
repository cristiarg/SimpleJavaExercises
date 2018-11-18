package crs.home;

import org.junit.Test;
import org.junit.Assert;

public class ConditionOnPriceTest {
  @Test
  public void test() {
    final var sej1 = new Stay( "Dest1" , new Price( 1 ) , 5 );
    final var sej1000 = new Stay( "Dest1" , new Price( 1000 ) , 5 );
    final var sej1001p000001 = new Stay( "Dest1" , new Price( 1001.000001 ) , 5 );
    // TODO: Circuit

    final var crit_0_1000 = new ConditionOnPrice( new Price( 0 ) , new Price( 1000 ) );
    final var crit_1001_MAX = new ConditionOnPrice( new Price( 1001 ) );

    Assert.assertTrue(crit_0_1000.isValid( sej1 ) );
    Assert.assertTrue(crit_0_1000.isValid( sej1000 ) );
    Assert.assertFalse(crit_0_1000.isValid( sej1001p000001 ) );

    Assert.assertFalse(crit_1001_MAX.isValid( sej1 ) );
    Assert.assertFalse(crit_1001_MAX.isValid( sej1000 ) );
    Assert.assertTrue(crit_1001_MAX.isValid( sej1001p000001 ) );
  }
}