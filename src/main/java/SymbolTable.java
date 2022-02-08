import java.util.HashMap;
import java.util.Map;

public class SemanticChecker {

  Map<String, Type> variable = new HashMap<>();

  enum Type {
    INT,
    CHAR,
    STRING,
    BOOL,
    PAIR,
    ARRAY
  }
}
