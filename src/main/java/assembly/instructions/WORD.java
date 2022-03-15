package assembly.instructions;

public class WORD extends Instruction {

  //.word number
  public WORD(long immValue) {
    this.immValue = immValue;
  }


  @Override
  public String toString() {
    return ".word " + immValue;
  }
}
