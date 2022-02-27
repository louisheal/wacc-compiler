package assembly;

import javax.lang.model.element.ModuleElement.Directive;

public class Instruction {

  InstrType type;
  Register dest;
  long immValue;
  Register operand1;
  Operand2 operand2;
  String label;

  //TODO: OPTIMIZE USE OF FIELDS

  Register rdLo;
  Register rdHi;
  Register rn;
  Register rm;

  //ADD{cond} dest, operand1, operand2
  //SUB{cond} dest, operand1, operand2
  public Instruction(InstrType type, Register dest, Register operand1, Operand2 operand2) {
    this.type = type;
    this.dest = dest;
    this.operand1 = operand1;
    this.operand2 = operand2;
  }

  //SMULL{S}{cond} RdLo, RdHi, Rn, Rm
  public Instruction(InstrType type, Register rdLo, Register rdHi, Register rn, Register rm) {
    this.type = type;
    this.rdLo = rdLo;
    this.rdHi = rdHi;
    this.rn = rn;
    this.rm = rm;
  }

  //CMP{cond} dest, immValue
  //MOV{cond} dest, immValue
  //LDR dest, immValue
  public Instruction(InstrType type, Register dest, long immValue) {
    this.type = type;
    this.dest = dest;
    this.immValue = immValue;
  }

  //CMP{cond} dest, operand
  //MOV{cond} dest, operand
  //STR{cond} src, dest
  //LDR{cond} dest, operand
  public Instruction(InstrType type, Register dest, Operand2 operand2) {
    this.type = type;
    this.dest = dest;
    this.operand2 = operand2;
  }

  //LDR{cond} dest, operand
  public Instruction(InstrType type, Register dest, String label) {
    this.type = type;
    this.dest = dest;
    this.label = label;
  }

  //BL label
  public Instruction(InstrType type, String label) {
    this.type = type;
    this.label = label;
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

    if (type == InstrType.BL) {
      return type + " " + label;
    }

    //NEG instruction format
    if (type == InstrType.NEG || type == InstrType.LEN || type == InstrType.ORD) {
      return type + " " + dest;
    }

    //ADD, SUB instruction format
    if (type == InstrType.ADD || type == InstrType.SUB) {
      return type + " " + dest + ", " + operand1 + ", " + operand2;
    }

    if (type == InstrType.SMULL) {
      return type + " " + rdLo + ", " + rdHi + ", " + rn + ", " + rm;
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
    MOV, CMP,
    NEG, LEN,
    ORD,
    ADD, SUB,
    SMULL,
    LDR, STR,
    BL,
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
