package mmt;

import java.util.Comparator;

public class ArrivalComparator implements Comparator<Service>{
  @Override
  public int compare(Service service1, Service service2){
    return service1.getArrivalStation().getDepartureTime().compareTo(service2.getArrivalStation().getDepartureTime());
  }
}
