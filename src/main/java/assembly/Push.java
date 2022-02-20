package assembly;

public class Push extends Instruction {

  public Push(Register dest) {
    this.dest = dest;
  }

  public String toString() {
    return "PUSH {%" + dest + "}\n";
  }

}
