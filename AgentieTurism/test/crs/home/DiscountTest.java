package crs.home;

import org.junit.Test;
import org.junit.Assert;

public class DiscountTest {
  @Test
  public void test() {
    IDiscount discountSejur0Procente = TestUtils.getDiscountSejur0Procente();
    IDiscount discountSejur10Procente = TestUtils.getDiscountSejur10Procente();

    final var sejur0Procente = new Stay( "Dest1" , new Price( 1000.0 ) , 10 );
    final var sejur10Procente = new Stay( "Dest1" , new Price( 1000.1 ) , 10 );

    Assert.assertTrue(discountSejur0Procente.isApplicableFor(sejur0Procente) );
    Assert.assertFalse(discountSejur10Procente.isApplicableFor(sejur0Procente) );

    Assert.assertFalse(discountSejur0Procente.isApplicableFor(sejur10Procente) );
    Assert.assertTrue(discountSejur10Procente.isApplicableFor(sejur10Procente) );

    // TODO: Circuit
  }
}