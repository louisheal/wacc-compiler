package assembly.instructions;

import assembly.Conditionals;

public class Branch extends Instruction {

  //B label
  public Branch(String label) {
    this.label = label;
  }

  //B{S}{Cond} label
  public Branch(String label, Conditionals conditionals) {
    this.label = label;
    this.extraInformation = conditionals.toString();
  }

  public Branch setSuffix(String suffix) {
    extraInformation = suffix + extraInformation;
    return this;
  }

  @Override
  public String toString() {
    return "B" + extraInformation + " " + label;
  }
}
