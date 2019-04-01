package mmt;

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import mmt.exceptions.BadDateSpecificationException;
import mmt.exceptions.BadEntryException;
import mmt.exceptions.BadTimeSpecificationException;
import mmt.exceptions.InvalidPassengerNameException;
import mmt.exceptions.NoSuchDepartureException;
import mmt.exceptions.NoSuchPassengerIdException;
import mmt.exceptions.NoSuchServiceIdException;
import mmt.exceptions.NoSuchStationNameException;
import mmt.exceptions.NoSuchItineraryChoiceException;
import mmt.exceptions.NonUniquePassengerNameException;
import mmt.exceptions.ImportFileException;

import java.time.LocalDate;
import java.time.Duration;
import java.time.LocalTime;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.time.format.DateTimeParseException;

/**
 * A train company has schedules (services) for its trains and passengers that
 * acquire itineraries based on those schedules.
 */
public class TrainCompany implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  /** Passengers who use the ticket office */
  private Map<Integer, Passenger> _passengers = new TreeMap<Integer, Passenger>();

  /** Services included in the ticket office */
  private Map<Integer, Service> _services = new TreeMap<Integer, Service>();

  /** Corresponds to the list of itinerares found in the search function **/
  private List<Itinerary> _itineraryOptions = new ArrayList<Itinerary>();

  /**  Corresponds to the number of passengers who use the Train Company **/
  private int _passengerID = 0;

  /** Clears the already existing TreeMaps */
  public void resetTree(){
    _passengers.clear();
  }

  /** Return the services tree
   * @return _services This is the Map with the services inside them
   */
  public Map<Integer, Service> getServiceTree(){
    return _services;
  }

  /** Set a new service tree
   * @param servicesTree
   */
  public void setServiceTree(Map<Integer, Service> servicesTree){
    _services = servicesTree;
  }

  /** Gets the number of passengers registered into the service
   * @return _passengerID The number of passengers registered in the service
   */
  public int getNumberOfPassengers(){
    return _passengerID;
  }

  /** Imports a file with passengers, services and registers them with registerFromFields
  * @param filename
  * @throws exception ImportFileException on error on importing file
  * @throws exception IOException on error on reading the file
  * @see mmt.exceptions.ImportFileException
  */
  public void importFile(String filename) throws ImportFileException{
    try{
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    String line;
    while((line = reader.readLine()) != null){
      String[] fields = line.split("\\|");
      registerFromFields(fields);
      }
    }
      catch(IOException e){
        throw new ImportFileException();
      }
      catch(NonUniquePassengerNameException e){
        throw new ImportFileException();
      }
  }

  /** Register each line of the file into their corresponding TreeMap
   * @param fields
   * Fields is obtained from importFile
   * @throws NonUniquePassengerNameException if the name is already in use
   * @see mmt.exceptions.NonUniquePassengerNameException
   */
  public void registerFromFields(String[] fields) throws NonUniquePassengerNameException{
    if(fields[0].equals("PASSENGER")){
      registerPassenger(fields[1]);
    }
    else if(fields[0].equals("SERVICE")){
      registerService(fields);
    }
    else if(fields[0].equals("ITINERARY")){
      registerItinerary(fields);
    }
  }

  /** Searchs the Passenger treemap for a passenger with a certain name
   * @param name
   * @throws exception NonUniquePassengerNameException if the name is already in use
   * @see mmt.exceptions.NonUniquePassengerNameException
   */
  public void searchTreeMapPassenger(String name) throws NonUniquePassengerNameException{
    for(Passenger passenger : _passengers.values()){
      if(name.equals(passenger.getName())){
        throw new NonUniquePassengerNameException(name);
      }
    }
  }

  /** Checks if a certain passenger ID exists inside the treemap
   * @param passangerID
   * @throws exception NoSuchPassengerIdException if the passengerID doesn't exist
   * @see mmt.exceptions.NoSuchPassengerIdException
   */
  public void checksIDTreeMap(int passengerID) throws NoSuchPassengerIdException{
    if(!(_passengers.containsKey(passengerID))){
      throw new NoSuchPassengerIdException(passengerID);
    }
  }

  /** Checks if a certain service ID exists inside the treemap
   * @param serviceID
   * @throws exception NoSuchServiceIdException if the service ID doesn't exist
   * @see mmt.exceptions.NoSuchServiceIdException
   */
  public void checksServiceIDMap(int serviceID) throws NoSuchServiceIdException{
    if(!(_services.containsKey(serviceID))){
      throw new NoSuchServiceIdException(serviceID);
    }
  }

  /** Register the passenger if his name doesnt already exist in the treemap
   * @param name
   * @throws exception NonUniquePassengerNameException if the name is already in use
   * @see mmt.exceptions.NonUniquePassengerNameException
   */
  public void registerPassenger(String name) throws NonUniquePassengerNameException{
    searchTreeMapPassenger(name);
    Passenger passenger = new Passenger(_passengerID++, name);
    _passengers.put(passenger.getID(), passenger);
  }

  /** Registers all the services into the TreeMap
   * @param services
   * Services are obtained from importFile
   */
  public void registerService(String[] services){
    int serviceID = Integer.parseInt(services[1]);
    double price = Double.parseDouble(services[2]);
    Service service = new Service(serviceID, price);
    for(int i = 3; i<services.length; i+=2){
      LocalTime time = LocalTime.parse(services[i]);
      Departure departure = new Departure(time, services[i+1]);
      service.addDeparture(departure);
    }
    _services.put(serviceID, service);
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
    checksIDTreeMap(passengerID);
    searchTreeMapPassenger(name);
    Passenger passenger = _passengers.get(passengerID);
    passenger.setName(name);
  }

  /** Prints a passenger if the ID inserted by the user exists
   * @param passengerID
   * @throws exception NoSuchPassengerIdException if the passengerID doesn't exist
   * @return Passenger
   * @see mmt.exceptions.NoSuchPassengerIdException
   */
  public Passenger showPassengerById(int passengerID) throws NoSuchPassengerIdException{
    checksIDTreeMap(passengerID);
    Passenger passenger = _passengers.get(passengerID);
    return passenger;
  }

  /** Prints a passenger if the ID inserted by the user exists
   * @param passengerID
   * @throws exception NoSuchPassengerIdException if the serviceID doesn't exist
   * @return Passenger
   * @see mmt.exceptions.NoSuchServiceIdException
   */
  public Service showServiceByID(int serviceID) throws NoSuchServiceIdException{
    checksServiceIDMap(serviceID);
    Service service = _services.get(serviceID);
    return service;
  }

  /** Prints all registereds Passengers
   * @return Collection of Services
   */
  public Collection<Passenger> showAllPassengers(){
    return Collections.unmodifiableCollection(_passengers.values());
  }

  /** Prints all registered Services
   * @return Collection of Services
   */
  public Collection<Service> showAllServices(){
    return Collections.unmodifiableCollection(_services.values());
  }

  /**Checks if a certain station name corresponds any station of a service
   * @param stationName
   * @throws exception NoSuchStationNameException if there isn't any service
   * with the station desired by the user
   * @see mmt.exceptions.NoSuchStationNameException
   * @return Services This is a list with the services who have the desired station
   * as the departure station
   */
  public List<Service> checksDepartureStation(String stationName) throws NoSuchStationNameException{
    List<Service> services = new ArrayList<Service>();
    String stationExists = "";
    for(Service service : _services.values()){
      for(Departure departure: service.getDepartures()){
        if(stationName.equals(departure.getStation())){
          stationExists = stationName;
        }
      }
      if(stationName.equals(service.getDepartureStation().getStation())){
        services.add(service);
      }
    }
    if(stationExists.equals("")){
      throw new NoSuchStationNameException(stationName);
    }
    return services;
  }

  /** Prints, in order of departure time, all the service starting in this station
   * @param stationName
   * @throws exception NoSuchStationNameException if there isn't any service
   * with the station desired by the user
   * @return Collection of Services This collection is ordered by departure time
   */
  public Collection<Service> showServiceByDepartingStation(String stationName) throws NoSuchStationNameException{
    List<Service> services = checksDepartureStation(stationName);
    Collections.sort(services, new DepartureComparator());
    return Collections.unmodifiableCollection(services);
  }


  /**Checks if a certain station name corresponds to the first station of a service
   * @param stationName
   * @throws exception NoSuchStationNameException if there isn't any service
   * with the station desired by the user
   * @see mmt.exceptions.NoSuchStationNameException
   * @return Services This is a list with the services who have the desired station
   * as the arrival station
   */
  public List<Service> checksArrivalStation(String stationName) throws NoSuchStationNameException{
    List<Service> services = new ArrayList<Service>();
    String stationExists = "";
    for(Service service : _services.values()){
      for(Departure departure: service.getDepartures()){
        if(stationName.equals(departure.getStation())){
          stationExists = stationName;
        }
      }
      if(stationName.equals(service.getArrivalStation().getStation())){
          services.add(service);
      }
    }
    if(stationExists.equals("")){
      throw new NoSuchStationNameException(stationName);
    }
    return services;
  }

  /** Prints, in order of departure time, all the service starting in this station
   * @param stationName
   * @throws exception NoSuchStationNameException if there isn't any service
   * with the station desired by the user
   * @return Collection of Services This collection is ordered by arrival time
   */
  public Collection<Service> showServiceByArrivingStation(String stationName) throws NoSuchStationNameException{
    List<Service> services = checksArrivalStation(stationName);
    Collections.sort(services, new ArrivalComparator());
    return Collections.unmodifiableCollection(services);
  }

  /** Registers the itineraries found within the import file into each passenger
  * @param fields - corresponds to what each itinerary contains and turns it into an
  * an itinerary associated with the respective passenger ID
  */
  public void registerItinerary(String[] fields){
    Passenger passenger = _passengers.get(Integer.parseInt(fields[1]));
    Itinerary itinerary = new Itinerary(LocalDate.parse(fields[2]));
    for(int i=3; i<fields.length; i++){
      String[] services = fields[i].split("\\/");
      Service service = _services.get(Integer.parseInt(services[0]));
      itinerary.addService(service);
      List<Departure> departures = service.getDepartures();
      for(Departure departure : departures){
        if(services[1].equals(departure.getStation())){
          itinerary.addDeparture(departure);
        }
        if(services[2].equals(departure.getStation())){
          itinerary.addDeparture(departure);
        }
      }
    }
    itinerary.setItineraryPrice();
    itinerary.setDuration();
    passenger.addItinerary(itinerary);
  }

  /** Shows each itinerary associated with a certain passengerID
  * @param passengerID
  * @throws exception NoSuchPassengerIdException if the passengerID doesn't exist
  * @see mmt.exceptions.NoSuchPassengerIdException
  * @return String with all the associated itineraries for the passengerID
  */
  public String showItinerarybyPassenger(int passengerID) throws NoSuchPassengerIdException{
    String printItinerary = "";
    checksIDTreeMap(passengerID);
    Passenger passenger = _passengers.get(passengerID);
    if(passenger.getNumberOfItineraries() == 0){
      return printItinerary;
    }
    else{
      printItinerary += passenger.stringItinerary() + "\n";
      List<Itinerary> itineraries = new ArrayList<Itinerary>();
      itineraries = passenger.getItineraries();
      Collections.sort(itineraries);
      for(Itinerary itinerary : itineraries){
        printItinerary += passenger.stringToItinerary(itinerary) + "\n";
        printItinerary += itinerary.toString() + "\n";
      }
      return printItinerary;
    }
  }

  /** Shows all itineraries associated with every passenger registered to the TrainCompany
  * @return String with every single itinerary associated with each passenger within
  * the TrainCompany, if the passenger has itineraries
  */
  public String showAllItineraries(){
    String printItinerary = "";
    for(Passenger passenger : _passengers.values()){
      if(passenger.getNumberOfItineraries() != 0){
        printItinerary += passenger.stringItinerary() + "\n";
        List<Itinerary> itineraries = passenger.getItineraries();
        Collections.sort(itineraries);
        for(Itinerary itinerary : itineraries){
          printItinerary += passenger.stringToItinerary(itinerary) + "\n";
          printItinerary += itinerary.toString() + "\n";
        }
      }
    }
    return printItinerary;
  }

  /** Checks if a certain station exists within the TrainCompany Services
  * @param station
  * @throws exception NoSuchStationNameException if there isn't any service
  * with the station desired by the user
  * @see mmt.exceptions.NoSuchStationNameException
  */
  public void checksServicesForStation(String station) throws NoSuchStationNameException{
    String stationName = "";
    for(Service service : _services.values()){
      for(Departure departure : service.getDepartures()){
        if(departure.getStation().equals(station)){
          stationName = station;
        }
      }
    }
    if(stationName.equals(""))
     throw new NoSuchStationNameException(station);
    }

  /** This function does two things within the code: for a start,
  * it calculates the services for which
  * the departure Station the users inputs exists, and then
  * calculates the services for which the next station
  * in the service exists, associated with the recursive function
  * @param currentStation
  * @param departureTime
  * @return Services who contains the currentStation
  */
  public List<Service> checksServicesForCurrentStation(String currentStation, LocalTime departureTime){
    List<Service> services = new ArrayList<Service>();
    for(Service service : _services.values()){
      for(Departure departure : service.getDepartures()){
        if(!(service.getDeparture(service.getDepartures().size()-1).getStation().equals(currentStation))){
          if(departure.getStation().equals(currentStation) && departure.getDepartureTime().isAfter(departureTime)){
            services.add(service);
          }
        }
      }
    }
    return services;
  }

  /** From the recursion, when we hit a service that is direct to the
  * arrivalStation, builds the itineraries for the services who are associated
  * with the itineraries
  * @param services (list with the services associated with the itinerary)
  * @param currentStation
  * @param arrivalStation
  * @param date
  * @param time
  * @return itineraries (list with all the possible itineraries)
  */
  public List<Itinerary> buildsItinerary(List<Service> services, String departureStation, String arrivalStation, LocalDate date, LocalTime time){
    List<Itinerary> itineraries = new ArrayList<Itinerary>();
    int j = 0;
    for(Service service : services){
      for(int i = 0; i<service.getNumberOfDepartures(); i++){
        Departure departure = service.getDeparture(i);
        if(departure.getStation().equals(departureStation) && service.getIndexDeparture(departureStation)<service.getIndexDeparture(arrivalStation)){
          j = i;
        }
        if(departure.getStation().equals(arrivalStation)){
          Itinerary itinerary = new Itinerary(date);
          itinerary.addService(service);
          itinerary.addDeparture(service.getDeparture(j));
          itinerary.addDeparture(departure);
          itinerary.setItineraryPrice();
          itinerary.setDuration();
          if(!itineraries.contains(itinerary))
            itineraries.add(itinerary);
        }
      }
    }
    return itineraries;
  }

  /** Recursive function associated with the search algorithm for the itineraries
  * The recursive function has two cases: if the current service has a direct route
  * to the arrivalStation or if the current service doesn't have one direct route
  * @param services (list with the services associated with the currentStation)
  * @param servicesItinerary (list with the services for which the itinerary goes through)
  * @param departureStation
  * @param currentStation
  * @param arrivalStation
  * @param date
  * @param time
  * @return List of itineraries possible from the services within the TrainCompany
  */
  public List<Itinerary> searchRecursive(List<Service> services, List<Service> servicesItinerary, String departureStation, String currentStation, String arrivalStation, LocalDate date, LocalTime time){
    List<Itinerary> itineraries = new ArrayList<Itinerary>();
    List<Service> servicesAux = new ArrayList<Service>();
    for(Service service : services){
      if(service.containsDeparture(arrivalStation) && service.getIndexDeparture(departureStation)<service.getIndexDeparture(arrivalStation)){
        servicesItinerary.add(service);
        itineraries = buildsItinerary(servicesItinerary, departureStation, arrivalStation, date, time);
      }
      else{
        if(service.getNextStation(currentStation)!=null){
            servicesAux = checksServicesForCurrentStation(service.getNextStation(currentStation), service.getTime(currentStation));
            if(!(servicesItinerary.contains(service)))
              servicesItinerary.add(service);
            searchRecursive(servicesAux, servicesItinerary, currentStation, service.getNextStation(currentStation), arrivalStation, date, service.getTime(currentStation));
        }
      }
    }
    return itineraries;
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
       LocalDate date = LocalDate.parse(departureDate);
       LocalTime time = LocalTime.parse(departureTime);
       checksIDTreeMap(passengerId);
       checksServicesForStation(departureStation);
       checksServicesForStation(arrivalStation);
       List<Service> services = checksServicesForCurrentStation(departureStation,time);
       List<Service> servicesItinerary = new ArrayList<Service>();
       String currentStation = departureStation;
       List<Itinerary> itineraries = searchRecursive(services, servicesItinerary, departureStation, currentStation ,arrivalStation, date, time);
       _itineraryOptions = itineraries;
       Collections.sort(itineraries, new ItineraryPriceComparator());
       Collections.sort(itineraries, new ItineraryArrivalComparator());
       Collections.sort(itineraries, new ItineraryStartComparator());
     return Collections.unmodifiableCollection(_itineraryOptions);
   }

   /** Function which returns the index of the itinerary within the List
   * @param Itinerary
   * @return int (index of the itinerary)
   */
   public int getItineraryIndex(Itinerary itinerary){
     return _itineraryOptions.indexOf(itinerary)+1;
   }

   /** Function which checks if the itinerary options are empty
   * @return boolean
   * true if the itinerary options are empty
   * false if it isn't
   */
   public boolean checksItineraries(){
     return _itineraryOptions.isEmpty();
   }

   /** Functions which inserts the itinerary into the passenger
   * @param passengerId
   * @param itineraryId
   * @throws exception NoSuchItineraryChoiceException if the number inserted doesn't
   * belong to any of the itinerary choices
   * @see mmt.exceptions.NoSuchItineraryChoiceException
   */
   public void commitItinerary(int passengerId, int itineraryId) throws NoSuchItineraryChoiceException{
     if(itineraryId==0){
       _itineraryOptions.clear();
       return;
     }
     if(itineraryId<0 || itineraryId>_itineraryOptions.size()){
      _itineraryOptions.clear();
      throw new NoSuchItineraryChoiceException(passengerId, itineraryId);
     }
     Passenger passenger = _passengers.get(passengerId);
     Itinerary itinerary = _itineraryOptions.get(itineraryId-1);
     passenger.addItinerary(itinerary);
     _itineraryOptions.clear();
   }

}
