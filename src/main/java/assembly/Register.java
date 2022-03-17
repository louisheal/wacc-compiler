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
    if (number == 13) {
      return "sp";
    }
    if (number == 14) {
      return "lr";
    }
    if (number == 15) {
      return "pc";
    }
    return "r" + number;
  }

}
