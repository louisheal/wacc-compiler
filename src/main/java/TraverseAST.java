import ast.AssignRHS;
import ast.AssignRHS.RHSType;
import ast.Expression;
import ast.Expression.ExprType;
import ast.Function;
import ast.Param;
import ast.Program;
import ast.Statement;
import ast.Type;
import ast.Type.EType;
import java.util.List;
import java.util.Objects;

public class TraverseAST {
  SymbolTable currentST = new SymbolTable(null);

  private int errors = 0;

  private void printSemanticError() {
    String errorMsg = "Semantic Error\n";
    errors++;

    System.out.println(errorMsg);
  }


  public Integer getNumberOfErrors() {
    return errors;
  }


  public Type getExpressionType(Expression expr) {
    switch(expr.getExprType()){
      case INTLITER:
        return new Type(EType.INT);
      case BOOLLITER:
        return new Type(EType.BOOL);
      case CHARLITER:
        return new Type(EType.CHAR);
      case STRINGLITER:
        return new Type(EType.STRING);
      //case IDENT:
      case PAIRELEM:
        Type fstType = getExpressionType(expr.getExpression1());
        Type sndType = getExpressionType(expr.getExpression2());
        if(fstType.getType() == (EType.PAIR)){
          fstType = new Type(EType.PAIR);
        }
        if(sndType.getType() == (EType.PAIR)){
          sndType = new Type(EType.PAIR);
        }
        return new Type(EType.PAIR,fstType,sndType);



    }
    return null;
  }

  public EType getRHSType(AssignRHS rhs) {
    switch(rhs.getAssignType()){
      case EXPR:
        return getExpressionType(rhs.getExpression1()).getType();
      case ARRAY:
        return EType.ARRAY;
      case NEWPAIR:
        return EType.PAIR;
      case PAIRELEM:
        return getExpressionType(rhs.getPairElem().getExpression()).getType();
      case CALL:
        break;
    }
    printSemanticError();
    return null;
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
    Expression expression = statement.getExpression();
    switch (statement.getStatType()) {
      case SKIP:
        break;
      case DECLARATION:
        if (!statement.getLhsType().getType().equals(getRHSType(statement.getRHS()))) {
          printSemanticError();
        }
        currentST.newSymbol(statement.getLhsIdent(), statement.getLhsType());
        traverse(statement.getRHS().getExpression1());
        break;
      case REASSIGNMENT:
        if (!currentST.contains(statement.getLhsIdent())){
          printSemanticError();
        } else if(!currentST.getType(statement.getLhsIdent()).getType().equals(getRHSType(statement.getRHS()))){
          printSemanticError();
        }
        currentST.newSymbol(statement.getLhsIdent(), statement.getLhsType());
        traverse(statement.getRHS().getExpression1());
        break;
      case READ:
        break;
      case FREE:
        if(statement.getRHS() == null
            ||statement.getRHS().getAssignType() != RHSType.ARRAY
            || statement.getRHS().getAssignType() != RHSType.PAIRELEM
            || statement.getRHS().getAssignType() != RHSType.NEWPAIR){
          printSemanticError();
        }
        else{
          traverse(expression);
        }
        break;
      case RETURN:
      case PRINT:
      case PRINTLN:
        traverse(expression);
        break;
      case EXIT:
        if(!getExpressionType(expression).equals(new Type(EType.INT))){
          printSemanticError();
        }
        else {
          traverse(expression);
        }
        break;
      case IF:
        if(expression.getExprType() != ExprType.BOOLLITER){
          if(expression == null) {
            printSemanticError();
          }
          else {
            traverse(expression);
          }
        }
        traverse(statement.getStatement1());
        traverse(statement.getStatement2());
        break;
      case WHILE:
        if(expression.getExprType()  != Expression.ExprType.BOOLLITER) {
          printSemanticError();
        }
        traverse(expression);
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
