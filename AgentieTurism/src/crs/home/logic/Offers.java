package crs.home.logic;

import crs.home.model.HolidayType;
import crs.home.model.discount.IDiscountedOffer;
import crs.home.model.IOffer;

import java.util.HashMap;
import java.util.Map;

public class Offers {
  private Map<String, Integer> nameKeyedIndexMap = new HashMap<>();
    // used to keep the initial added order

  private Map<Integer, IDiscountedOffer > offerMap = new HashMap<>();

  private IDiscountStrategy discountStrategy;

  public Offers(IDiscountStrategy _discountStrategy) {
    discountStrategy = _discountStrategy;
  }

  public boolean add( IOffer _offer) throws TooManyOffersException {
    if (offerMap.size() >= OffersSettings.MAX_OFFER_COUNT) {
      throw new TooManyOffersException( "Maximum number of offers exceeded." );
    }
    String name = _offer.getName();
    if (nameKeyedIndexMap.containsKey( name )) {
      return false;
    } else {
      final int index = nameKeyedIndexMap.size();
      nameKeyedIndexMap.put(name, index);
      final IDiscountedOffer discountedOffer = discountStrategy.apply(_offer);
      offerMap.put(index, discountedOffer);
      return true;
    }
  }

  public void displayOffers() {
    for ( final Integer indexKey : offerMap.keySet() ) {
      final IDiscountedOffer discountedOffer = offerMap.get(indexKey);
      System.out.println( discountedOffer.representation() );
    }

    if (offerMap.size() == 0 ) {
      System.out.println( "<< none >>" );
    }
  }

  private void displayOffers( HolidayType _type) {
    int count = 0;
    for ( final Integer indexKey : offerMap.keySet() ) {
      final IDiscountedOffer discountedOffer = offerMap.get(indexKey);
      if ( discountedOffer.getType() == _type ) {
        ++count;
        System.out.println( discountedOffer.representation() );
      }
    }
    if(count == 0 ) {
      System.out.println( "<< none >>" );
    }
  }

  public void displayStays() {
    displayOffers( HolidayType.Stay);
  }

  public void displayCircuits() {
    displayOffers( HolidayType.Circuit);
  }

  public boolean deleteOffer(String _name) {
    if( nameKeyedIndexMap.containsKey( _name )) {
      final Integer index = nameKeyedIndexMap.get(_name);
      assert offerMap.containsKey( index );

      nameKeyedIndexMap.remove( _name );
      offerMap.remove( index );
      return true;

    } else {
      return false;
    }
  }

}

