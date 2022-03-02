import ast.Type;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

  private final SymbolTable parent;
  private final Map<String, Type> variables = new HashMap<>();
  private final Map<String, Integer> variableSPMapping = new HashMap<>();

  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
  }

  public void newVariable(String ident, Type node) {
    variables.put(ident, node);
  }

  public boolean contains(String ident) {
    return variables.containsKey(ident);
  }

  public Integer getSPMapping (String ident) {
    if (!variableSPMapping.containsKey(ident) && parent != null) {
      return parent.getSPMapping(ident);
    }
    return variableSPMapping.get(ident);
  }

  public void setSPMapping (String ident, Integer absoluteSP) {
    variableSPMapping.put(ident, absoluteSP);
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
