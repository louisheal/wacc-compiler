package assembly;

public class Operand {

  private Register register;

  public Operand(Register register) {
    this.register = register;
  }

  @Override
  public String toString() {
    return "%" + register;
  }

}
