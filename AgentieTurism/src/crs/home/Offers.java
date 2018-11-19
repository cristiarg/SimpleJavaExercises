package crs.home;

import java.util.ArrayList;
import java.util.List;

class OffersSettings {
  static final int MAX_OFFER_COUNT = 10;
}

class TooManyOffersException extends Exception {
  TooManyOffersException(String _message) {
    super(_message);
  }
}

class Offers {
  private List<IDiscountedOffer> offerList = new ArrayList<>();
  private MaximumDiscountStrategy discountStrategy = new MaximumDiscountStrategy();
    // TODO: discount strategy should be supplied from afar
  private List<IDiscount> discountList = DiscountFactory.getProductionDiscountList();
    // TODO: same for this

  void add(IOffer _offer) throws TooManyOffersException {
    if (offerList.size() >= OffersSettings.MAX_OFFER_COUNT) {
      throw new TooManyOffersException( "Maximum number of offers exceeded." );
    }
    final var discountedOffer = discountStrategy.apply(_offer,  discountList);
    offerList.add( discountedOffer );
  }

  void displayOffers() {
    for ( final var discountedOffer : offerList ) {
      System.out.println( discountedOffer.representation() );
    }

    if (offerList.size() == 0 ) {
      System.out.println( "<< none >>" );
    }
  }

  private void displayOffers(EType _type) {
    int count = 0;
    for ( final var discountedOffer : offerList ) {
      if ( discountedOffer.getOffer().getType() == _type ) {
        ++count;
        System.out.println( discountedOffer.representation() );
      }
    }
    if(count == 0 ) {
      System.out.println( "<< none >>" );
    }
  }

  void displayStays() {
    displayOffers(EType.Stay);
  }

  void displayCircuits() {
    displayOffers(EType.Circuit);
  }

  void deleteOffer(String _destination) {
  }
}
