import ast.AssignRHS;
import ast.AssignRHS.RHSType;
import ast.Expression;
import ast.Function;
import ast.Param;
import ast.Program;
import ast.Statement;
import ast.Type;
import ast.Type.EType;
import java.util.List;

public class TraverseAST {
  SymbolTable currentST = new SymbolTable(null);

  private int errors = 0;

  private void printSemanticError() {
    String errorMsg = "Semantic Error";

    System.out.println(errorMsg);
  }

  public Integer getNumberOfErrors() {
    return errors;
  }

  public void traverse(Program program) {
    for (Function function : program.getFunctions()) {
      traverse(function);
    }
    traverse(program.getStatement());
  }

  private void traverse(Function function) {
    traverse(function.getParams());
    traverse(function.getStatement());
  }

  private void traverse(List<Param> params) {

  }

  private void traverse(Expression expression) {
    switch (expression.getExprType()) {
      case INTLITER:
        break;
      case BOOLLITER:
        break;
      case CHARLITER:
        break;
      case STRINGLITER:
        break;
      case IDENT:
      case ARRAYELEM:
      case PAIRLITER:
        break;
      case NOT:
      case NEG:
      case LEN:
      case CHR:
      case ORD:
      case BRACKETS:
        traverse(expression.getExpression1());
        break;
      case DIVIDE:
      case MULTIPLY:
      case MODULO:
      case PLUS:
      case MINUS:
      case GT:
      case GTE:
      case LT:
      case LTE:
      case EQ:
      case NEQ:
      case AND:
      case OR:
        traverse(expression.getExpression1());
        traverse(expression.getExpression2());
    }
  }

  private void traverse(Statement statement) {
    switch (statement.getStatType()) {
      case SKIP:
        break;
      case DECLARATION:
        currentST.newSymbol(statement.getLhsIdent(), statement.getLhsType());
        traverse(statement.getRHS().getExpression1());
        break;
      case REASSIGNMENT:
        if (currentST.contains(statement.getLhsIdent())){
          currentST.newSymbol(statement.getLhsIdent(), statement.getLhsType());
          traverse(statement.getRHS().getExpression1());
        }
        else{
          errors++;
        }
        break;
      case READ:
        break;
      case FREE:
        if(statement.getRHS().getAssignType() != RHSType.ARRAY
            || statement.getRHS().getAssignType() != RHSType.PAIRELEM
            || statement.getRHS().getAssignType() != RHSType.NEWPAIR){
          errors++;
        }
        else{
          traverse(statement.getExpression());
        }
        break;
      case RETURN:
        traverse(statement.getExpression());
        break;
      case EXIT:
        if(statement.getLhsType().getType() != EType.INT){
          errors++;
        }
        else {
          traverse(statement.getExpression());
        }
        break;
      case PRINT:
        traverse(statement.getExpression());
        break;
      case PRINTLN:
        traverse(statement.getExpression());
        break;
      case IF:
        traverse(statement.getExpression());
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
        break;
      case WHILE:
        if(statement.getExpression().getExprType()  != Expression.ExprType.BOOLLITER) {
          errors++;
          printSemanticError();
        }
        traverse(statement.getExpression());
        traverse(statement.getStatement1());
        break;
      case BEGIN:
        traverse(statement.getStatement1());
        break;
      case CONCAT:
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
        break;
    }
  }
}
