package assembly;

public class Pop extends Instruction {

  public Pop(Register dest) {
    this.dest = dest;
  }

  public String toString() {
    return "POP {" + dest + "}";
  }

}
