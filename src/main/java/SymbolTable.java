import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

  private final SymbolTable parent;
  private final Map<String, SemanticChecker.Type> variables = new HashMap<>();

  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
  }

  public void newSymbol(String ident, SemanticChecker.Type node) {
    variables.put(ident, node);
  }

  public SemanticChecker.Type getType(String ident) {
    return variables.get(ident);
  }

  public SymbolTable getParent() {
    return parent;
  }
}
