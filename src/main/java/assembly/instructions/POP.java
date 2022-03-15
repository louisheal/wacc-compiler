package assembly.instructions;

import assembly.Register;

public class POP extends Instruction{

  //POP {dest}
  public POP(Register dest) {
    this.dest = dest;
  }

  @Override
  public String toString() {
    return "POP {" + dest + "}";
  }
}
