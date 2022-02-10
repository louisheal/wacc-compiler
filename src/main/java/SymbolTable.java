import antlr.BasicParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

  private final SymbolTable parent;
  private final Map<String, SemanticChecker.Type> variables = new HashMap<>();
  private final Map<String, BasicParser.AssignRHSContext> values = new HashMap<>();

  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
  }

  public void newSymbol(String ident, SemanticChecker.Type node, BasicParser.AssignRHSContext value) {
    variables.put(ident, node);
    values.put(ident, value);
  }

  public boolean contains(String ident) {
    return variables.containsKey(ident);
  }

  public SemanticChecker.Type getType(String ident) {
    return variables.get(ident);
  }

  public BasicParser.AssignRHSContext getValue(String ident) {return values.get(ident);}

  public SymbolTable getParent() {
    return parent;
  }
}
