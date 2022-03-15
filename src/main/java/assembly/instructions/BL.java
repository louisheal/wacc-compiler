package assembly.instructions;

import assembly.Conditionals;

public class BL extends Instruction {

  //BL label
  //.ascii label
  public BL(String label) {
    this.label = label;
  }

  //BL{VS} label
  public BL(String label, Conditionals conditionals) {
    this.label = label;
    this.extraInformation = conditionals.toString();
  }

  @Override
  public String toString() {
    return "BL" + extraInformation + " " + label;
  }
}
