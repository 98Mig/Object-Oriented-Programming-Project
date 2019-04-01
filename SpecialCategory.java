package mmt;

import java.io.Serializable;

public class SpecialCategory extends CategoryType implements Serializable{

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  /** Passenger Construtor
   * @param passenger
   */
  public SpecialCategory(Passenger passenger){
    super(passenger);
  }

  @Override
  public double getDiscount(){
    return 0.50;
  }

  @Override
  public String toString(){
    return "ESPECIAL";
  }
}
