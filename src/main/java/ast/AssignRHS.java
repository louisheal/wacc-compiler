package ast;

import java.util.List;

public class AssignRHS {

  RHSType assignType;
  Expression expression1;
  Expression expression2;
  List<Expression> array;
  PairElem pairElem;
  String functionIdent;
  List<Argument> argList;

  enum RHSType {

    EXPR,
    ARRAY,
    NEWPAIR,
    PAIRELEM,
    CALL

  }

}
