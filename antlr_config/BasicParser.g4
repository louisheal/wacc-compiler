parser grammar BasicParser;

options {
  tokenVocab=BasicLexer;
}

// EOF indicates that the program must consume to the end of the input.
prog: BEGIN func* stat END EOF ;

func: type IDENT P_OPEN paramList? P_CLOSE IS stat END ;

paramList: param (COMMA param)* ;

param: type IDENT ;

intLiter: INTEGER ;
signedIntLiter: PLUS INTEGER    #positiveInt
              | MINUS INTEGER   #negativeInt
              ;

boolLiter: BOOL_LITER ;

charLiter: CHAR_LITER ;

stringLiter: STR_LITER ;

stat: S_SKIP                            #skip
    | type IDENT ASSIGN assignRHS       #declaration
    | assignLHS ASSIGN assignRHS        #reassignment
    | READ assignLHS                    #read
    | FREE expr                         #free
    | RETURN expr                       #return
    | EXIT expr                         #exit
    | PRINT expr                        #print
    | PRINTLN expr                      #println
    | IF expr THEN stat ELSE stat FI    #if_then_else_fi
    | WHILE expr DO stat DONE           #while_do_done
    | BEGIN stat END                    #begin_end
    | stat SEMI_COLON stat              #semi_colon 
    ;

assignLHS: IDENT        #identLHS
         | arrayElem    #arrayLHS
         | pairElem     #pairLHS
         ;

expr: intLiter                          #intExpr
    | signedIntLiter                    #signedIntExpr
    | boolLiter                         #boolExpr
    | charLiter                         #charExpr
    | stringLiter                       #stringExpr
    | pairLiter                         #pairExpr
    | IDENT                             #identExpr
    | arrayElem                         #arrayExpr
    | unaryOper expr                    #unOp
    | expr DIVIDE expr                  #divExpr
    | expr MULTIPLY expr                #mulExpr
    | expr MODULO expr                  #modExpr
    | expr PLUS expr                    #plusExpr
    | expr MINUS expr                   #minusExpr
    | expr GREATER_THAN expr            #gtExpr
    | expr GREATER_THAN_OR_EQUAL expr   #gteExpr
    | expr LESS_THAN expr               #ltExpr
    | expr LESS_THAN_OR_EQUAL expr      #lteExpr
    | expr EQUAL expr                   #eqExpr
    | expr NOT_EQUAL expr               #neqExpr
    | expr AND expr                     #andExpr
    | expr OR expr                      #orExpr
    | P_OPEN expr P_CLOSE               #brExpr
    ;

assignRHS: expr                                     #exprRHS
         | arrayLiter                               #arrayRHS
         | NEW_PAIR P_OPEN expr COMMA expr P_CLOSE  #newPairRHS
         | pairElem                                 #pairElemRHS
         | CALL IDENT P_OPEN argList P_CLOSE        #callRHS
         ;

argList: expr (COMMA expr)* | ; // second empty in case no args taken

pairElem: FST expr      #fstElem
        | SND expr      #sndElem
        ;

type: baseType
    | arrayType
    | pairType
    ;

baseType: INT       #intType
        | BOOL      #boolType
        | CHAR      #charType
        | STRING    #stringType
        ;

arrayType: baseType SB_OPEN SB_CLOSE    #baseArrayType
         | pairType SB_OPEN SB_CLOSE    #pairArrayType
         | arrayType SB_OPEN SB_CLOSE   #nestedArrayType 
         ;

pairType: PAIR P_OPEN pairElemType COMMA pairElemType P_CLOSE ;

pairElemType: baseType      #basePairElem
            | arrayType     #arrayPairElem
            | PAIR          #pairPairElem
            ;

unaryOper: NOT      #not
         | MINUS    #minus
         | LEN      #len
         | ORD      #ord
         | CHR      #chr
         ;

arrayElem: IDENT (SB_OPEN expr SB_CLOSE)+ ;

arrayLiter: SB_OPEN (expr (COMMA expr)*)? SB_CLOSE ;

pairLiter: NULL ;

comment: COMMENT ;
