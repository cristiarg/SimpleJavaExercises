package crs.home;

import java.util.List;
import java.util.Arrays;

import org.junit.Test;
import org.junit.Assert;

public class MaximumDiscountStrategyTest {
  @Test
  public void testSejur() {
    List< IDiscount > discountListSejur = Arrays.asList(
        DiscountFactory.getDiscountSejur0Procente(),
        DiscountFactory.getDiscountSejur10Procente() );
    MaximumDiscountStrategy mds = new MaximumDiscountStrategy(discountListSejur);

    final var sejurCuDiscount0 = new Stay( "Dest1" , new Price( 999 ) , 5 );
    final var sejurCuDiscount10Procente = new Stay( "Dest1" , new Price( 1001 ) , 5 );

    {
      final var ofertaCuDiscount = mds.apply( sejurCuDiscount0 );
      Assert.assertTrue( ofertaCuDiscount.discounted() );
      final var gross = ofertaCuDiscount.getGrossValue();
      final var disc = ofertaCuDiscount.getDiscountValue();
      final var netto = ofertaCuDiscount.getNettoValue();
      Assert.assertEquals( gross.compareTo( netto ) , 0 );
      Assert.assertEquals( disc.compareTo( Price.ZERO() ) , 0 );
    }

    {
      final var ofertaCuDiscount = mds.apply( sejurCuDiscount10Procente );
      Assert.assertTrue( ofertaCuDiscount.discounted() );
      final var gross = ofertaCuDiscount.getGrossValue();
      final var disc = ofertaCuDiscount.getDiscountValue();
      final var netto = ofertaCuDiscount.getNettoValue();
      Assert.assertEquals( gross.compareTo( netto ) , 1 );
      Assert.assertEquals( disc.compareTo( Price.ZERO() ) , 1 );
    }
  }

  @Test
  public void testSejurNoZeroDiscount() {
    List< IDiscount > discountListSejur = Arrays.asList(
        DiscountFactory.getDiscountSejur10Procente() );
    MaximumDiscountStrategy mds = new MaximumDiscountStrategy(discountListSejur);

    final var sejurCuDiscount0 = new Stay( "Dest1" , new Price( 999 ) , 5 );

    {
      final var ofertaCuDiscount = mds.apply( sejurCuDiscount0 );
      Assert.assertFalse( ofertaCuDiscount.discounted() );
//      final var gross = ofertaCuDiscount.getGrossValue();
//      final var disc = ofertaCuDiscount.getDiscountValue();
//      final var netto = ofertaCuDiscount.getNettoValue();
//      Assert.assertEquals( gross.compareTo( netto ) , 0 );
//      Assert.assertEquals( disc.compareTo( Price.ZERO() ) , 0 );
    }
  }

  private void testCircuitDiscount0( ICircuit _circuit, IDiscountStrategy _strategy,
                            List< IDiscount > _discountList) {
    final var ofertaCuDiscount = _strategy.apply( _circuit );
    final var gross = ofertaCuDiscount.getGrossValue();
    final var disc = ofertaCuDiscount.getDiscountValue();
    final var netto = ofertaCuDiscount.getNettoValue();
    Assert.assertEquals( gross.compareTo( netto ) , 0 );
    Assert.assertEquals( disc.compareTo( Price.ZERO() ) , 0 );
  }

  @Test
  public void testCircuit() {
    List< IDiscount > listaDiscount = Arrays.asList(
        DiscountFactory.getDiscountCircuit0Procente(),
        DiscountFactory.getDiscountCircuit5Procente(),
        DiscountFactory.getDiscountCircuit15Procente());
    final MaximumDiscountStrategy mds = new MaximumDiscountStrategy(listaDiscount);

    final var circuit1CuDiscount0 = new Circuit("DestCircuit", new Price(1000), 9, "Autocar");
    testCircuitDiscount0( circuit1CuDiscount0, mds, listaDiscount );

    final var circuit2CuDiscount0 = new Circuit("DestCircuit", new Price(1000), 11, "Autocar");
    testCircuitDiscount0( circuit2CuDiscount0, mds, listaDiscount );

    final var circuitCuDiscount5 = new Circuit("DestCircuit", new Price(2000), 9, "Autocar");
    {
      final var ofertaCuDiscount = mds.apply( circuitCuDiscount5);
      final var gross = ofertaCuDiscount.getGrossValue();
      final var disc = ofertaCuDiscount.getDiscountValue();
      final var netto = ofertaCuDiscount.getNettoValue();
      Assert.assertEquals( gross.compareTo( netto ) , 1 );
      Assert.assertEquals( disc.compareTo( Price.ZERO() ) , 1 );
      Assert.assertEquals( disc.getValue() , 100.0 , Math.ulp( 1.0 ) );
    }

    final var circuitCuDiscount15 = new Circuit("DestCircuit", new Price(2000), 11, "Autocar");
    {
      final var ofertaCuDiscount = mds.apply( circuitCuDiscount15 );
      final var gross = ofertaCuDiscount.getGrossValue();
      final var disc = ofertaCuDiscount.getDiscountValue();
      final var netto = ofertaCuDiscount.getNettoValue();
      Assert.assertEquals( gross.compareTo( netto ) , 1 );
      Assert.assertEquals( disc.compareTo( Price.ZERO() ) , 1 );
      Assert.assertEquals( disc.getValue() , 300.0 , Math.ulp( 1.0 ) );
    }
  }
}