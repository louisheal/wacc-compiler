package assembly;

public class Instruction {

  InstrType type;
  Register dest;
  Integer immValue;
  Operand2 operand2;

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
  public static Instruction getInstruction(InstrType type, Register dest, Integer immValue) {
    return new Instruction(type, dest, immValue, null);
  }

  //CMP{cond} dest, operand
  //MOV{cond} dest, operand
  //ADD{cond} dest, operand
  //SUB{cond} dest, operand
  //STR src, dest
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

    //STR instruction format
    if (type == InstrType.STR) {
      // STR src dest
      return type + " " + operand2 + ", [" + dest + "]";
    }

    return null;
  }

  public enum InstrType {
    PUSH, POP,
    MOV, CMP, ADD, SUB,
    LDR, STR
  }

}
