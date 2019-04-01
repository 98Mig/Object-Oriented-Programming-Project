package mmt;

import java.io.Serializable;

public class NormalCategory extends CategoryType implements Serializable{

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;

  /** Passenger Construtor
   * @param passenger
   */
  public NormalCategory(Passenger passenger){
    super(passenger);
  }

  @Override
  public double getDiscount(){
    return 1.00;
  }

  @Override
  public String toString(){
    return "NORMAL";
  }

}
