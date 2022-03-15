package assembly.instructions;

import assembly.Conditionals;
import assembly.Operand2;
import assembly.Register;

public class STR extends Instruction {

  //STR src, dest
  public STR(assembly.Instruction.InstrType type, Register dest, Operand2 operand2) {
    this.dest = dest;
    this.operand2 = operand2;
  }

  //STR{cond} src, dest
  public STR(assembly.Instruction.InstrType type, Register dest, Operand2 operand2, Conditionals conditionals) {
    this.dest = dest;
    this.operand2 = operand2;
    this.extraInformation += conditionals;
  }

  //STR{B} dest, operand
  public STR(assembly.Instruction.InstrType type, Register dest, Operand2 operand2, String suffix) {
    this.dest = dest;
    this.operand2 = operand2;
    this.extraInformation += suffix;
  }

  @Override
  public String toString() {
    return "STR" + extraInformation + " " + operand2 + ", [" + dest + "]";
  }
}
