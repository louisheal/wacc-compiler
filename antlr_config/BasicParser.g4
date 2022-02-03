parser grammar BasicParser;

options {
  tokenVocab=BasicLexer;
}

comment: COMMENT ;

pairLiter: NULL ;

arrayLiter: SB_OPEN (expr (COMMA expr)*)? SB_CLOSE ;

arrayElem: IDENT (SB_OPEN expr SB_CLOSE)+ ;

binaryOper: PLUS
          | MINUS
          | MULTIPLY
          | DIVIDE
          | MODULO
          | GREATER_THAN
          | GREATER_THAN_OR_EQUAL
          | LESS_THAN
          | LESS_THAN_OR_EQUAL
          | EQUAL
          | NOT_EQUAL
          | AND
          | OR ;

unaryOper: NOT
         | MINUS
         | LEN
         | ORD
         | CHR ;

expr: INT_LITER
    | BOOL_LITER
    | CHAR_LITER
    | STR_LITER
    | pairLiter
    | IDENT
    | arrayElem
    | unaryOper expr
    | expr binaryOper expr
    | P_OPEN expr P_CLOSE
    ;

pairElemBaseType: baseType
                | PAIR ;

pairElemType: baseType
            | arrayType
            | PAIR ;

pairType: PAIR P_OPEN pairElemType COMMA pairElemType P_CLOSE ;

arrayType: type SB_OPEN SB_CLOSE ;

baseType: INT
        | BOOL
        | CHAR
        | STRING ;

type: baseType
    | type SB_OPEN SB_CLOSE
    | pairType ;

pairElem: FST expr
        | SND expr ;

argList: expr (COMMA expr)* ;

assignRHS: expr
          | arrayLiter
          | NEW_PAIR P_OPEN expr COMMA expr P_CLOSE
          | pairElem
          | CALL IDENT P_OPEN ;

assignLHS: IDENT
          | arrayElem
          | pairElem ;

stat: S_SKIP
    | type IDENT ASSIGN assignRHS
    | assignLHS ASSIGN assignRHS
    | READ assignLHS
    | FREE expr
    | RETURN expr
    | EXIT expr
    | PRINT expr
    | PRINTLN expr
    | IF expr THEN stat ELSE stat FI
    | WHILE expr DO stat DONE
    | BEGIN stat END
    | stat SEMI_COLON stat ;

param: type IDENT ;

paramList: param (COMMA param)* ;

func: type IDENT P_OPEN paramList? P_CLOSE IS stat END ;

// EOF indicates that the program must consume to the end of the input.
prog: BEGIN func* stat END EOF ;
