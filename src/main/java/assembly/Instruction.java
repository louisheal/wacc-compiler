package assembly;

import javax.lang.model.element.ModuleElement.Directive;

public class Instruction {

  InstrType type;
  Register dest;
  long immValue;
  Register operand1;
  Operand2 operand2;

  //CMP{cond} dest, immValue
  //MOV{cond} dest, immValue
  //LDR dest, immValue
  public Instruction(InstrType type, Register dest, long immValue) {
    this.type = type;
    this.dest = dest;
    this.immValue = immValue;
  }

  //ADD{cond} dest, operand1, operand2
  //SUB{cond} dest, operand1, operand2
  public Instruction(InstrType type, Register dest, Register operand1, Operand2 operand2) {
    this.type = type;
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
  }

  //CMP{cond} dest, operand
  //MOV{cond} dest, operand
  //STR src, dest
  //LDR dest, operand
  public Instruction(InstrType type, Register dest, Operand2 operand2) {
    this.type = type;
    this.dest = dest;
    this.operand2 = operand2;
  }

  //PUSH {dest}
  //POP {dest}
  public Instruction(InstrType type, Register dest) {
    this.type = type;
    this.dest = dest;
  }

  @Override
  public String toString() {

    //PUSH, POP instruction format
    if (type == InstrType.PUSH || type == InstrType.POP) {
      return type + " {" + dest + "}";
    }

    //ADD, SUB instruction format
    if (type == InstrType.ADD || type == InstrType.SUB) {
      return type + " " + dest + ", " + operand1 + ", " + operand2;
    }

    //CMP, MOV instruction format
    if (type == InstrType.CMP || type == InstrType.MOV) {

      //With immValue format
      if (operand2 == null) {
        return type + " " + dest + ", #" + immValue;
      }

      //With operand format
      return type + " " + dest + ", " + operand2;
    }

    if (type == InstrType.LDR) { //LDR instruction format
      if (operand2 == null) {
        return type + " " + dest + ", =" + immValue;
      } else {
        return  type + " " + dest + ", [" + operand2 + "]";
      }
    } else if (type == InstrType.STR) { //STR instruction format
      // STR src dest
      return type + " " + operand2 + ", [" + dest + "]";
    }
    return type.toString();
  }

  public enum InstrType {
    PUSH, POP,
    MOV, CMP, ADD, SUB, LDR, STR,
    TEXT{
      public String toString() {
        return ".text";
      }
    },
    DATA{
      public String toString() {
        return ".data";
      }
    },
   LTORG{
      public String toString() {
        return ".ltorg";
      }
    },
    GLOBAL_MAIN{
      public String toString() {
        return ".global main";
      }
    }
  }

}
