package assembly;

public class Push {
  Register dest;

  public Push(Register dest) {
    this.dest = dest;
  }

  public String toString() {
    return "PUSH {%" + dest + "}\n";
  }

}
