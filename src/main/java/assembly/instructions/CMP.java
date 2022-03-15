package assembly.instructions;

import assembly.Conditionals;
import assembly.Operand2;
import assembly.Register;

public class CMP extends Instruction {

  //CMP dest, immValue
  public CMP( Register dest, long immValue) {
    this.dest = dest;
    this.immValue = immValue;
  }

  //CMP{cond} dest, immValue
  public CMP(Register dest, long immValue, Conditionals conditionals) {
    this.dest = dest;
    this.immValue = immValue;
    this.extraInformation += conditionals.name();
  }

  //CMP dest, operand
  public CMP(Register dest, Operand2 operand2) {
    this.dest = dest;
    this.operand2 = operand2;
  }
  //CMP{cond} dest, operand
  public CMP(Register dest, Operand2 operand2, Conditionals conditionals) {
    this.dest = dest;
    this.operand2 = operand2;
    this.extraInformation += conditionals;
  }

  @Override
  public String toString() {
    if (operand2 == null) {
      return "CMP" + extraInformation + " " + dest + ", #" + immValue;
    }

    //With operand format
    return "CMP" + extraInformation + " " + dest + ", " + operand2;
  }
}
