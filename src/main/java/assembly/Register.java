package assembly;

public class Register {

  private final int number;

  public Register(int number) {
    this.number = number;
  }

  public Register next() {
    return new Register(number + 1);
  }

  public Register last() {
    return new Register(number - 1);
  }

  @Override
  public String toString() {
    if (number == 13){
      return "sp";
    }
    return "r" + number;
  }

}
