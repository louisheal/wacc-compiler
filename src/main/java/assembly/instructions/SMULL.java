package assembly.instructions;

import assembly.Register;

public class SMULL extends Instruction{

  //SMULL{S}{cond} RdLo, RdHi, Rn, Rm
  public SMULL(Register rdLo, Register rdHi, Register rn, Register rm) {
    this.rdLo = rdLo;
    this.rdHi = rdHi;
    this.rn = rn;
    this.rm = rm;
  }

  @Override
  public String toString() {
    return "SMULL " + rdLo + ", " + rdHi + ", " + rn + ", " + rm;
  }
}
