package mmt;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class Itinerary implements Serializable, Comparable<Itinerary>{

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  private LocalDate _date;
  private double _itineraryPrice;
  private Duration _duration;
  private List<Service> _services = new ArrayList<Service>();
  private List<Departure> _itineraryDepartures = new ArrayList<Departure>();

  /** Itinerary Construtor
   * @param date
   */
  public Itinerary(LocalDate date){
    _date = date;
    _duration = Duration.parse("PT0H0M");
  }

  public void addService(Service service){
    _services.add(service);
  }

  public void addDeparture(Departure departure){
    _itineraryDepartures.add(departure);
  }

  public void setItineraryPrice(){
    double price = 0;
    for(Service service : _services){
      for(int i = 0; i<_itineraryDepartures.size()-1 ; i+=2){
        if(service.containsDeparture(_itineraryDepartures.get(i)))
          price += service.segmentPrice(_itineraryDepartures.get(i), _itineraryDepartures.get(i+1));
      }
    }
    _itineraryPrice = price;
  }

  public double getItineraryPrice(){
    return _itineraryPrice;
  }

  public LocalDate getItineraryDate(){
    return _date;
  }

  public void setDuration(){
    Duration totalDuration = Duration.parse("PT0H0M");
    Duration betweenDepartures = Duration.parse("PT0H0M");
    for(int i=0;i<_itineraryDepartures.size()-1;i++){
      betweenDepartures = betweenDepartures.between(_itineraryDepartures.get(i).getDepartureTime(), _itineraryDepartures.get(i+1).getDepartureTime());
      totalDuration = totalDuration.plus(betweenDepartures);
    }
    _duration = totalDuration;
  }

  public Duration getItineraryDuration(){
    return _duration;
  }

  public LocalTime getStartTime(){
    return _itineraryDepartures.get(0).getDepartureTime();
  }

  public LocalTime getArrivalTime(){
    return _itineraryDepartures.get(_itineraryDepartures.size()-1).getDepartureTime();
  }

  @Override
  public int compareTo(Itinerary itinerary){
    return _date.compareTo(itinerary.getItineraryDate());
  }

  public boolean containsService(Service service){
    return _services.contains(service);
  }

  public String stringToItinerary(int i){
    return "\nItinerário " + i + " para " + _date + " @ " + String.format("%.2f", _itineraryPrice);
  }

  @Override
  public String toString(){
    String printItinerary = "";
    int contadorServicos = 0;
    boolean continues = false;
    for(int i = 0; i<_itineraryDepartures.size() - 1; i+=2){
      Service service = _services.get(contadorServicos);
      Departure start = _itineraryDepartures.get(i);
      Departure arrival = _itineraryDepartures.get(i+1);
      printItinerary += "Serviço #" + service.getServiceID() + " @ " + String.format("%.2f", service.segmentPrice(start, arrival)) + "\n";
      List<Departure> departures = service.getDepartures();
      for(Departure departure : departures){
        if(_itineraryDepartures.get(i).equals(departure)){
          continues = true;
        }
        if(continues){
          printItinerary += departure.toString();
        }
        if(_itineraryDepartures.get(i+1).equals(departure)){
          continues = false;
        }
      }
      contadorServicos++;
    }
    return printItinerary.trim();
  }

}
