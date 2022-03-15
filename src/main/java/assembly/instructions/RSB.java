package assembly.instructions;

import assembly.Flags;
import assembly.Operand2;
import assembly.Register;

public class RSB extends Instruction {

  //RSB{cond} dest, operand1, operand2
  public RSB(assembly.Instruction.InstrType type, Register dest, Register operand1, Operand2 operand2) {
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
  }

  //RSB{S}{cond} dest, operand1, operand2
  public RSB(assembly.Instruction.InstrType type, Register dest, Register operand1, Operand2 operand2, Flags flag) {
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
    this.extraInformation += flag.toString();
  }

  @Override
  public String toString() {
    return "RSB" + extraInformation + " " + dest + ", " + operand1 + ", " + operand2;
  }
}
