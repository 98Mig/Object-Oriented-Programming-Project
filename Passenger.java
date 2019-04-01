package mmt;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class Passenger implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  private int _passengerID;
  private String _passengerName;
  private CategoryType _category;
  private Duration _totalTime;
  private int _numberOfItineraries;
  private double _paidValue;
  private List<Itinerary> _itineraries = new ArrayList<Itinerary>();

  /** Passenger Construtor
   * @param id
   * @param name
   * @param category
   */
  public Passenger(int id, String name){
    _passengerID = id;
    _passengerName = name;
    _category = new NormalCategory(this);
    _numberOfItineraries = 0;
    _totalTime = Duration.parse("PT0H0M");
    _paidValue = 0;
  }

  public void setName(String name){
    _passengerName = name;
  }

  public void setCategory(CategoryType category){
    _category = category;
  }

  public int getID(){
    return _passengerID;
  }

  public String getName(){
    return _passengerName;
  }

  public CategoryType getCategory(){
    return _category;
  }

  public int getNumberOfItineraries(){
    return _numberOfItineraries;
  }

  public List<Itinerary> getItineraries(){
    return _itineraries;
  }

  public void addItinerary(Itinerary itinerary){
    _numberOfItineraries++;
    _itineraries.add(itinerary);
    double discount = _category.getDiscount();
    _paidValue += (itinerary.getItineraryPrice())*discount;
    _totalTime = _totalTime.plus(itinerary.getItineraryDuration());
    _category.testState();
  }

  public int getIndexOf(Itinerary itinerary){
    return _itineraries.indexOf(itinerary)+1;
  }

  public String stringToItinerary(Itinerary itinerary){
    return "Itinerário " + getIndexOf(itinerary) + " para " + itinerary.getItineraryDate() + " @ " + String.format("%.2f", itinerary.getItineraryPrice());
  }

  public double getPaidValue(){
    double paidValue = 0;
    List<Itinerary> lastTenItineraries = new ArrayList<Itinerary>();
    if(getNumberOfItineraries() < 10){
      return _paidValue;
    }
    else{
      lastTenItineraries = _itineraries.subList(_numberOfItineraries-10, _numberOfItineraries);
      for(Itinerary itinerary : lastTenItineraries)
        paidValue += itinerary.getItineraryPrice();
    }
    return paidValue;
  }

  public String stringItinerary(){
    return "== Passageiro " + _passengerID + ": " + _passengerName + " ==";
  }

  @Override
  @SuppressWarnings("nls")
  public String toString(){
    long hours = _totalTime.toHours();
    long minutes = _totalTime.toMinutes();
    return _passengerID +"|"+ _passengerName +"|" + _category.toString() + "|"
    + _numberOfItineraries + "|" + String.format("%.2f", _paidValue) + "|" + String.format("%02d",hours) + ":" + String.format("%02d",minutes-60*hours);
  }
}
