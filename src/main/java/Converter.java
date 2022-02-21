import assembly.Instruction;
import assembly.Register;
import ast.Statement;
import java.util.ArrayList;
import java.util.List;

// This class will be used to generate assembly code from WACC code
public class Converter {
  List<Instruction> instructions = new ArrayList<>();

  private Register r0 = new Register(0);
  private Register r1 = new Register(1);
  private Register r2 = new Register(2);
  private Register r3 = new Register(3);
  private Register r4 = new Register(4);
  private Register r5 = new Register(5);
  private Register r6 = new Register(6);
  private Register r7 = new Register(7);
  private Register r8 = new Register(8);
  private Register r9 = new Register(9);
  private Register r10 = new Register(10);
  private Register r11 = new Register(11);
  private Register r12 = new Register(12);
  private Register sp = new Register(13);
  private Register pc = new Register(15);

  public void translateDeclaration(Statement statement) {

  }


}
