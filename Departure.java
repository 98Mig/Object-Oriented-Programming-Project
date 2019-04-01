package mmt;

import java.time.LocalTime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Departure implements Serializable{

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  private LocalTime _departureTime;
  private String _station;

  /** Departure Construtor
   * @param departuretime
   * @param station
   */
  public Departure(LocalTime departuretime, String station){
    _station = station;
    _departureTime = departuretime;
  }

  public void setStation(String station){
    _station = station;
  }
  
  public void setDepartureTime(LocalTime departuretime){
    _departureTime = departuretime;
  }

  public String getStation(){
    return _station;
  }

  public LocalTime getDepartureTime(){
    return _departureTime;
  }

  @Override
  @SuppressWarnings("nls")
  public String toString(){
    return _departureTime + " " + _station + "\n";
  }
}
