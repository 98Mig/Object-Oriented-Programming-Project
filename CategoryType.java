package mmt;

import java.io.Serializable;

public abstract class CategoryType implements Serializable{

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  private Passenger _passenger;

  /** Passenger Construtor
   * @param passenger
   */
  public CategoryType(Passenger passenger){
    _passenger = passenger;
  }

  public Passenger getPassenger(){
    return _passenger;
  }

  public abstract double getDiscount();

  public void testState(){
    if(_passenger.getPaidValue() > 250 && _passenger.getPaidValue() <= 2500)
      _passenger.setCategory(new FrequentCategory(_passenger));
    else if(_passenger.getPaidValue() > 2500)
      _passenger.setCategory(new SpecialCategory(_passenger));
    else
      _passenger.setCategory(new NormalCategory(_passenger));
  }
}
