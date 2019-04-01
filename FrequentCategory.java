package mmt;

import java.io.Serializable;

public class FrequentCategory extends CategoryType implements Serializable{

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  /** Passenger Construtor
   * @param passenger
   */
  public FrequentCategory(Passenger passenger){
    super(passenger);
  }

  @Override
  public double getDiscount(){
    return 0.85;
  }

  @Override
  public String toString(){
    return "FREQUENTE";
  }
}
