package ast;

public class AssignLHS {

  LHSType assignType;
  String ident;
  ArrayElem arrayElem;
  PairElem pairElem;

  enum LHSType {

    IDENT,
    ARRAYELEM,
    PAIRELEM

  }

}
