package crs.home;

import org.junit.Test;
import org.junit.Assert;

public class ConditionOnTypeTest {
  @Test
  public void test() {
    final var sej = new Stay( "DestSejur" , new Price(0), 5 );
    final var circ = new Circuit("DestCircuit", new Price(0), 12, "Autocar");

    final var critSejur = new ConditionOnType( EType.Stay );
    final var critCircuit = new ConditionOnType( EType.Circuit );

    Assert.assertTrue(critSejur.isValid(sej));
    Assert.assertFalse(critSejur.isValid(circ));

    Assert.assertFalse(critCircuit.isValid(sej));
    Assert.assertTrue(critCircuit.isValid(circ));
  }
}