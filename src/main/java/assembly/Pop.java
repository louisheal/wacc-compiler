package assembly;

public class Pop {
  Register dest;

  public Pop(Register dest) {
    this.dest = dest;
  }

  public String toString() {
    return "POP {%" + dest + "}\n";
  }

}
