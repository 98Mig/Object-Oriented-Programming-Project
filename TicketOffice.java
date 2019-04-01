package mmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mmt.exceptions.BadDateSpecificationException;
import mmt.exceptions.BadTimeSpecificationException;
import mmt.exceptions.ImportFileException;
import mmt.exceptions.InvalidPassengerNameException;
import mmt.exceptions.MissingFileAssociationException;
import mmt.exceptions.NoSuchPassengerIdException;
import mmt.exceptions.NoSuchServiceIdException;
import mmt.exceptions.NoSuchStationNameException;
import mmt.exceptions.NoSuchItineraryChoiceException;
import mmt.exceptions.NonUniquePassengerNameException;
import java.text.ParseException;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Façade for handling persistence and other functions.
 */
public class TicketOffice{

  //** The object doing most of the actual work. */
  private TrainCompany _trains;

  /** The name of the file we are working on */
  private String _programFile;

  //** Construtor do TicketOffice */
  public TicketOffice(){
    _trains = new TrainCompany();
  }

  /** Saves the current state of the program within a file
   * @return _programFile This is the string associated with the filename
   */
  public String getProgramFile(){
    return _programFile;
  }

  /** Imports a file with passengers, services and registers them with registerFromFields
  * @param filename
  * @throws exception ImportFileException on error on importing file
  * @throws excepttion IOException on error on reading the file
  * @see mmt.exceptions.ImportFileException
  */
  public void importFile(String datafile) throws ImportFileException{
      _trains.importFile(datafile);
  }

  /** Resets the program, erasing the passengers and itineraries, keeping the services
   */
  public void reset() {
    Map<Integer,Service>_servicesOld = _trains.getServiceTree();
    _programFile = null;
    _trains.resetTree();
    TrainCompany newTrains = new TrainCompany();
    _trains = newTrains;
    _trains.setServiceTree(_servicesOld);
  }


  /** Saves the current state of the program within a file
   * @param filename
   */
  public void save(String filename) throws IOException, ClassNotFoundException{
    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
    out.writeObject(_trains);
    out.close();
    _programFile = filename;
  }

  public void save() throws IOException, ClassNotFoundException{
    save(_programFile);
  }

  /** Loads a previous state of the program saved within a file
   * @param filename
   */
  public void load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
    ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
    _trains = (TrainCompany) in.readObject();
    in.close();
    _programFile = filename;
  }

  /** Register the passenger if his name doesnt already exist in the treemap
   * @param name
   * @throws exception NonUniquePassengerNameException if the name is already in use
   * @see mmt.exceptions.NonUniquePassengerNameException
   */
  public void registerPassenger(String name) throws NonUniquePassengerNameException{
    _trains.registerPassenger(name);
  }

  /** Changes the passenger's ID name to a new one given by the user
   * @param passengerID
   * @param name
   * @throws exception NonUniquePassengerNameException if the name is already in use
   * @throws exception NoSuchPassengerIdException if the passengerID doesn't exist
   * @see mmt.exceptions.NonUniquePassengerNameException
   * @see mmt.exceptions.NoSuchPassengerIdException
   */
  public void changePassengerName(int passengerID, String name) throws NonUniquePassengerNameException, NoSuchPassengerIdException{
    _trains.changePassengerName(passengerID, name);
  }

  /** Prints a passenger if the ID inserted by the user exists
   * @param passengerID
   * @throws exception NoSuchPassengerIdException if the passengerID doesn't exist
   * @return Passenger
   * @see mmt.exceptions.NoSuchPassengerIdException
   */
  public Passenger showPassengerById(int passengerID) throws NoSuchPassengerIdException{
    return _trains.showPassengerById(passengerID);
  }

  /** Prints a passenger if the ID inserted by the user exists
   * @param passengerID
   * @throws exception NoSuchPassengerIdException if the serviceID doesn't exist
   * @return Passenger
   * @see mmt.exceptions.NoSuchServiceIdException
   */
  public Service showServiceByID(int serviceID) throws NoSuchServiceIdException{
    return _trains.showServiceByID(serviceID);
  }

  /** Prints a service if the ID inserted by the user exists
   * @param serviceID
   * @return Collection of Services
   */
  public Collection<Passenger> showAllPassengers(){
    return _trains.showAllPassengers();
  }

  /** Prints all registered Services
   * @return Collection of Services
   */
  public Collection<Service> showAllServices(){
    return _trains.showAllServices();
  }

  /**Checks if a certain station name corresponds to the first station of a service
   * @param stationName
   * @throws exception NoSuchStationNameException if there isn't any service
   * with the departure station desired by the user
   * @return Services This is a list with the services who have the desired station
   * as the departure station
   */
  public Collection<Service> showServiceByDepartingStation(String stationName) throws NoSuchStationNameException{
    return _trains.showServiceByDepartingStation(stationName);
  }


  /** Prints, in order of departure time, all the service starting in this station
   * @param stationName
   * @throws exception NoSuchStationNameException if there isn't any service
   * with the station desired by the user
   * @return Collection of Services This collection is ordered by arrival time
   */
  public Collection<Service> showServiceByArrivingStation(String stationName) throws NoSuchStationNameException{
    return _trains.showServiceByArrivingStation(stationName);
  }

  /** Shows each itinerary associated with a certain passengerID
  * @param passengerID
  * @throws exception NoSuchPassengerIdException if the passengerID doesn't exist
  * @see mmt.exceptions.NoSuchPassengerIdException
  * @return String with all the associated itineraries for the passengerID
  */
  public String showItinerarybyPassenger(int passengerID) throws NoSuchPassengerIdException{
    return _trains.showItinerarybyPassenger(passengerID);
  }

  /** Shows all itineraries associated with every passenger registered to the TrainCompany
  * @return String with every single itinerary associated with each passenger within
  * the TrainCompany, if the passenger has itineraries
  */
  public String showAllItineraries(){
    return _trains.showAllItineraries();
  }

  /** Function which handles all of the things associated with the search algorythm
  * @param passengerId
  * @param departureStation
  * @param arrivalStation
  * @param departureDate
  * @param departureTime
  * @throws exception NoSuchStationNameException if there isn't any service
  * with the station desired by the user
  * @see mmt.exceptions.NoSuchStationNameException
  * @throws exception NoSuchPassengerIdException if the passengerID doesn't exist
  * @see mmt.exceptions.NoSuchPassengerIdException
  * @return Collection of itineraries sorted by price, time of arrival and time of DepartureComparator
  */
  public Collection<Itinerary> search(int passengerId, String departureStation, String arrivalStation, String departureDate,
   String departureTime) throws NoSuchStationNameException, NoSuchPassengerIdException{
     return _trains.search(passengerId, departureStation, arrivalStation, departureDate, departureTime);
  }

  /** Function which returns the index of the itinerary within the List
  * @param Itinerary
  * @return int (index of the itinerary)
  */
  public int getItineraryIndex(Itinerary itinerary){
    return _trains.getItineraryIndex(itinerary);
  }

  /** Function which checks if the itinerary options are empty
  * @return boolean
  * true if the itinerary options are empty
  * false if it isn't
  */
  public boolean checksItineraries(){
    return _trains.checksItineraries();
  }

  /** Functions which inserts the itinerary into the passenger
  * @param passengerId
  * @param itineraryId
  * @throws exception NoSuchItineraryChoiceException if the number inserted doesn't
  * belong to any of the itinerary choices
  * @see mmt.exceptions.NoSuchItineraryChoiceException
  */
  public void commitItinerary(int passengerId, int itineraryId) throws NoSuchItineraryChoiceException{
     _trains.commitItinerary(passengerId, itineraryId);
  }

}
