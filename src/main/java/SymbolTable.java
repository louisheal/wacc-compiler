import ast.Type;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

  private final SymbolTable parent;
  private final Map<String, Type.EType> variables = new HashMap<>();

  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
  }

  public boolean contains(String ident) {return variables.containsKey(ident);}

  public void newSymbol(String ident, Type.EType node) {
    variables.put(ident, node);
    values.put(ident, value);
  }

  public boolean contains(String ident) {
    return variables.containsKey(ident);
  }

  public Type.EType getType(String ident) {
    return variables.get(ident);
  }

  public BasicParser.AssignRHSContext getValue(String ident) {return values.get(ident);}

  public SymbolTable getParent() {
    return parent;
  }
}
