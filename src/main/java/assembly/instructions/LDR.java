package assembly.instructions;

import assembly.Conditionals;
import assembly.Operand2;
import assembly.Register;

public class LDR extends Instruction {

  //LDR dest, immValue
  public LDR(Register dest, long immValue) {
    this.dest = dest;
    this.immValue = immValue;
  }

  //LDR{cond} dest, immValue
  public LDR(Register dest, long immValue, Conditionals conditionals) {
    this.dest = dest;
    this.immValue = immValue;
    this.extraInformation += conditionals.name();
  }

  //LDR dest, operand
  public LDR(Register dest, Operand2 operand2) {
    this.dest = dest;
    this.operand2 = operand2;
  }

  //LDR{cond} dest, operand
  public LDR(Register dest, Operand2 operand2, Conditionals conditionals) {
    this.dest = dest;
    this.operand2 = operand2;
    this.extraInformation += conditionals;
  }

  //LDR{cond} dest, operand
  public LDR(Register dest, String label) {
    this.dest = dest;
    this.label = label;
  }

  //LDR{S}{cond} dest, operand
  public LDR(Register dest, String label, Conditionals conditionals) {
    this.dest = dest;
    this.label = label;
    this.extraInformation = conditionals.toString();
  }

  //LDR{B} dest, operand
  public LDR(Register dest, Operand2 operand2, String suffix) {
    this.dest = dest;
    this.operand2 = operand2;
    this.extraInformation += suffix;
  }

  @Override
  public String toString() {
    if(label != null) {
      return String.format("LDR%s %s, =%s", extraInformation, dest, label);
    } else if (operand2 == null) {
      return String.format("LDR%s %s, =%d", extraInformation, dest, immValue);
    } else {
      return String.format("LDR%s %s, [%s]", extraInformation, dest, operand2);
    }
  }
}
