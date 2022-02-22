package assembly;

import javax.lang.model.element.ModuleElement.Directive;

public class Instruction {

  InstrType type;
  Register dest;
  Integer immValue;
  Operand2 operand2;
  String directive;

  private Instruction(InstrType type, Register dest, Integer immValue, Operand2 operand2) {
    this.type = type;
    this.dest = dest;
    this.immValue = immValue;
    this.operand2 = operand2;
  }

  //CMP{cond} dest, immValue
  //MOV{cond} dest, immValue
  //ADD{cond} dest, immValue
  //SUB{cond} dest, immValue
  //LDR dest, immValue
  public static Instruction getInstruction(InstrType type, Register dest, Integer immValue) {
    return new Instruction(type, dest, immValue, null);
  }

  //CMP{cond} dest, operand
  //MOV{cond} dest, operand
  //ADD{cond} dest, operand
  //SUB{cond} dest, operand
  //STR src, dest
  //LDR dest, operand
  public static Instruction getInstruction(InstrType type, Register dest, Operand2 operand2) {
    return new Instruction(type, dest, null, operand2);
  }

  //PUSH {dest}
  //POP {dest}
  public static Instruction getInstruction(InstrType type, Register dest) {
    return new Instruction(type, dest, null, null);
  }

  @Override
  public String toString() {

    //PUSH, POP instruction format
    if (type == InstrType.PUSH || type == InstrType.POP) {
      return type + " {" + dest + "}";
    }

    //CMP, MOV, ADD. SUB instruction format
    if (type == InstrType.CMP || type == InstrType.MOV || type == InstrType.ADD || type == InstrType.SUB) {

      //With immValue format
      if (operand2 == null) {
        return type + " " + dest + ", #" + immValue;
      }

      //With operand format
      return type + " " + dest + ", " + operand2;
    }

    if (type == InstrType.LDR) { //LDR instruction format
      if (operand2 == null) {
        return type + " " + dest + ", =" + immValue.toString().substring(1);
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
