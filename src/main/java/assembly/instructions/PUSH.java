package assembly.instructions;

import assembly.Register;

public class PUSH extends Instruction {

  //PUSH {dest}
  public PUSH(Register dest) {
    this.dest = dest;
  }

  @Override
  public String toString() {
    return "PUSH {" + dest + "}";
  }
}
