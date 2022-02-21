package assembly;

// Move register or constant
public class MOV extends Instruction {

  private int constant;
  private Operand2 operand;

  // Move a constant to a register
  public MOV(Register dest, int constant) {
    this.dest = dest;
    this.constant = constant;
  }

  // Move operand to a register
  public MOV(Register dest, Operand2 operand) {
    this.dest = dest;
    this.operand = operand;
  }

  @Override
  public String toString() {
    if (operand == null) {
      return "MOV " + dest + ", #" + constant + "\n";
    } else {
      return "MOV " + dest + ", " + operand + "\n";
    }
  }



}
