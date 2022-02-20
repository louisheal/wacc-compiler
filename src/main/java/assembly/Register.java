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

}
