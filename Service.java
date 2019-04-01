package mmt;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalTime;
import java.time.Duration;
import java.io.Serializable;

public class Service implements Serializable{

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  private int _serviceID;
  private double _servicePrice;
  private List<Departure> _serviceDepartures = new ArrayList<Departure>();

  /** Service Construtor
   * @param serviceID
   * @param servicePrice
   */
  public Service(int serviceID, double servicePrice){
    _serviceID = serviceID;
    _servicePrice = servicePrice;
  }

  public void setServiceID(int serviceID){
    _serviceID = serviceID;
  }

  public int getServiceID(){
    return _serviceID;
  }

  public void setServicePrice(double servicePrice){
    _servicePrice = servicePrice;
  }

  public double getServicePrice(){
    return _servicePrice;
  }

  public List<Departure> getDepartures(){
    return _serviceDepartures;
  }

  public Departure getDeparture(int index){
    return _serviceDepartures.get(index);
  }

  public Departure getDepartureStation(){
    return getDeparture(0);
  }

  public Departure getArrivalStation(){
    return getDeparture(_serviceDepartures.size() - 1);
  }

  public String getNextStation(String station){
    String nextStation = "";
    for(int i = 0; i< _serviceDepartures.size()-1; i++){
      Departure departure = _serviceDepartures.get(i);
      if(departure.getStation().equals(station)){
        nextStation = _serviceDepartures.get(i+1).getStation();
      }
    }
    return nextStation;
  }

  public int getNumberOfDepartures(){
    return _serviceDepartures.size();
  }

  public boolean containsDeparture(Departure departure){
    return _serviceDepartures.contains(departure);
  }

  public int numberofStations(){
    return _serviceDepartures.size();
  }

  public Duration totalServiceDuration(){
    Duration duration = Duration.between(getDepartureStation().getDepartureTime(), getArrivalStation().getDepartureTime());
    return duration;
  }

  public Duration totalSegmentDuration(Departure departure1, Departure departure2){
    Duration duration = Duration.between(departure1.getDepartureTime(), departure2.getDepartureTime());
    return duration;
  }

  public boolean checksFinalStation(String station){
    if(getArrivalStation().getStation().equals(station)){
      return true;
    }
    return false;
  }

  public double segmentPrice(Departure departure1, Departure departure2){
    Duration totalDuration = totalServiceDuration();
    Duration durationSegment = totalSegmentDuration(departure1, departure2);
    long minutesTotal = totalDuration.toMinutes();
    long minutesSegment = durationSegment.toMinutes();
    return (_servicePrice * minutesSegment)/minutesTotal;
  }

  public void addDeparture(Departure departure){
    _serviceDepartures.add(departure);
  }

  public boolean containsDeparture(String station){
    boolean contains = false;
    for(Departure departure : _serviceDepartures){
      if(departure.getStation().equals(station)){
        contains = true;
      }
    }
    return contains;
  }

  public int getIndexDeparture(String station){
    int i = 0;
    for(Departure departure : _serviceDepartures){
      if(departure.getStation().equals(station)){
        i = _serviceDepartures.indexOf(departure);
      }
    }
    return i;
  }

  public LocalTime getTime(String station){
    LocalTime time = LocalTime.of(0,0);
    for(Departure departure : _serviceDepartures){
      if(departure.getStation().equals(station)){
        time = departure.getDepartureTime();
      }
    }
    return time;
  }

  public int getIndexOf(Departure departure){
    return _serviceDepartures.indexOf(departure);
  }

  @Override
  @SuppressWarnings("nls")
  public String toString(){
    String printService =  "Serviço #"+_serviceID+" @ "+String.format("%.2f", _servicePrice)+"\n";
    for(Departure departure : _serviceDepartures){
      printService += departure.toString();
    }
    return printService.trim();
  }
}
