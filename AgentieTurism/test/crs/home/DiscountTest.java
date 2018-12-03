package crs.home;

import crs.home.model.discount.IDiscount;
import crs.home.logic.DiscountFactory;
import crs.home.model.Price;
import crs.home.model.Stay;
import org.junit.Test;
import org.junit.Assert;

public class DiscountTest {
  @Test
  public void test() {
    IDiscount discountSejur0Procente = DiscountFactory.getDiscountSejur0Procente();
    IDiscount discountSejur10Procente = DiscountFactory.getDiscountSejur10Procente();

    final var sejur0Procente = new Stay( "Dest1" , new Price( 1000.0 ) , 10 );
    final var sejur10Procente = new Stay( "Dest1" , new Price( 1000.1 ) , 10 );

    Assert.assertTrue(discountSejur0Procente.isApplicableFor(sejur0Procente) );
    Assert.assertFalse(discountSejur10Procente.isApplicableFor(sejur0Procente) );

    Assert.assertFalse(discountSejur0Procente.isApplicableFor(sejur10Procente) );
    Assert.assertTrue(discountSejur10Procente.isApplicableFor(sejur10Procente) );

    // TODO: Circuit
  }
}