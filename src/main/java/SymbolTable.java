import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

  private final SymbolTable parent;
  private final Map<String, TerminalNode> variables = new HashMap<>();

  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
  }

  public void newSymbol(String ident, TerminalNode node) {
    variables.put(ident, node);
  }

  public TerminalNode getType(String ident) {
    return variables.get(ident);
  }

  public SymbolTable getParent() {
    return parent;
  }
}
