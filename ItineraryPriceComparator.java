package mmt;

import java.util.Comparator;

public class ItineraryPriceComparator implements Comparator<Itinerary>{
  @Override
  public int compare(Itinerary itinerary1, Itinerary itinerary2){
    return Double.compare(itinerary1.getItineraryPrice(), itinerary2.getItineraryPrice());
  }
}
