package mmt;

import java.util.Comparator;

public class DepartureComparator implements Comparator<Service>{
  @Override
  public int compare(Service service1, Service service2){
    return service1.getDepartureStation().getDepartureTime().compareTo(service2.getDepartureStation().getDepartureTime());
  }
}
