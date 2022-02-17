import ast.Type;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

  private final SymbolTable parent;
  private final Map<String, Type> variables = new HashMap<>();


  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
  }

  public void newVariable(String ident, Type node) {
    variables.put(ident, node);
  }

  public boolean contains(String ident) {
    return variables.containsKey(ident);
  }

  public Type getType(String ident) {

    if (!variables.containsKey(ident) && parent != null) {
      return parent.getType(ident);
    }
    return variables.get(ident);
  }

  public SymbolTable getParent() {
    return parent;
  }
}
