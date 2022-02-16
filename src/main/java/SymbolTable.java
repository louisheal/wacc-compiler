import antlr.BasicParser;
import ast.Param;
import ast.Type;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

  private final SymbolTable parent;
  private final Map<String, Type> variables = new HashMap<>();
  private final Map<String, List<Param>> functionToParams = new HashMap<>();
  private final Map<String, Type.EType> functionToReturnType = new HashMap<>();


  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
  }

  public void newSymbol(String ident, Type node) {
    variables.put(ident, node);
  }

  public boolean contains(String ident) {
    return variables.containsKey(ident);
  }

  public List<Param> getFunctionParams(String ident) {
    return functionToParams.get(ident);
  }

  public Type.EType getFunctionReturnType(String ident) {
    return functionToReturnType.get(ident);
  }

  public Type getType(String ident) {
    return variables.get(ident);
  }

  public SymbolTable getParent() {
    return parent;
  }
}
