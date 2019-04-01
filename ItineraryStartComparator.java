package mmt;

import java.util.Comparator;

public class ItineraryStartComparator implements Comparator<Itinerary>{
  @Override
  public int compare(Itinerary itinerary1, Itinerary itinerary2){
    return itinerary1.getStartTime().compareTo(itinerary2.getStartTime());
  }
}
