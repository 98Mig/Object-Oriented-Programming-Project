package mmt;

import java.util.Comparator;

public class ItineraryArrivalComparator implements Comparator<Itinerary>{
  @Override
  public int compare(Itinerary itinerary1, Itinerary itinerary2){
    return itinerary1.getArrivalTime().compareTo(itinerary2.getArrivalTime());
  }
}
